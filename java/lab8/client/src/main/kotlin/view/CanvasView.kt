package view

import connection.ProductEntry
import i18n.BundleKeys
import i18n.LocaleManager
import javafx.animation.FadeTransition
import javafx.animation.FillTransition
import javafx.animation.ParallelTransition
import javafx.animation.ScaleTransition
import javafx.animation.SequentialTransition
import javafx.collections.ListChangeListener
import javafx.scene.Cursor
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tooltip
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import viewmodel.MainViewModel
import kotlin.math.sqrt

/**
 * Canvas-based visualisation of the product collection.
 *
 * Each [ProductEntry] is represented as a [Circle] node placed on a fixed-size [Pane].
 * The circle's horizontal position is derived from the product's X coordinate (range 0..321),
 * and the vertical position is derived from the Y coordinate (scaled around canvas centre).
 * The circle radius is proportional to the square root of the product price.
 * Products created by the same user share the same colour, chosen deterministically from
 * [PALETTE] using the hash of the creator login.
 *
 * The view subscribes to [MainViewModel.allEntries] and diffs each incoming snapshot against
 * the previous one to detect additions, removals, and updates. Each detected change triggers
 * a matching animation:
 *
 * - Add: scale from 0 to 1 and fade in ([ANIM_ADD_MS] ms).
 * - Remove: scale to 0 and fade out ([ANIM_REMOVE_MS] ms), then removes the node.
 * - Update: pulse scale (1 to 1.3 back to 1) in parallel with a colour flash ([ANIM_UPDATE_MS] ms).
 *
 * Clicking a circle opens an [Alert] with all product fields formatted according to the
 * current locale.
 *
 * @property viewModel the shared view model that owns the collection list
 */
class CanvasView(private val viewModel: MainViewModel) {

    private data class EntryNode(val circle: Circle, val label: Label)

    /**
     * Invoked when the user selects "Edit" from a circle's context menu.
     * Set by [MainView] to open the [ProductFormDialog] for the clicked entry.
     * If null, the edit action is silently ignored.
     */
    var onEditRequest: ((ProductEntry) -> Unit)? = null

    private val canvasPane = Pane().apply {
        prefWidth = CANVAS_WIDTH
        prefHeight = CANVAS_HEIGHT
        style = "-fx-background-color: #eef2ff;"
    }

    /** The scroll pane that wraps [canvasPane]; set as the canvas tab content in [view.MainView]. */
    val root: ScrollPane = ScrollPane(canvasPane).apply {
        isPannable = true
        isFitToWidth = true
        isFitToHeight = true
        style = "-fx-background: #eef2ff; -fx-background-color: #eef2ff;"
    }

    private val nodes = mutableMapOf<String, EntryNode>()
    private var snapshot = mapOf<String, ProductEntry>()

    init {
        viewModel.allEntries.addListener(ListChangeListener { _ ->
            syncCanvas(viewModel.allEntries.toList())
        })
    }

    /**
     * Renders the current contents of [MainViewModel.allEntries] without animation.
     * Must be called once after [view.MainView.show] to display the initial state.
     */
    fun initialize() {
        syncCanvas(viewModel.allEntries.toList())
    }

    private fun syncCanvas(newEntries: List<ProductEntry>) {
        val newMap = newEntries.associateBy { it.key }
        snapshot.keys.filter { it !in newMap }.forEach { animateRemove(it) }
        newMap.values.filter { it.key !in snapshot }.forEach { animateAdd(it) }
        newMap.values.filter { e -> e.key in snapshot && snapshot[e.key] != e }
            .forEach { animateUpdate(it) }
        snapshot = newMap
    }

    private fun animateAdd(entry: ProductEntry) {
        val node = buildNode(entry)
        nodes[entry.key] = node
        canvasPane.children.addAll(node.circle, node.label)
        node.circle.scaleX = 0.0
        node.circle.scaleY = 0.0
        node.circle.opacity = 0.0
        node.label.opacity = 0.0
        ParallelTransition(
            ScaleTransition(Duration.millis(ANIM_ADD_MS), node.circle)
                .also { it.toX = 1.0; it.toY = 1.0 },
            FadeTransition(Duration.millis(ANIM_ADD_MS), node.circle)
                .also { it.toValue = CIRCLE_OPACITY },
            FadeTransition(Duration.millis(ANIM_ADD_MS * 1.3), node.label)
                .also { it.toValue = 1.0 }
        ).play()
    }

