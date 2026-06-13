package i18n

import java.util.ListResourceBundle

/**
 * Albanian resource bundle. Locale tag: sq.
 */
class Messages_sq : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> = arrayOf(
        arrayOf(BundleKeys.AUTH_WINDOW_TITLE, "Hyrje"),
        arrayOf(BundleKeys.AUTH_LOGIN_LABEL, "Emri i p\u00ebrdoruesit"),
        arrayOf(BundleKeys.AUTH_PASSWORD_LABEL, "Fjal\u00ebkalimi"),
        arrayOf(BundleKeys.AUTH_LOGIN_BUTTON, "Hyr"),
        arrayOf(BundleKeys.AUTH_REGISTER_BUTTON, "Regjistrohu"),
        arrayOf(BundleKeys.AUTH_PROMPT_LOGIN, "Nuk keni llogari? Regjistrohu"),
        arrayOf(BundleKeys.AUTH_PROMPT_REGISTER, "Keni llogari? Hyr"),
        arrayOf(BundleKeys.AUTH_ERROR_BLANK, "Emri dhe fjal\u00ebkalimi nuk mund t\u00eb jen\u00eb bosh\u00eb"),
        arrayOf(BundleKeys.AUTH_ERROR_SERVER, "Serveri nuk \u00ebsht\u00eb i disponuesh\u00ebm, provoni p\u00ebrs\u00ebri m\u00eb von\u00eb"),

        arrayOf(BundleKeys.MAIN_WINDOW_TITLE, "Koleksioni i produkteve"),
        arrayOf(BundleKeys.MAIN_CURRENT_USER, "I identifikuar si"),
        arrayOf(BundleKeys.MAIN_LOGOUT, "Dil"),
        arrayOf(BundleKeys.MAIN_LANGUAGE, "Gjuha"),

        arrayOf(BundleKeys.TAB_TABLE, "Tabela"),
        arrayOf(BundleKeys.TAB_CANVAS, "Vizualizim"),

        arrayOf(BundleKeys.COL_KEY, "\u00c7elsi"),
        arrayOf(BundleKeys.COL_ID, "ID"),
        arrayOf(BundleKeys.COL_NAME, "Emri"),
        arrayOf(BundleKeys.COL_COORD_X, "X"),
        arrayOf(BundleKeys.COL_COORD_Y, "Y"),
        arrayOf(BundleKeys.COL_CREATION_DATE, "Data e krijimit"),
        arrayOf(BundleKeys.COL_PRICE, "\u00c7mimi"),
        arrayOf(BundleKeys.COL_PART_NUMBER, "Nr. i pjes\u00ebs"),
        arrayOf(BundleKeys.COL_MANUFACTURE_COST, "Kostoja e prodhimit"),
        arrayOf(BundleKeys.COL_UNIT_OF_MEASURE, "Njesia"),
        arrayOf(BundleKeys.COL_OWNER_NAME, "Pronari"),
        arrayOf(BundleKeys.COL_OWNER_HEIGHT, "Gjatesia"),
        arrayOf(BundleKeys.COL_OWNER_HAIR_COLOR, "Ngjyra e fl\u00f6keve"),
        arrayOf(BundleKeys.COL_OWNER_NATIONALITY, "Shtetesia"),
        arrayOf(BundleKeys.COL_CREATOR, "Krijuesi"),

        arrayOf(BundleKeys.BTN_INSERT, "Shto"),
        arrayOf(BundleKeys.BTN_UPDATE, "P\u00ebrditeso"),
        arrayOf(BundleKeys.BTN_REMOVE_KEY, "Hiq sipas \u00e7elsit"),
        arrayOf(BundleKeys.BTN_CLEAR, "Pastro t\u00eb miat"),
        arrayOf(BundleKeys.BTN_REMOVE_LOWER, "Hiq m\u00eb t\u00eb vog\u00eblat"),
        arrayOf(BundleKeys.BTN_REPLACE_IF_GREATER, "Z\u00ebrro n\u00ebse m\u00eb i madh"),
        arrayOf(BundleKeys.BTN_REMOVE_GREATER_KEY, "Hiq \u00e7els\u00ebn m\u00eb t\u00eb madh"),
        arrayOf(BundleKeys.BTN_COUNT_GREATER_PRICE, "Numro > \u00e7mim"),
        arrayOf(BundleKeys.BTN_UNIQUE_UNIT, "Njesi unike"),
        arrayOf(BundleKeys.BTN_PRICES_DESC, "\u00c7mimet z\u00ebrit\u00ebs"),
        arrayOf(BundleKeys.BTN_INFO, "Info p\u00ebr koleksionin"),
        arrayOf(BundleKeys.BTN_EXECUTE_SCRIPT, "Ekzekuto skriptin"),
        arrayOf(BundleKeys.BTN_REFRESH, "Rir\u00ebnkoso"),
        arrayOf(BundleKeys.BTN_OK, "OK"),
        arrayOf(BundleKeys.BTN_CANCEL, "Anulo"),
        arrayOf(BundleKeys.BTN_SAVE, "Ruaj"),
        arrayOf(BundleKeys.BTN_EDIT, "Modifiko"),
        arrayOf(BundleKeys.BTN_DELETE, "Fshi"),

        arrayOf(BundleKeys.DLG_INSERT_TITLE, "Shto produkt"),
        arrayOf(BundleKeys.DLG_UPDATE_TITLE, "P\u00ebrditeso produktin"),
        arrayOf(BundleKeys.DLG_DELETE_TITLE, "Fshi produktin"),
        arrayOf(BundleKeys.DLG_DELETE_CONFIRM, "Jeni i sigurt q\u00eb doni t\u00eb fshini k\u00ebt\u00eb produkt?"),
        arrayOf(BundleKeys.DLG_INFO_TITLE, "Informacion p\u00ebr koleksionin"),
        arrayOf(BundleKeys.DLG_SCRIPT_TITLE, "Ekzekuto skriptin"),
        arrayOf(BundleKeys.DLG_SCRIPT_FILE_LABEL, "Rruga e skedarit t\u00eb skriptit"),
        arrayOf(BundleKeys.DLG_OBJECT_INFO_TITLE, "Detajet e produktit"),

        arrayOf(BundleKeys.FORM_KEY, "\u00c7elsi i koleksionit"),
        arrayOf(BundleKeys.FORM_NAME, "Emri i produktit"),
        arrayOf(BundleKeys.FORM_COORD_X, "Koordinata X (maks 321)"),
        arrayOf(BundleKeys.FORM_COORD_Y, "Koordinata Y"),
        arrayOf(BundleKeys.FORM_PRICE, "\u00c7mimi (> 0)"),
        arrayOf(BundleKeys.FORM_PART_NUMBER, "Nr. i pjes\u00ebs (opsional)"),
        arrayOf(BundleKeys.FORM_MANUFACTURE_COST, "Kostoja e prodhimit (opsional)"),
        arrayOf(BundleKeys.FORM_UNIT_OF_MEASURE, "Njesia e mas\u00ebs (opsionale)"),
        arrayOf(BundleKeys.FORM_OWNER_NAME, "Emri i pronarit"),
        arrayOf(BundleKeys.FORM_OWNER_HEIGHT, "Gjatesia e pronarit (> 0)"),
        arrayOf(BundleKeys.FORM_OWNER_HAIR_COLOR, "Ngjyra e fl\u00f6keve t\u00eb pronarit"),
        arrayOf(BundleKeys.FORM_OWNER_NATIONALITY, "Shtetesia e pronarit (opsionale)"),
        arrayOf(BundleKeys.FORM_ID_LABEL, "ID i produktit"),

        arrayOf(BundleKeys.FILTER_PLACEHOLDER, "Filtro\u2026"),

        arrayOf(BundleKeys.MSG_INSERT_OK, "Produkti u shtua me sukses"),
        arrayOf(BundleKeys.MSG_UPDATE_OK, "Produkti u p\u00ebrdit\u00ebsua me sukses"),
        arrayOf(BundleKeys.MSG_REMOVE_OK, "Produkti u fshi me sukses"),
        arrayOf(BundleKeys.MSG_CLEAR_OK, "Produktet tuaja u fshin"),
        arrayOf(BundleKeys.MSG_REGISTER_OK, "Regjistrimi u krye me sukses. Ju lutem hyrni."),
        arrayOf(BundleKeys.MSG_LOGIN_OK, "Hyrja u krye me sukses"),
        arrayOf(BundleKeys.MSG_REMOVED_COUNT, "U fshin {0} produkt(e)"),
        arrayOf(BundleKeys.MSG_COUNT_RESULT, "U gjet\u00ebn {0} produkt(e) me \u00e7mim m\u00eb t\u00eb lart\u00eb"),
        arrayOf(BundleKeys.MSG_REPLACE_REPLACED, "Produkti u z\u00ebrvendes\u00eba me sukses"),
        arrayOf(BundleKeys.MSG_REPLACE_NOT_REPLACED, "Produkti nuk u z\u00ebrvendes\u00eba: vlera e re nuk \u00ebsht\u00eb m\u00eb e madhe"),

        arrayOf(BundleKeys.ERR_VALIDATION, "Gabim validimi"),
        arrayOf(BundleKeys.ERR_BLANK_FIELD, "Ky fush\u00eb nuk mund t\u00eb jet\u00eb bosh\u00eb"),
        arrayOf(BundleKeys.ERR_INVALID_NUMBER, "Ju lutem vendosni nj\u00eb num\u00ebr t\u00eb vlefsh\u00ebm"),
        arrayOf(BundleKeys.ERR_COORD_X_MAX, "Koordinata X nuk duhet t\u00eb kaloj\u00eb 321"),
        arrayOf(BundleKeys.ERR_PRICE_POSITIVE, "\u00c7mimi duhet t\u00eb jet\u00eb m\u00eb i madh se 0"),
        arrayOf(BundleKeys.ERR_HEIGHT_POSITIVE, "Gjatesia duhet t\u00eb jet\u00eb m\u00eb e madhe se 0"),
        arrayOf(BundleKeys.ERR_SERVER, "Gabim serveri"),
        arrayOf(BundleKeys.ERR_NETWORK, "Gabim rrjeti"),
        arrayOf(BundleKeys.ERR_NO_SELECTION, "Ju lutem zgjidhni s\u00ebpari nj\u00eb produkt"),
        arrayOf(BundleKeys.ERR_NOT_OWNER, "Mund t\u00eb modifikoni vet\u00ebm produktet tuaja")
    )
}
