package viewmodel

import connection.ProductEntry
import i18n.BundleKeys
import i18n.LocaleManager
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import net.GatewayResult
import net.ServerGateway
import net.onSuccess
import net.onFailure
import objects.Product
import script.ScriptExecutor
import session.AppSession
import sortfilter.TableFilter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * ViewModel for the main application window.
 *
 * Holds the authoritative in-memory collection as [masterList] and exposes a two-layer
 * observable view over it: [filteredList] applies a [TableFilter]-built predicate that
 * uses the Stream API to match entries, and [sortedList] wraps it for column-header sort
 * in the [javafx.scene.control.TableView].
 *
 * All gateway calls execute on daemon background threads. Result callbacks are always
 * dispatched to the JavaFX Application Thread via [Platform.runLater].
 *
 * @property gateway the server facade
 * @property session the current user session
 */
class MainViewModel(
    private val gateway: ServerGateway,
    val session: AppSession
) {

    private val masterList = FXCollections.observableArrayList<ProductEntry>()

    /**
     * Read-only observable view of all entries in the collection.
     * Used by [view.CanvasView] to render and animate product nodes independently of
     * the filter applied to [filteredList].
     */
    val allEntries: ObservableList<ProductEntry> = masterList

    /**
     * Stream-filtered view of [masterList].
     * The predicate is rebuilt from [filterText] using [TableFilter.buildPredicate]
     * which internally uses [java.util.stream.Stream.anyMatch] across all field values.
     */
    val filteredList: FilteredList<ProductEntry> = FilteredList(masterList)

    /**
     * Sorted view of [filteredList] whose comparator is bound to the [javafx.scene.control.TableView]
     * comparator property from [view.MainView].
     */
    val sortedList: SortedList<ProductEntry> = SortedList(filteredList)

    /** Text typed in the filter field; updating it rebuilds the [filteredList] predicate. */
    val filterText = SimpleStringProperty("")

    /** Human-readable result of the last command, shown in the status bar. */
    val statusText = SimpleStringProperty("")

    /** True while a network request is in flight. */
    val isLoading = SimpleBooleanProperty(false)

    init {
        filterText.addListener { _, _, newValue ->
            filteredList.setPredicate(TableFilter.buildPredicate(newValue ?: ""))
        }
    }

    /**
     * Fetches all collection entries from the server and replaces [masterList].
     * The existing filter and sort state are preserved; only the underlying data changes.
     *
     * @param onComplete optional callback invoked on the Application Thread when done
     */
    fun refresh(onComplete: (() -> Unit)? = null) {
        runCommand({ gateway.show() }) { result ->
            result.onSuccess { entries -> masterList.setAll(entries) }
                .onFailure { msg -> statusText.set(msg) }
            onComplete?.invoke()
        }
    }

    /**
     * Requests collection metadata and passes the result to [onComplete].
     *
     * @param onComplete receives the info string or an error description
     */
    fun info(onComplete: (GatewayResult<String>) -> Unit) =
        runCommand({ gateway.info() }, onComplete)

    /**
     * Inserts [product] under [key] and refreshes the list on success.
     *
     * @param key the collection key
     * @param product the product to insert
     * @param onComplete called with the operation result
     */
    fun insert(key: String, product: Product, onComplete: (GatewayResult<Unit>) -> Unit) =
        runCommand({ gateway.insert(key, product) }) { result ->
            result.onSuccess { statusText.set(LocaleManager[BundleKeys.MSG_INSERT_OK]) }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Replaces the product with [id] with [product] and refreshes the list on success.
     *
     * @param id the id of the product to replace
     * @param product the new product data
     * @param onComplete called with the operation result
     */
    fun update(id: Long, product: Product, onComplete: (GatewayResult<Unit>) -> Unit) =
        runCommand({ gateway.update(id, product) }) { result ->
            result.onSuccess { statusText.set(LocaleManager[BundleKeys.MSG_UPDATE_OK]) }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Removes the element at [key] and refreshes the list on success.
     *
     * @param key the collection key to remove
     * @param onComplete called with the operation result
     */
    fun removeKey(key: String, onComplete: (GatewayResult<Unit>) -> Unit) =
        runCommand({ gateway.removeKey(key) }) { result ->
            result.onSuccess { statusText.set(LocaleManager[BundleKeys.MSG_REMOVE_OK]) }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Removes all products belonging to the current user and refreshes the list on success.
     *
     * @param onComplete called with the operation result
     */
    fun clear(onComplete: (GatewayResult<Unit>) -> Unit) =
        runCommand({ gateway.clear() }) { result ->
            result.onSuccess { statusText.set(LocaleManager[BundleKeys.MSG_CLEAR_OK]) }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Removes all user-owned products cheaper than [product] and refreshes the list on success.
     *
     * @param product the reference product
     * @param onComplete called with the number of removed products or an error
     */
    fun removeLower(product: Product, onComplete: (GatewayResult<Int>) -> Unit) =
        runCommand({ gateway.removeLower(product) }) { result ->
            result.onSuccess { count ->
                statusText.set(LocaleManager.get(BundleKeys.MSG_REMOVED_COUNT, count))
            }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Replaces the product at [key] with [product] if [product] is greater and refreshes on success.
     *
     * @param key the collection key
     * @param product the candidate replacement
     * @param onComplete called with true if replaced, false if not
     */
    fun replaceIfGreater(key: String, product: Product, onComplete: (GatewayResult<Boolean>) -> Unit) =
        runCommand({ gateway.replaceIfGreater(key, product) }) { result ->
            result.onSuccess { replaced ->
                val msg = if (replaced) LocaleManager[BundleKeys.MSG_REPLACE_REPLACED]
                else LocaleManager[BundleKeys.MSG_REPLACE_NOT_REPLACED]
                statusText.set(msg)
            }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Removes all user-owned products with a key lexicographically greater than [key].
     *
     * @param key the exclusive boundary key
     * @param onComplete called with the number of removed products or an error
     */
    fun removeGreaterKey(key: String, onComplete: (GatewayResult<Int>) -> Unit) =
        runCommand({ gateway.removeGreaterKey(key) }) { result ->
            result.onSuccess { count ->
                statusText.set(LocaleManager.get(BundleKeys.MSG_REMOVED_COUNT, count))
            }
            if (result is GatewayResult.Success) refresh()
            onComplete(result)
        }

    /**
     * Counts products with a price greater than [price].
     *
     * @param price the price threshold
     * @param onComplete called with the count or an error
     */
    fun countGreaterThanPrice(price: Long, onComplete: (GatewayResult<Long>) -> Unit) =
        runCommand({ gateway.countGreaterThanPrice(price) }, onComplete)

    /**
     * Retrieves all distinct unit-of-measure values present in the collection.
     *
     * @param onComplete called with the list of enum name strings or an error
     */
    fun uniqueUnitOfMeasure(onComplete: (GatewayResult<List<String>>) -> Unit) =
        runCommand({ gateway.uniqueUnitOfMeasure() }, onComplete)

    /**
     * Retrieves all product prices sorted in descending order.
     *
     * @param onComplete called with the sorted price list or an error
     */
    fun pricesDescending(onComplete: (GatewayResult<List<Long>>) -> Unit) =
        runCommand({ gateway.pricesDescending() }, onComplete)

    /**
     * Returns true when [entry] belongs to the currently authenticated user.
     *
     * @param entry the entry to check
     * @return true if [entry.creatorLogin] matches the current user's login
     */
    fun isOwner(entry: ProductEntry): Boolean =
        entry.creatorLogin == session.currentLogin

    /**
     * Starts periodic polling of the collection from the server.
     * Uses [scheduleWithFixedDelay] so the next poll starts only after the previous
     * network call completes, preventing concurrent requests.
     * Silently ignores network errors to tolerate temporary server unavailability.
     * Has no effect if polling is already running; call [stopPolling] first to change the interval.
     *
     * @param intervalMs delay between polls in milliseconds; defaults to 2500
     */
    fun startPolling(intervalMs: Long = 2500L) {
        stopPolling()
        pollerScheduler = Executors.newSingleThreadScheduledExecutor { r ->
            Thread(r, "collection-poller").apply { isDaemon = true }
        }
        pollerScheduler?.scheduleWithFixedDelay({
            when (val result = gateway.show()) {
                is GatewayResult.Success ->
                    Platform.runLater { masterList.setAll(result.value) }
                is GatewayResult.Failure -> Unit
            }
        }, intervalMs, intervalMs, TimeUnit.MILLISECONDS)
    }

    /**
     * Stops the background polling scheduler if it is running.
     * Safe to call when polling is not active.
     */
    fun stopPolling() {
        pollerScheduler?.shutdownNow()
        pollerScheduler = null
    }

    /**
     * Executes a command script file asynchronously on a daemon thread.
     * After execution, refreshes the collection and calls [onComplete] on the
     * JavaFX Application Thread with the execution log.
     *
     * @param filePath absolute or relative path to the script file
     * @param onComplete called with the list of log lines when execution finishes
     */
    fun executeScript(filePath: String, onComplete: (List<String>) -> Unit) {
        Platform.runLater { isLoading.set(true) }
        Thread {
            val log = ScriptExecutor(gateway).execute(filePath)
            when (val r = gateway.show()) {
                is GatewayResult.Success -> Platform.runLater {
                    masterList.setAll(r.value)
                    isLoading.set(false)
                    onComplete(log)
                }
                is GatewayResult.Failure -> Platform.runLater {
                    isLoading.set(false)
                    onComplete(log)
                }
            }
        }.apply { isDaemon = true }.start()
    }

    private var pollerScheduler: ScheduledExecutorService? = null

    private fun <T> runCommand(
        action: () -> GatewayResult<T>,
        onComplete: (GatewayResult<T>) -> Unit
    ) {
        Platform.runLater { isLoading.set(true) }
        Thread {
            val result = action()
            Platform.runLater {
                isLoading.set(false)
                onComplete(result)
            }
        }.apply { isDaemon = true }.start()
    }
}