    private fun animateRemove(key: String) {
        val node = nodes[key] ?: return
        ParallelTransition(
            ScaleTransition(Duration.millis(ANIM_REMOVE_MS), node.circle)
                .also { it.toX = 0.0; it.toY = 0.0 },
            FadeTransition(Duration.millis(ANIM_REMOVE_MS), node.circle)
                .also { it.toValue = 0.0 },
            FadeTransition(Duration.millis(ANIM_REMOVE_MS), node.label)
                .also { it.toValue = 0.0 }
        ).apply {
            setOnFinished {
                canvasPane.children.removeAll(node.circle, node.label)
                nodes.remove(key)
            }
        }.play()
    }

    private fun animateUpdate(entry: ProductEntry) {
        val node = nodes[entry.key] ?: run { animateAdd(entry); return }
        val color = userColor(entry.creatorLogin)
        val (cx, cy) = toScreen(entry.product.coordinates.x, entry.product.coordinates.y)
        val r = toRadius(entry.product.price)

        node.circle.apply { fill = color; radius = r; centerX = cx; centerY = cy }
        node.label.apply {
            text = entry.product.name
            layoutX = cx - 30.0
            layoutY = cy + r + 3.0
        }
        node.circle.setOnMouseClicked { e ->
            if (e.button == javafx.scene.input.MouseButton.PRIMARY) showInfo(entry)
        }
        node.circle.setOnContextMenuRequested { e ->
            buildContextMenu(entry).show(node.circle, e.screenX, e.screenY)
        }
        Tooltip.install(node.circle, buildTooltip(entry))

        val halfDuration = Duration.millis(ANIM_UPDATE_MS / 2.0)
        ParallelTransition(
            SequentialTransition(
                ScaleTransition(halfDuration, node.circle).also { it.toX = 1.3; it.toY = 1.3 },
                ScaleTransition(halfDuration, node.circle).also { it.toX = 1.0; it.toY = 1.0 }
            ),
            FillTransition(Duration.millis(ANIM_UPDATE_MS), node.circle, color.brighter(), color)
        ).play()
    }

    private fun buildNode(entry: ProductEntry): EntryNode {
        val color = userColor(entry.creatorLogin)
        val (cx, cy) = toScreen(entry.product.coordinates.x, entry.product.coordinates.y)
        val r = toRadius(entry.product.price)

        val circle = Circle(cx, cy, r).apply {
            fill = color
            stroke = color.darker()
            strokeWidth = 1.5
            opacity = CIRCLE_OPACITY
            cursor = Cursor.HAND
        }
        Tooltip.install(circle, buildTooltip(entry))
        circle.setOnMouseClicked { e ->
            if (e.button == javafx.scene.input.MouseButton.PRIMARY) showInfo(entry)
        }
        circle.setOnContextMenuRequested { e ->
            buildContextMenu(entry).show(circle, e.screenX, e.screenY)
        }

        val label = Label(entry.product.name).apply {
            layoutX = cx - 30.0
            layoutY = cy + r + 3.0
            style = "-fx-font-size: 10px; -fx-text-fill: #374151;"
            isMouseTransparent = true
        }
        return EntryNode(circle, label)
    }

    private fun buildContextMenu(entry: ProductEntry): javafx.scene.control.ContextMenu {
        val editItem = javafx.scene.control.MenuItem(
            LocaleManager[i18n.BundleKeys.BTN_EDIT]
        ).apply {
            setOnAction { onEditRequest?.invoke(entry) }
        }
        val infoItem = javafx.scene.control.MenuItem(
            LocaleManager[i18n.BundleKeys.DLG_OBJECT_INFO_TITLE]
        ).apply {
            setOnAction { showInfo(entry) }
        }
        return javafx.scene.control.ContextMenu(editItem, infoItem)
    }

    private fun buildTooltip(entry: ProductEntry): Tooltip = Tooltip(
        "${entry.product.name}\n" +
                "${LocaleManager[BundleKeys.COL_PRICE]}: ${LocaleManager.formatLong(entry.product.price)}\n" +
                "${LocaleManager[BundleKeys.COL_CREATOR]}: ${entry.creatorLogin}"
    )

