package view

import connection.ProductEntry
import i18n.BundleKeys
import i18n.LocaleManager
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SelectionMode
import javafx.scene.control.Separator
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.TextInputDialog
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import net.GatewayResult
import net.onFailure
import net.onSuccess
import viewmodel.MainViewModel
import java.util.Locale

/**
 * Main application window displayed after the user authenticates.
 *
 * Layout (top to bottom inside [BorderPane]):
 * TOP - [MenuBar] with all collection commands, then a [HBox] toolbar with the current
 *        user label, logout button, and language selector.
 * CENTER - a [VBox] containing a filter row ([TextField] backed by
 *        [viewmodel.MainViewModel.filterText]) and a [TabPane] with a table tab and a
 *        canvas tab (placeholder until increment 6).
 * BOTTOM - a [Label] status bar showing the result of the last operation.
 *
 * The [TableView] uses [viewmodel.MainViewModel.sortedList] as its items source.
 * Column-header sort is wired by binding
 * [javafx.collections.transformation.SortedList.comparatorProperty] to
 * [TableView.comparatorProperty].
 * Filtering is performed via [sortfilter.TableFilter.buildPredicate] which uses
 * [java.util.stream.Stream.anyMatch] over all field values of each entry.
 *
 * @property stage the primary application stage
 * @property viewModel the main view model
 * @property onLogout called when the user clicks Log Out; expected to clear the session
 *           and navigate back to the auth screen
 */
