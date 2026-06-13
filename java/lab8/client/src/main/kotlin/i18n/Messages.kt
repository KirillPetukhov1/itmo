package i18n

import java.util.ListResourceBundle

/**
 * Default English resource bundle used as the ultimate fallback by [ResourceBundle.getBundle].
 * All keys from [BundleKeys] must be present here; locale-specific bundles override individual
 * entries without needing to repeat keys that are absent in their own list.
 */
class Messages : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> = arrayOf(
        arrayOf(BundleKeys.AUTH_WINDOW_TITLE, "Sign In"),
        arrayOf(BundleKeys.AUTH_LOGIN_LABEL, "Login"),
        arrayOf(BundleKeys.AUTH_PASSWORD_LABEL, "Password"),
        arrayOf(BundleKeys.AUTH_LOGIN_BUTTON, "Log In"),
        arrayOf(BundleKeys.AUTH_REGISTER_BUTTON, "Register"),
        arrayOf(BundleKeys.AUTH_PROMPT_LOGIN, "No account? Register"),
        arrayOf(BundleKeys.AUTH_PROMPT_REGISTER, "Already have an account? Log In"),
        arrayOf(BundleKeys.AUTH_ERROR_BLANK, "Login and password must not be empty"),
        arrayOf(BundleKeys.AUTH_ERROR_SERVER, "Server unavailable, please try again later"),

        arrayOf(BundleKeys.MAIN_WINDOW_TITLE, "Product Collection"),
        arrayOf(BundleKeys.MAIN_CURRENT_USER, "Logged in as"),
        arrayOf(BundleKeys.MAIN_LOGOUT, "Log Out"),
        arrayOf(BundleKeys.MAIN_LANGUAGE, "Language"),

        arrayOf(BundleKeys.TAB_TABLE, "Table"),
        arrayOf(BundleKeys.TAB_CANVAS, "Canvas"),

        arrayOf(BundleKeys.COL_KEY, "Key"),
        arrayOf(BundleKeys.COL_ID, "ID"),
        arrayOf(BundleKeys.COL_NAME, "Name"),
        arrayOf(BundleKeys.COL_COORD_X, "X"),
        arrayOf(BundleKeys.COL_COORD_Y, "Y"),
        arrayOf(BundleKeys.COL_CREATION_DATE, "Created"),
        arrayOf(BundleKeys.COL_PRICE, "Price"),
        arrayOf(BundleKeys.COL_PART_NUMBER, "Part Number"),
        arrayOf(BundleKeys.COL_MANUFACTURE_COST, "Mfr. Cost"),
        arrayOf(BundleKeys.COL_UNIT_OF_MEASURE, "Unit"),
        arrayOf(BundleKeys.COL_OWNER_NAME, "Owner"),
        arrayOf(BundleKeys.COL_OWNER_HEIGHT, "Height"),
        arrayOf(BundleKeys.COL_OWNER_HAIR_COLOR, "Hair Colour"),
        arrayOf(BundleKeys.COL_OWNER_NATIONALITY, "Nationality"),
        arrayOf(BundleKeys.COL_CREATOR, "Creator"),

        arrayOf(BundleKeys.BTN_INSERT, "Insert"),
        arrayOf(BundleKeys.BTN_UPDATE, "Update"),
        arrayOf(BundleKeys.BTN_REMOVE_KEY, "Remove by Key"),
        arrayOf(BundleKeys.BTN_CLEAR, "Clear Mine"),
        arrayOf(BundleKeys.BTN_REMOVE_LOWER, "Remove Lower"),
        arrayOf(BundleKeys.BTN_REPLACE_IF_GREATER, "Replace If Greater"),
        arrayOf(BundleKeys.BTN_REMOVE_GREATER_KEY, "Remove Greater Key"),
        arrayOf(BundleKeys.BTN_COUNT_GREATER_PRICE, "Count > Price"),
        arrayOf(BundleKeys.BTN_UNIQUE_UNIT, "Unique Units"),
        arrayOf(BundleKeys.BTN_PRICES_DESC, "Prices Descending"),
        arrayOf(BundleKeys.BTN_INFO, "Collection Info"),
        arrayOf(BundleKeys.BTN_EXECUTE_SCRIPT, "Execute Script"),
        arrayOf(BundleKeys.BTN_REFRESH, "Refresh"),
        arrayOf(BundleKeys.BTN_OK, "OK"),
        arrayOf(BundleKeys.BTN_CANCEL, "Cancel"),
        arrayOf(BundleKeys.BTN_SAVE, "Save"),
        arrayOf(BundleKeys.BTN_EDIT, "Edit"),
        arrayOf(BundleKeys.BTN_DELETE, "Delete"),

        arrayOf(BundleKeys.DLG_INSERT_TITLE, "Insert Product"),
        arrayOf(BundleKeys.DLG_UPDATE_TITLE, "Update Product"),
        arrayOf(BundleKeys.DLG_DELETE_TITLE, "Delete Product"),
        arrayOf(BundleKeys.DLG_DELETE_CONFIRM, "Are you sure you want to delete this product?"),
        arrayOf(BundleKeys.DLG_INFO_TITLE, "Collection Information"),
        arrayOf(BundleKeys.DLG_SCRIPT_TITLE, "Execute Script"),
        arrayOf(BundleKeys.DLG_SCRIPT_FILE_LABEL, "Script file path"),
        arrayOf(BundleKeys.DLG_OBJECT_INFO_TITLE, "Product Details"),

        arrayOf(BundleKeys.FORM_KEY, "Collection Key"),
        arrayOf(BundleKeys.FORM_NAME, "Product Name"),
        arrayOf(BundleKeys.FORM_COORD_X, "X Coordinate (max 321)"),
        arrayOf(BundleKeys.FORM_COORD_Y, "Y Coordinate"),
        arrayOf(BundleKeys.FORM_PRICE, "Price (> 0)"),
        arrayOf(BundleKeys.FORM_PART_NUMBER, "Part Number (optional)"),
        arrayOf(BundleKeys.FORM_MANUFACTURE_COST, "Manufacture Cost (optional)"),
        arrayOf(BundleKeys.FORM_UNIT_OF_MEASURE, "Unit of Measure (optional)"),
        arrayOf(BundleKeys.FORM_OWNER_NAME, "Owner Name"),
        arrayOf(BundleKeys.FORM_OWNER_HEIGHT, "Owner Height (> 0)"),
        arrayOf(BundleKeys.FORM_OWNER_HAIR_COLOR, "Owner Hair Colour"),
        arrayOf(BundleKeys.FORM_OWNER_NATIONALITY, "Owner Nationality (optional)"),
        arrayOf(BundleKeys.FORM_ID_LABEL, "Product ID"),

        arrayOf(BundleKeys.FILTER_PLACEHOLDER, "Filter..."),

        arrayOf(BundleKeys.MSG_INSERT_OK, "Product inserted successfully"),
        arrayOf(BundleKeys.MSG_UPDATE_OK, "Product updated successfully"),
        arrayOf(BundleKeys.MSG_REMOVE_OK, "Product removed successfully"),
        arrayOf(BundleKeys.MSG_CLEAR_OK, "Your products have been cleared"),
        arrayOf(BundleKeys.MSG_REGISTER_OK, "Registration successful. Please log in."),
        arrayOf(BundleKeys.MSG_LOGIN_OK, "You are now logged in"),
        arrayOf(BundleKeys.MSG_REMOVED_COUNT, "Removed {0} product(s)"),
        arrayOf(BundleKeys.MSG_COUNT_RESULT, "Found {0} product(s) with a higher price"),
        arrayOf(BundleKeys.MSG_REPLACE_REPLACED, "Product replaced successfully"),
        arrayOf(BundleKeys.MSG_REPLACE_NOT_REPLACED, "Product not replaced: new value is not greater"),

        arrayOf(BundleKeys.ERR_VALIDATION, "Validation Error"),
        arrayOf(BundleKeys.ERR_BLANK_FIELD, "This field must not be empty"),
        arrayOf(BundleKeys.ERR_INVALID_NUMBER, "Please enter a valid number"),
        arrayOf(BundleKeys.ERR_COORD_X_MAX, "X coordinate must not exceed 321"),
        arrayOf(BundleKeys.ERR_PRICE_POSITIVE, "Price must be greater than 0"),
        arrayOf(BundleKeys.ERR_HEIGHT_POSITIVE, "Height must be greater than 0"),
        arrayOf(BundleKeys.ERR_SERVER, "Server Error"),
        arrayOf(BundleKeys.ERR_NETWORK, "Network Error"),
        arrayOf(BundleKeys.ERR_NO_SELECTION, "Please select a product first"),
        arrayOf(BundleKeys.ERR_NOT_OWNER, "You can only modify your own products")
    )
}
