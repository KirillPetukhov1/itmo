package view

import connection.ProductEntry
import i18n.BundleKeys
import i18n.LocaleManager
import javafx.event.ActionEvent
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.Dialog
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Modality
import objects.Color as HairColor
import objects.Coordinates
import objects.Country
import objects.Person
import objects.Product
import objects.UnitOfMeasure

/**
 * A modal dialog for creating or editing a [Product].
 *
 * The form adapts to [Mode]:
 * - [Mode.INSERT]: shows the key field; all product fields are empty.
 * - [Mode.UPDATE]: hides the key field; pre-fills all fields from [prefill].
 * - [Mode.REPLACE_IF_GREATER]: shows the key field; product fields empty.
 * - [Mode.REMOVE_LOWER]: hides the key field; used to specify a reference product for comparison.
 *
 * Validation runs when the user clicks OK via an [ActionEvent] filter on the OK button.
 * If validation fails the dialog remains open and the error is shown at the bottom of the form.
 *
 * All labels are locale-aware and refresh immediately when [LocaleManager.locale] changes.
 *
 * @property mode determines which fields are shown and how the title is chosen
 * @property prefill an existing entry used to pre-populate fields in [Mode.UPDATE]; ignored otherwise
 */
class ProductFormDialog(
    private val mode: Mode,
    private val prefill: ProductEntry? = null
) {

    /**
     * Operating modes of the form.
     */
    enum class Mode { INSERT, UPDATE, REPLACE_IF_GREATER, REMOVE_LOWER }

    /**
     * The data returned when the user confirms the form.
     *
     * @property key the collection key; empty string when not applicable to the current mode
     * @property product the product built from the form fields
     */
    data class Result(val key: String, val product: Product)

    private val keyField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val nameField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val coordXField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val coordYField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val priceField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val partNumberField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val manufactureCostField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val unitBox = ComboBox<String>().apply { maxWidth = Double.MAX_VALUE }
    private val ownerNameField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val ownerHeightField = TextField().apply { maxWidth = Double.MAX_VALUE }
    private val hairColorBox = ComboBox<String>().apply { maxWidth = Double.MAX_VALUE }
    private val nationalityBox = ComboBox<String>().apply { maxWidth = Double.MAX_VALUE }

    private val errorLabel = Label().apply {
        style = "-fx-text-fill: #dc2626; -fx-font-size: 12px;"
        isWrapText = true
        isVisible = false
        maxWidth = 380.0
    }

    private val labelNodes = mutableListOf<Pair<Label, String>>()

    /**
     * Builds and displays the dialog modally.
     * Blocks until the user confirms or cancels.
     *
     * @return the [Result] built from the form on confirmation, or null on cancellation
     */
    fun showAndWait(): Result? {
        val dialog = Dialog<Result>()
        dialog.initModality(Modality.APPLICATION_MODAL)
        dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
        dialog.dialogPane.prefWidth = 460.0

        populateComboBoxes()
        prefill?.let { applyPrefill(it) }

        dialog.dialogPane.content = buildContent()
        applyTexts(dialog)

        LocaleManager.localeProperty.addListener { _, _, _ -> applyTexts(dialog) }

        dialog.dialogPane.lookupButton(ButtonType.OK)
            .addEventFilter(ActionEvent.ACTION) { event ->
                val error = validate()
                if (error != null) {
                    errorLabel.text = error
                    errorLabel.isVisible = true
                    event.consume()
                } else {
                    errorLabel.isVisible = false
                }
            }

        dialog.setResultConverter { btn ->
            if (btn == ButtonType.OK) buildResult() else null
        }

        return dialog.showAndWait().orElse(null)
    }

    private fun populateComboBoxes() {
        unitBox.items.add(NONE)
        unitBox.items.addAll(UnitOfMeasure.values().map { it.name })
        unitBox.value = NONE

        hairColorBox.items.addAll(HairColor.values().map { it.name })
        hairColorBox.value = HairColor.GREEN.name

        nationalityBox.items.add(NONE)
        nationalityBox.items.addAll(Country.values().map { it.name })
        nationalityBox.value = NONE
    }

    private fun applyPrefill(entry: ProductEntry) {
        val p = entry.product
        keyField.text = entry.key
        nameField.text = p.name
        coordXField.text = p.coordinates.x.toString()
        coordYField.text = p.coordinates.y.toString()
        priceField.text = p.price.toString()
        partNumberField.text = p.partNumber ?: ""
        manufactureCostField.text = p.manufactureCost?.toString() ?: ""
        unitBox.value = p.unitOfMeasure?.name ?: NONE
        ownerNameField.text = p.owner.name
        ownerHeightField.text = p.owner.height.toString()
        hairColorBox.value = p.owner.hairColor.name
        nationalityBox.value = p.owner.nationality?.name ?: NONE
    }

    private fun buildContent(): VBox {
        val grid = GridPane().apply {
            hgap = 12.0
            vgap = 9.0
            padding = Insets(16.0, 16.0, 8.0, 16.0)
            columnConstraints.addAll(
                ColumnConstraints(150.0),
                ColumnConstraints(180.0, 210.0, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true)
            )
        }

        var row = 0

        fun addRow(labelKey: String, field: javafx.scene.Node, show: Boolean = true) {
            val lbl = Label().apply { style = ROW_LABEL_STYLE }
            labelNodes.add(lbl to labelKey)
            grid.add(lbl, 0, row)
            grid.add(field, 1, row)
            row++
            if (!show) {
                lbl.isManaged = false; lbl.isVisible = false
                field.isManaged = false; field.isVisible = false
            }
        }

        val showKey = mode == Mode.INSERT || mode == Mode.REPLACE_IF_GREATER
        addRow(BundleKeys.FORM_KEY, keyField, showKey)
        addRow(BundleKeys.FORM_NAME, nameField)
        addRow(BundleKeys.FORM_COORD_X, coordXField)
        addRow(BundleKeys.FORM_COORD_Y, coordYField)
        addRow(BundleKeys.FORM_PRICE, priceField)
        addRow(BundleKeys.FORM_PART_NUMBER, partNumberField)
        addRow(BundleKeys.FORM_MANUFACTURE_COST, manufactureCostField)
        addRow(BundleKeys.FORM_UNIT_OF_MEASURE, unitBox)
        addRow(BundleKeys.FORM_OWNER_NAME, ownerNameField)
        addRow(BundleKeys.FORM_OWNER_HEIGHT, ownerHeightField)
        addRow(BundleKeys.FORM_OWNER_HAIR_COLOR, hairColorBox)
        addRow(BundleKeys.FORM_OWNER_NATIONALITY, nationalityBox)

        return VBox(8.0, grid, errorLabel).apply {
            padding = Insets(0.0, 8.0, 4.0, 8.0)
        }
    }

    private fun applyTexts(dialog: Dialog<Result>) {
        dialog.title = when (mode) {
            Mode.INSERT -> LocaleManager[BundleKeys.DLG_INSERT_TITLE]
            Mode.UPDATE -> LocaleManager[BundleKeys.DLG_UPDATE_TITLE]
            Mode.REPLACE_IF_GREATER -> LocaleManager[BundleKeys.BTN_REPLACE_IF_GREATER]
            Mode.REMOVE_LOWER -> LocaleManager[BundleKeys.BTN_REMOVE_LOWER]
        }
        dialog.headerText = null
        labelNodes.forEach { (lbl, key) -> lbl.text = LocaleManager[key] }
        (dialog.dialogPane.lookupButton(ButtonType.OK) as? Button)
            ?.text = LocaleManager[BundleKeys.BTN_OK]
        (dialog.dialogPane.lookupButton(ButtonType.CANCEL) as? Button)
            ?.text = LocaleManager[BundleKeys.BTN_CANCEL]
    }

    private fun validate(): String? {
        if (mode == Mode.INSERT || mode == Mode.REPLACE_IF_GREATER) {
            if (keyField.text.isBlank())
                return "${LocaleManager[BundleKeys.ERR_BLANK_FIELD]} (${LocaleManager[BundleKeys.FORM_KEY]})"
        }
        if (nameField.text.isBlank())
            return "${LocaleManager[BundleKeys.ERR_BLANK_FIELD]} (${LocaleManager[BundleKeys.FORM_NAME]})"
        val x = coordXField.text.trim().toLongOrNull()
            ?: return "${LocaleManager[BundleKeys.ERR_INVALID_NUMBER]} (${LocaleManager[BundleKeys.FORM_COORD_X]})"
        if (x > 321) return LocaleManager[BundleKeys.ERR_COORD_X_MAX]
        coordYField.text.trim().toDoubleOrNull()
            ?: return "${LocaleManager[BundleKeys.ERR_INVALID_NUMBER]} (${LocaleManager[BundleKeys.FORM_COORD_Y]})"
        val price = priceField.text.trim().toLongOrNull()
            ?: return "${LocaleManager[BundleKeys.ERR_INVALID_NUMBER]} (${LocaleManager[BundleKeys.FORM_PRICE]})"
        if (price <= 0) return LocaleManager[BundleKeys.ERR_PRICE_POSITIVE]
        if (manufactureCostField.text.isNotBlank()) {
            manufactureCostField.text.trim().toLongOrNull()
                ?: return "${LocaleManager[BundleKeys.ERR_INVALID_NUMBER]} (${LocaleManager[BundleKeys.FORM_MANUFACTURE_COST]})"
        }
        if (ownerNameField.text.isBlank())
            return "${LocaleManager[BundleKeys.ERR_BLANK_FIELD]} (${LocaleManager[BundleKeys.FORM_OWNER_NAME]})"
        val height = ownerHeightField.text.trim().toLongOrNull()
            ?: return "${LocaleManager[BundleKeys.ERR_INVALID_NUMBER]} (${LocaleManager[BundleKeys.FORM_OWNER_HEIGHT]})"
        if (height <= 0) return LocaleManager[BundleKeys.ERR_HEIGHT_POSITIVE]
        return null
    }

    private fun buildResult(): Result {
        val product = Product(
            name = nameField.text.trim(),
            coordinates = Coordinates(
                x = coordXField.text.trim().toLong(),
                y = coordYField.text.trim().toDouble()
            ),
            price = priceField.text.trim().toLong(),
            partNumber = partNumberField.text.trim().takeIf { it.isNotBlank() },
            manufactureCost = manufactureCostField.text.trim().toLongOrNull(),
            unitOfMeasure = unitBox.value.takeIf { it != NONE }
                ?.let { UnitOfMeasure.valueOf(it) },
            owner = Person(
                name = ownerNameField.text.trim(),
                height = ownerHeightField.text.trim().toLong(),
                hairColor = HairColor.valueOf(hairColorBox.value),
                nationality = nationalityBox.value.takeIf { it != NONE }
                    ?.let { Country.valueOf(it) }
            )
        )
        val key = if (mode == Mode.INSERT || mode == Mode.REPLACE_IF_GREATER)
            keyField.text.trim() else prefill?.key ?: ""
        return Result(key, product)
    }

    private companion object {
        const val NONE = "(none)"
        const val ROW_LABEL_STYLE =
            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;"
    }
}