class MainView(
    private val stage: Stage,
    private val viewModel: MainViewModel,
    private val onLogout: () -> Unit
) {

    private val menuCollection = Menu()
    private val menuModify = Menu()
    private val menuRemove = Menu()
    private val menuQuery = Menu()

    private val miInfo = MenuItem()
    private val miRefresh = MenuItem()
    private val miExecuteScript = MenuItem()
    private val miInsert = MenuItem()
    private val miUpdate = MenuItem()
    private val miReplaceIfGreater = MenuItem()
    private val miRemoveKey = MenuItem()
    private val miRemoveLower = MenuItem()
    private val miRemoveGreaterKey = MenuItem()
    private val miClear = MenuItem()
    private val miCountGreaterPrice = MenuItem()
    private val miUniqueUnit = MenuItem()
    private val miPricesDesc = MenuItem()

    private val ctxEdit = MenuItem()
    private val ctxDelete = MenuItem()

    private val userLabel = Label()
    private val logoutButton = Button()
    private val languageBox = ComboBox<Locale>()

    private val filterLabel = Label()
    private val filterField = TextField()

    private val tabTable = Tab()
    private val tabCanvas = Tab()

    private val statusLabel = Label()

    private val colKey = TableColumn<ProductEntry, String>()
    private val colCreator = TableColumn<ProductEntry, String>()
    private val colId = TableColumn<ProductEntry, String>()
    private val colName = TableColumn<ProductEntry, String>()
    private val colCoordX = TableColumn<ProductEntry, String>()
    private val colCoordY = TableColumn<ProductEntry, String>()
    private val colCreationDate = TableColumn<ProductEntry, String>()
    private val colPrice = TableColumn<ProductEntry, String>()
    private val colPartNumber = TableColumn<ProductEntry, String>()
    private val colManufactureCost = TableColumn<ProductEntry, String>()
    private val colUnitOfMeasure = TableColumn<ProductEntry, String>()
    private val colOwnerName = TableColumn<ProductEntry, String>()
    private val colOwnerHeight = TableColumn<ProductEntry, String>()
    private val colOwnerHairColor = TableColumn<ProductEntry, String>()
    private val colOwnerNationality = TableColumn<ProductEntry, String>()

    private lateinit var tableView: TableView<ProductEntry>

    private val canvasView = CanvasView(viewModel)

    /**
     * Builds the scene, wires all bindings, subscribes to locale changes,
     * shows the stage, and triggers an initial collection refresh.
     */
    fun show() {
        stage.scene = buildScene()
        viewModel.sortedList.comparatorProperty().bind(tableView.comparatorProperty())

        stage.isResizable = true
        stage.width = 1280.0
        stage.height = 740.0
        stage.centerOnScreen()

        bindControls()
        setupLanguageBox()
        setupTableColumns()
        setupContextMenu()
        refreshTexts()

        LocaleManager.localeProperty.addListener { _, _, _ ->
            refreshTexts()
            tableView.refresh()
        }

        stage.show()
        canvasView.initialize()
        viewModel.startPolling()
        viewModel.refresh()
    }

    private fun buildScene(): Scene {
        val menuBar = buildMenuBar()
        val toolbar = buildToolbar()
        val topBar = VBox(menuBar, toolbar)

        val filterRow = buildFilterRow()
        tableView = buildTableView()
        VBox.setVgrow(tableView, Priority.ALWAYS)
        val tableContent = VBox(8.0, filterRow, tableView).apply {
            padding = Insets(8.0, 10.0, 0.0, 10.0)
        }

        tabTable.isClosable = false
        tabTable.content = tableContent

        tabCanvas.isClosable = false
        tabCanvas.content = canvasView.root

        val tabPane = TabPane(tabTable, tabCanvas).apply {
            tabMinWidth = 90.0
        }

        val statusBar = buildStatusBar()

        val root = BorderPane().apply {
            top = topBar
            center = tabPane
            bottom = statusBar
            style = "-fx-background-color: #f8fafc;"
        }

        return Scene(root, 1280.0, 740.0)
    }

    private fun buildMenuBar(): MenuBar {
        menuCollection.items.addAll(miInfo, miRefresh, SeparatorMenuItem(), miExecuteScript)
        menuModify.items.addAll(miInsert, miUpdate, miReplaceIfGreater)
        menuRemove.items.addAll(miRemoveKey, miRemoveLower, miRemoveGreaterKey, SeparatorMenuItem(), miClear)
        menuQuery.items.addAll(miCountGreaterPrice, miUniqueUnit, miPricesDesc)

        miInfo.setOnAction { handleInfo() }
        miRefresh.setOnAction { viewModel.refresh { statusLabel.text = "" } }
        miExecuteScript.setOnAction { handleExecuteScript() }
        miInsert.setOnAction { openInsertDialog() }
        miUpdate.setOnAction { handleUpdateSelected() }
        miReplaceIfGreater.setOnAction { openReplaceIfGreaterDialog() }
        miRemoveKey.setOnAction { handleRemoveKey() }
        miRemoveLower.setOnAction { openRemoveLowerDialog() }
        miRemoveGreaterKey.setOnAction { handleRemoveGreaterKey() }
        miClear.setOnAction { handleClear() }
        miCountGreaterPrice.setOnAction { handleCountGreaterPrice() }
        miUniqueUnit.setOnAction { handleUniqueUnit() }
        miPricesDesc.setOnAction { handlePricesDesc() }

        return MenuBar(menuCollection, menuModify, menuRemove, menuQuery)
    }

    private fun buildToolbar(): HBox {
        userLabel.style = "-fx-font-size: 13px; -fx-text-fill: #374151;"

        logoutButton.apply {
            style = "-fx-background-color: transparent; -fx-border-color: #d1d5db; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 4 12 4 12;"
            setOnAction { onLogout() }
        }

        val spacer = HBox().apply { HBox.setHgrow(this, Priority.ALWAYS) }
        val separator = Separator(javafx.geometry.Orientation.VERTICAL).apply {
            HBox.setMargin(this, Insets(4.0, 6.0, 4.0, 6.0))
        }

        return HBox(12.0).apply {
            alignment = Pos.CENTER_LEFT
            padding = Insets(8.0, 12.0, 8.0, 12.0)
            style = "-fx-background-color: white; " +
                    "-fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;"
            children.addAll(spacer, userLabel, logoutButton, separator, languageBox)
        }
    }

    private fun buildFilterRow(): HBox {
        filterField.apply {
            prefWidth = 320.0
            style = "-fx-background-radius: 6; -fx-border-radius: 6; " +
                    "-fx-border-color: #d1d5db; -fx-border-width: 1; " +
                    "-fx-font-size: 13px; -fx-padding: 5 10 5 10;"
        }
        val clearBtn = Button("\u00d7").apply {
            style = "-fx-background-color: transparent; -fx-font-size: 15px; " +
                    "-fx-cursor: hand; -fx-padding: 2 6 2 6;"
            setOnAction { filterField.clear() }
        }
        return HBox(6.0, filterLabel, filterField, clearBtn).apply {
            alignment = Pos.CENTER_LEFT
        }
    }

    private fun buildTableView(): TableView<ProductEntry> {
        val allColumns = listOf(
            colKey, colCreator, colId, colName, colCoordX, colCoordY,
            colCreationDate, colPrice, colPartNumber, colManufactureCost,
            colUnitOfMeasure, colOwnerName, colOwnerHeight, colOwnerHairColor,
            colOwnerNationality
        )
        return TableView<ProductEntry>().apply {
            items = viewModel.sortedList
            selectionModel.selectionMode = SelectionMode.SINGLE
            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
            columns.addAll(allColumns)
            setOnKeyPressed { e ->
                if (e.code == KeyCode.DELETE) handleDeleteSelected()
            }
            style = "-fx-font-size: 13px;"
        }
    }

    private fun buildStatusBar(): Label {
        return statusLabel.apply {
            padding = Insets(5.0, 12.0, 5.0, 12.0)
            style = "-fx-background-color: #f1f5f9; -fx-font-size: 12px; -fx-text-fill: #64748b;"
            maxWidth = Double.MAX_VALUE
            textProperty().bind(viewModel.statusText)
        }
    }

    private fun setupTableColumns() {
        colKey.apply {
            prefWidth = 80.0
            setCellValueFactory { SimpleStringProperty(it.value.key) }
        }
        colCreator.apply {
            prefWidth = 90.0
            setCellValueFactory { SimpleStringProperty(it.value.creatorLogin) }
        }
        colId.apply {
            prefWidth = 60.0
            setCellValueFactory { SimpleStringProperty(it.value.product.id.toString()) }
        }
        colName.apply {
            prefWidth = 140.0
            setCellValueFactory { SimpleStringProperty(it.value.product.name) }
        }
        colCoordX.apply {
            prefWidth = 60.0
            setCellValueFactory {
                SimpleStringProperty(LocaleManager.formatLong(it.value.product.coordinates.x))
            }
        }
        colCoordY.apply {
            prefWidth = 70.0
            setCellValueFactory {
                SimpleStringProperty(LocaleManager.formatDouble(it.value.product.coordinates.y))
            }
        }
        colCreationDate.apply {
            prefWidth = 150.0
            setCellValueFactory {
                SimpleStringProperty(LocaleManager.formatDateTime(it.value.product.creationDate))
            }
        }
        colPrice.apply {
            prefWidth = 90.0
            setCellValueFactory {
                SimpleStringProperty(LocaleManager.formatLong(it.value.product.price))
            }
        }
        colPartNumber.apply {
            prefWidth = 100.0
            setCellValueFactory { SimpleStringProperty(it.value.product.partNumber ?: "") }
        }
        colManufactureCost.apply {
            prefWidth = 110.0
            setCellValueFactory {
                val v = it.value.product.manufactureCost
                SimpleStringProperty(if (v != null) LocaleManager.formatLong(v) else "")
            }
        }
        colUnitOfMeasure.apply {
            prefWidth = 110.0
            setCellValueFactory {
                SimpleStringProperty(it.value.product.unitOfMeasure?.name ?: "")
            }
        }
        colOwnerName.apply {
            prefWidth = 110.0
            setCellValueFactory { SimpleStringProperty(it.value.product.owner.name) }
        }
        colOwnerHeight.apply {
            prefWidth = 70.0
            setCellValueFactory {
                SimpleStringProperty(LocaleManager.formatLong(it.value.product.owner.height))
            }
        }
        colOwnerHairColor.apply {
            prefWidth = 90.0
            setCellValueFactory { SimpleStringProperty(it.value.product.owner.hairColor.name) }
        }
        colOwnerNationality.apply {
            prefWidth = 100.0
            setCellValueFactory {
                SimpleStringProperty(it.value.product.owner.nationality?.name ?: "")
            }
        }
    }

    private fun setupContextMenu() {
        ctxEdit.setOnAction { handleUpdateSelected() }
        ctxDelete.setOnAction { handleDeleteSelected() }
        val menu = ContextMenu(ctxEdit, ctxDelete)
        tableView.contextMenu = menu
    }

    private fun bindControls() {
        filterField.textProperty().bindBidirectional(viewModel.filterText)
        val busy = viewModel.isLoading
        menuCollection.disableProperty().bind(busy)
        menuModify.disableProperty().bind(busy)
        menuRemove.disableProperty().bind(busy)
        menuQuery.disableProperty().bind(busy)
        logoutButton.disableProperty().bind(busy)
        canvasView.onEditRequest = { entry ->
            if (viewModel.isOwner(entry)) openUpdateDialog(entry)
            else showError(LocaleManager[BundleKeys.ERR_NOT_OWNER])
        }
    }

    private fun setupLanguageBox() {
        languageBox.items.addAll(LocaleManager.supportedLocales)
        languageBox.value = LocaleManager.locale
        val makeCell = {
            object : ListCell<Locale>() {
                override fun updateItem(item: Locale?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item?.takeIf { !empty }?.let { LocaleManager.localeDisplayNames[it] }
                }
            }
        }
        languageBox.setCellFactory { makeCell() }
        languageBox.buttonCell = makeCell()
        languageBox.setOnAction {
            languageBox.value?.takeIf { it != LocaleManager.locale }
                ?.let { LocaleManager.locale = it }
        }
    }

    private fun refreshTexts() {
        stage.title = LocaleManager[BundleKeys.MAIN_WINDOW_TITLE]

        userLabel.text = "${LocaleManager[BundleKeys.MAIN_CURRENT_USER]}: " +
                viewModel.session.currentLogin
        logoutButton.text = LocaleManager[BundleKeys.MAIN_LOGOUT]

        menuCollection.text = LocaleManager[BundleKeys.BTN_INFO].substringBefore(' ')
            .let { "Collection" }
        menuCollection.text = "Collection"
        menuModify.text = "Modify"
        menuRemove.text = "Remove"
        menuQuery.text = "Query"

        miInfo.text = LocaleManager[BundleKeys.BTN_INFO]
        miRefresh.text = LocaleManager[BundleKeys.BTN_REFRESH]
        miExecuteScript.text = LocaleManager[BundleKeys.BTN_EXECUTE_SCRIPT]
        miInsert.text = LocaleManager[BundleKeys.BTN_INSERT]
        miUpdate.text = LocaleManager[BundleKeys.BTN_UPDATE]
        miReplaceIfGreater.text = LocaleManager[BundleKeys.BTN_REPLACE_IF_GREATER]
        miRemoveKey.text = LocaleManager[BundleKeys.BTN_REMOVE_KEY]
        miRemoveLower.text = LocaleManager[BundleKeys.BTN_REMOVE_LOWER]
        miRemoveGreaterKey.text = LocaleManager[BundleKeys.BTN_REMOVE_GREATER_KEY]
        miClear.text = LocaleManager[BundleKeys.BTN_CLEAR]
        miCountGreaterPrice.text = LocaleManager[BundleKeys.BTN_COUNT_GREATER_PRICE]
        miUniqueUnit.text = LocaleManager[BundleKeys.BTN_UNIQUE_UNIT]
        miPricesDesc.text = LocaleManager[BundleKeys.BTN_PRICES_DESC]

        ctxEdit.text = LocaleManager[BundleKeys.BTN_EDIT]
        ctxDelete.text = LocaleManager[BundleKeys.BTN_DELETE]

        tabTable.text = LocaleManager[BundleKeys.TAB_TABLE]
        tabCanvas.text = LocaleManager[BundleKeys.TAB_CANVAS]

        filterLabel.text = LocaleManager[BundleKeys.FILTER_PLACEHOLDER]
        filterField.promptText = LocaleManager[BundleKeys.FILTER_PLACEHOLDER]

        colKey.text = LocaleManager[BundleKeys.COL_KEY]
        colCreator.text = LocaleManager[BundleKeys.COL_CREATOR]
        colId.text = LocaleManager[BundleKeys.COL_ID]
        colName.text = LocaleManager[BundleKeys.COL_NAME]
        colCoordX.text = LocaleManager[BundleKeys.COL_COORD_X]
        colCoordY.text = LocaleManager[BundleKeys.COL_COORD_Y]
        colCreationDate.text = LocaleManager[BundleKeys.COL_CREATION_DATE]
        colPrice.text = LocaleManager[BundleKeys.COL_PRICE]
        colPartNumber.text = LocaleManager[BundleKeys.COL_PART_NUMBER]
        colManufactureCost.text = LocaleManager[BundleKeys.COL_MANUFACTURE_COST]
        colUnitOfMeasure.text = LocaleManager[BundleKeys.COL_UNIT_OF_MEASURE]
        colOwnerName.text = LocaleManager[BundleKeys.COL_OWNER_NAME]
        colOwnerHeight.text = LocaleManager[BundleKeys.COL_OWNER_HEIGHT]
        colOwnerHairColor.text = LocaleManager[BundleKeys.COL_OWNER_HAIR_COLOR]
        colOwnerNationality.text = LocaleManager[BundleKeys.COL_OWNER_NATIONALITY]
    }

    private fun handleInfo() {
        viewModel.info { result ->
            result.onSuccess { text ->
                showInfo(LocaleManager[BundleKeys.DLG_INFO_TITLE], text)
            }.onFailure { msg -> showError(msg) }
        }
    }

    private fun handleRemoveKey() {
        val selected = tableView.selectionModel.selectedItem
        if (selected != null) {
            confirmAndRun(
                LocaleManager[BundleKeys.DLG_DELETE_CONFIRM],
                LocaleManager[BundleKeys.DLG_DELETE_TITLE]
            ) {
                viewModel.removeKey(selected.key) { r ->
                    r.onFailure { msg -> showError(msg) }
                }
            }
        } else {
            askText(LocaleManager[BundleKeys.FORM_KEY]) { key ->
                viewModel.removeKey(key) { r ->
                    r.onFailure { msg -> showError(msg) }
                }
            }
        }
    }

    private fun handleDeleteSelected() {
        val selected = tableView.selectionModel.selectedItem ?: run {
            showError(LocaleManager[BundleKeys.ERR_NO_SELECTION])
            return
        }
        if (!viewModel.isOwner(selected)) {
            showError(LocaleManager[BundleKeys.ERR_NOT_OWNER])
            return
        }
        confirmAndRun(
            LocaleManager[BundleKeys.DLG_DELETE_CONFIRM],
            LocaleManager[BundleKeys.DLG_DELETE_TITLE]
        ) {
            viewModel.removeKey(selected.key) { r ->
                r.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun handleUpdateSelected() {
        val selected = tableView.selectionModel.selectedItem ?: run {
            showError(LocaleManager[BundleKeys.ERR_NO_SELECTION])
            return
        }
        if (!viewModel.isOwner(selected)) {
            showError(LocaleManager[BundleKeys.ERR_NOT_OWNER])
            return
        }
        openUpdateDialog(selected)
    }

    private fun handleClear() {
        confirmAndRun(
            LocaleManager[BundleKeys.MSG_CLEAR_OK] + "?",
            LocaleManager[BundleKeys.BTN_CLEAR]
        ) {
            viewModel.clear { r ->
                r.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun handleRemoveGreaterKey() {
        askText(LocaleManager[BundleKeys.FORM_KEY]) { key ->
            viewModel.removeGreaterKey(key) { r ->
                r.onSuccess { count ->
                    showInfo(
                        LocaleManager[BundleKeys.BTN_REMOVE_GREATER_KEY],
                        LocaleManager.get(BundleKeys.MSG_REMOVED_COUNT, count)
                    )
                }.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun handleCountGreaterPrice() {
        askText(LocaleManager[BundleKeys.FORM_PRICE]) { input ->
            val price = input.trim().toLongOrNull() ?: run {
                showError(LocaleManager[BundleKeys.ERR_INVALID_NUMBER])
                return@askText
            }
            viewModel.countGreaterThanPrice(price) { r ->
                r.onSuccess { count ->
                    showInfo(
                        LocaleManager[BundleKeys.BTN_COUNT_GREATER_PRICE],
                        LocaleManager.get(BundleKeys.MSG_COUNT_RESULT, count)
                    )
                }.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun handleUniqueUnit() {
        viewModel.uniqueUnitOfMeasure { r ->
            r.onSuccess { values ->
                showInfo(
                    LocaleManager[BundleKeys.BTN_UNIQUE_UNIT],
                    values.ifEmpty { listOf("-") }.joinToString("\n")
                )
            }.onFailure { msg -> showError(msg) }
        }
    }

    private fun handlePricesDesc() {
        viewModel.pricesDescending { r ->
            r.onSuccess { prices ->
                val text = prices.ifEmpty { listOf(0L) }
                    .joinToString("\n") { LocaleManager.formatLong(it) }
                showInfo(LocaleManager[BundleKeys.BTN_PRICES_DESC], text)
            }.onFailure { msg -> showError(msg) }
        }
    }

    private fun openInsertDialog() {
        ProductFormDialog(ProductFormDialog.Mode.INSERT).showAndWait()?.let { result ->
            viewModel.insert(result.key, result.product) { r ->
                r.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun openUpdateDialog(entry: ProductEntry) {
        ProductFormDialog(ProductFormDialog.Mode.UPDATE, entry).showAndWait()?.let { result ->
            viewModel.update(entry.product.id, result.product) { r ->
                r.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun openReplaceIfGreaterDialog() {
        val selected = tableView.selectionModel.selectedItem
        val prefill = selected?.takeIf { viewModel.isOwner(it) }
        ProductFormDialog(ProductFormDialog.Mode.REPLACE_IF_GREATER, prefill).showAndWait()
            ?.let { result ->
                viewModel.replaceIfGreater(result.key, result.product) { r ->
                    r.onSuccess { replaced ->
                        val msg = if (replaced) LocaleManager[BundleKeys.MSG_REPLACE_REPLACED]
                        else LocaleManager[BundleKeys.MSG_REPLACE_NOT_REPLACED]
                        showInfo(LocaleManager[BundleKeys.BTN_REPLACE_IF_GREATER], msg)
                    }.onFailure { msg -> showError(msg) }
                }
            }
    }

    private fun openRemoveLowerDialog() {
        ProductFormDialog(ProductFormDialog.Mode.REMOVE_LOWER).showAndWait()?.let { result ->
            viewModel.removeLower(result.product) { r ->
                r.onSuccess { count ->
                    showInfo(
                        LocaleManager[BundleKeys.BTN_REMOVE_LOWER],
                        LocaleManager.get(BundleKeys.MSG_REMOVED_COUNT, count)
                    )
                }.onFailure { msg -> showError(msg) }
            }
        }
    }

    private fun handleExecuteScript() {
        val chooser = javafx.stage.FileChooser().apply {
            title = LocaleManager[BundleKeys.DLG_SCRIPT_TITLE]
            extensionFilters.add(
                javafx.stage.FileChooser.ExtensionFilter("Script files", "*.txt", "*.script", "*.*")
            )
        }
        val file = chooser.showOpenDialog(stage) ?: return
        viewModel.executeScript(file.absolutePath) { lines ->
            showScriptResults(lines)
        }
    }

    private fun showScriptResults(lines: List<String>) {
        val textArea = javafx.scene.control.TextArea(lines.joinToString("\n")).apply {
            isEditable = false
            prefRowCount = 22
            prefColumnCount = 65
            maxWidth = Double.MAX_VALUE
            maxHeight = Double.MAX_VALUE
        }
        Alert(Alert.AlertType.INFORMATION).apply {
            title = LocaleManager[BundleKeys.BTN_EXECUTE_SCRIPT]
            headerText = LocaleManager[BundleKeys.DLG_SCRIPT_TITLE]
            dialogPane.expandableContent = textArea
            dialogPane.isExpanded = true
            dialogPane.prefWidth = 680.0
        }.showAndWait()
    }

    private fun showInfo(title: String, message: String) {
        Alert(Alert.AlertType.INFORMATION).apply {
            this.title = title
            headerText = null
            contentText = message
        }.showAndWait()
    }

    private fun showError(message: String) {
        viewModel.statusText.set(message)
        Alert(Alert.AlertType.ERROR).apply {
            title = LocaleManager[BundleKeys.ERR_SERVER]
            headerText = null
            contentText = message
        }.showAndWait()
    }

    private fun confirmAndRun(message: String, title: String, action: () -> Unit) {
        Alert(Alert.AlertType.CONFIRMATION).apply {
            this.title = title
            headerText = null
            contentText = message
        }.showAndWait().ifPresent { btn ->
            if (btn == ButtonType.OK) action()
        }
    }

    private fun askText(prompt: String, onSubmit: (String) -> Unit) {
        TextInputDialog().apply {
            title = prompt
            headerText = null
            contentText = prompt
        }.showAndWait().ifPresent { value ->
            if (value.isNotBlank()) onSubmit(value.trim())
        }
    }
}