    private fun showInfo(entry: ProductEntry) {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = LocaleManager[BundleKeys.DLG_OBJECT_INFO_TITLE]
            headerText = "${entry.product.name}  [${entry.key}]"
            contentText = buildInfoText(entry)
        }.show()
    }

    private fun buildInfoText(entry: ProductEntry): String {
        val p = entry.product
        return buildString {
            appendLine("${LocaleManager[BundleKeys.COL_KEY]}: ${entry.key}")
            appendLine("${LocaleManager[BundleKeys.COL_CREATOR]}: ${entry.creatorLogin}")
            appendLine("${LocaleManager[BundleKeys.COL_ID]}: ${p.id}")
            appendLine("${LocaleManager[BundleKeys.COL_NAME]}: ${p.name}")
            appendLine("${LocaleManager[BundleKeys.COL_COORD_X]}: ${p.coordinates.x}   " +
                    "${LocaleManager[BundleKeys.COL_COORD_Y]}: ${p.coordinates.y}")
            appendLine("${LocaleManager[BundleKeys.COL_CREATION_DATE]}: " +
                    LocaleManager.formatDateTime(p.creationDate))
            appendLine("${LocaleManager[BundleKeys.COL_PRICE]}: ${LocaleManager.formatLong(p.price)}")
            p.partNumber?.let {
                appendLine("${LocaleManager[BundleKeys.COL_PART_NUMBER]}: $it")
            }
            p.manufactureCost?.let {
                appendLine("${LocaleManager[BundleKeys.COL_MANUFACTURE_COST]}: " +
                        LocaleManager.formatLong(it))
            }
            p.unitOfMeasure?.let {
                appendLine("${LocaleManager[BundleKeys.COL_UNIT_OF_MEASURE]}: ${it.name}")
            }
            appendLine("${LocaleManager[BundleKeys.COL_OWNER_NAME]}: ${p.owner.name}")
            appendLine("${LocaleManager[BundleKeys.COL_OWNER_HEIGHT]}: " +
                    LocaleManager.formatLong(p.owner.height))
            appendLine("${LocaleManager[BundleKeys.COL_OWNER_HAIR_COLOR]}: ${p.owner.hairColor.name}")
            p.owner.nationality?.let {
                appendLine("${LocaleManager[BundleKeys.COL_OWNER_NATIONALITY]}: ${it.name}")
            }
        }.trimEnd()
    }

    private fun userColor(login: String): Color =
        PALETTE[Math.abs(login.hashCode()) % PALETTE.size]

    /**
     * Maps product coordinates to canvas pixel coordinates.
     * X (range 0..321) maps linearly to [PADDING, CANVAS_WIDTH - PADDING].
     * Y is centred at the canvas midpoint; one coordinate unit equals [Y_SCALE] pixels.
     * Results are clamped to the canvas bounds with [PADDING] margin.
     *
     * @param x the product X coordinate
     * @param y the product Y coordinate
     * @return screen position as (screenX, screenY)
     */
    private fun toScreen(x: Long, y: Double): Pair<Double, Double> {
        val sx = PADDING + (x.toDouble() / MAX_COORD_X) * (CANVAS_WIDTH - 2.0 * PADDING)
        val sy = CANVAS_HEIGHT / 2.0 - y * Y_SCALE
        return sx.coerceIn(PADDING, CANVAS_WIDTH - PADDING) to
                sy.coerceIn(PADDING, CANVAS_HEIGHT - PADDING)
    }

    /**
     * Converts product price to a circle radius using a square-root scale so that very large
     * prices do not produce unusably large circles.
     *
     * @param price the product price
     * @return radius in pixels, clamped to [MIN_RADIUS]..[MAX_RADIUS]
     */
    private fun toRadius(price: Long): Double =
        (MIN_RADIUS + sqrt(price / 60.0)).coerceAtMost(MAX_RADIUS)

    private companion object {
        const val CANVAS_WIDTH = 920.0
        const val CANVAS_HEIGHT = 620.0
        const val PADDING = 64.0
        const val MAX_COORD_X = 321.0
        const val Y_SCALE = 0.85
        const val CIRCLE_OPACITY = 0.88
        const val MIN_RADIUS = 8.0
        const val MAX_RADIUS = 50.0
        const val ANIM_ADD_MS = 420.0
        const val ANIM_REMOVE_MS = 340.0
        const val ANIM_UPDATE_MS = 360.0

        val PALETTE: List<Color> = listOf(
            Color.web("#4f46e5"),
            Color.web("#0ea5e9"),
            Color.web("#10b981"),
            Color.web("#f59e0b"),
            Color.web("#ef4444"),
            Color.web("#8b5cf6"),
            Color.web("#ec4899"),
            Color.web("#14b8a6"),
            Color.web("#f97316"),
            Color.web("#6366f1")
        )
    }
}