package i18n

import java.util.ListResourceBundle

/**
 * Slovak resource bundle. Locale tag: sk.
 */
class Messages_sk : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> = arrayOf(
        arrayOf(BundleKeys.AUTH_WINDOW_TITLE, "Prihl\u00e1senie"),
        arrayOf(BundleKeys.AUTH_LOGIN_LABEL, "Prihlasovacie meno"),
        arrayOf(BundleKeys.AUTH_PASSWORD_LABEL, "Heslo"),
        arrayOf(BundleKeys.AUTH_LOGIN_BUTTON, "Prihl\u00e1si\u0165 sa"),
        arrayOf(BundleKeys.AUTH_REGISTER_BUTTON, "Registrova\u0165 sa"),
        arrayOf(BundleKeys.AUTH_PROMPT_LOGIN, "Nem\u00e1te \u00fa\u010det? Registrova\u0165 sa"),
        arrayOf(BundleKeys.AUTH_PROMPT_REGISTER, "U\u017e m\u00e1te \u00fa\u010det? Prihl\u00e1si\u0165 sa"),
        arrayOf(BundleKeys.AUTH_ERROR_BLANK, "Meno a heslo nesmú by\u0165 pr\u00e1zdne"),
        arrayOf(BundleKeys.AUTH_ERROR_SERVER, "Server nie je dostupn\u00fd, sk\u00faste nesk\u00f4r"),

        arrayOf(BundleKeys.MAIN_WINDOW_TITLE, "Kol\u00e9kcia produktov"),
        arrayOf(BundleKeys.MAIN_CURRENT_USER, "Prihl\u00e1sen\u00fd ako"),
        arrayOf(BundleKeys.MAIN_LOGOUT, "Odhl\u00e1si\u0165 sa"),
        arrayOf(BundleKeys.MAIN_LANGUAGE, "Jazyk"),

        arrayOf(BundleKeys.TAB_TABLE, "Tabu\u013eka"),
        arrayOf(BundleKeys.TAB_CANVAS, "Vizualiz\u00e1cia"),

        arrayOf(BundleKeys.COL_KEY, "K\u013e\u00fa\u010d"),
        arrayOf(BundleKeys.COL_ID, "ID"),
        arrayOf(BundleKeys.COL_NAME, "N\u00e1zov"),
        arrayOf(BundleKeys.COL_COORD_X, "X"),
        arrayOf(BundleKeys.COL_COORD_Y, "Y"),
        arrayOf(BundleKeys.COL_CREATION_DATE, "D\u00e1tum vytvorenia"),
        arrayOf(BundleKeys.COL_PRICE, "Cena"),
        arrayOf(BundleKeys.COL_PART_NUMBER, "\u010c\u00edslo dielu"),
        arrayOf(BundleKeys.COL_MANUFACTURE_COST, "N\u00e1klady v\u00fdroby"),
        arrayOf(BundleKeys.COL_UNIT_OF_MEASURE, "Jednotka"),
        arrayOf(BundleKeys.COL_OWNER_NAME, "Vlastn\u00edk"),
        arrayOf(BundleKeys.COL_OWNER_HEIGHT, "V\u00fd\u0161ka"),
        arrayOf(BundleKeys.COL_OWNER_HAIR_COLOR, "Farba vlas\u00f3v"),
        arrayOf(BundleKeys.COL_OWNER_NATIONALITY, "N\u00e1rodnost\u0165"),
        arrayOf(BundleKeys.COL_CREATOR, "Autor"),

        arrayOf(BundleKeys.BTN_INSERT, "Vlo\u017ei\u0165"),
        arrayOf(BundleKeys.BTN_UPDATE, "Aktualizova\u0165"),
        arrayOf(BundleKeys.BTN_REMOVE_KEY, "Odstr\u00e1ni\u0165 pod\u013ea k\u013e\u00fa\u010da"),
        arrayOf(BundleKeys.BTN_CLEAR, "Vymaza\u0165 moje"),
        arrayOf(BundleKeys.BTN_REMOVE_LOWER, "Odstr\u00e1ni\u0165 men\u0161ie"),
        arrayOf(BundleKeys.BTN_REPLACE_IF_GREATER, "Nahradi\u0165, ak v\u00e4\u010d\u0161ie"),
        arrayOf(BundleKeys.BTN_REMOVE_GREATER_KEY, "Odstr\u00e1ni\u0165 v\u00e4\u010d\u0161\u00ed k\u013e\u00fa\u010d"),
        arrayOf(BundleKeys.BTN_COUNT_GREATER_PRICE, "Po\u010det > cena"),
        arrayOf(BundleKeys.BTN_UNIQUE_UNIT, "Unik\u00e1tne jednotky"),
        arrayOf(BundleKeys.BTN_PRICES_DESC, "Ceny zostupne"),
        arrayOf(BundleKeys.BTN_INFO, "Info o kolekcii"),
        arrayOf(BundleKeys.BTN_EXECUTE_SCRIPT, "Spusti\u0165 skript"),
        arrayOf(BundleKeys.BTN_REFRESH, "Obnovi\u0165"),
        arrayOf(BundleKeys.BTN_OK, "OK"),
        arrayOf(BundleKeys.BTN_CANCEL, "Zru\u0161i\u0165"),
        arrayOf(BundleKeys.BTN_SAVE, "Ulo\u017ei\u0165"),
        arrayOf(BundleKeys.BTN_EDIT, "Upravi\u0165"),
        arrayOf(BundleKeys.BTN_DELETE, "Odstr\u00e1ni\u0165"),

        arrayOf(BundleKeys.DLG_INSERT_TITLE, "Vlo\u017ei\u0165 produkt"),
        arrayOf(BundleKeys.DLG_UPDATE_TITLE, "Aktualizova\u0165 produkt"),
        arrayOf(BundleKeys.DLG_DELETE_TITLE, "Odstr\u00e1ni\u0165 produkt"),
        arrayOf(BundleKeys.DLG_DELETE_CONFIRM, "Naozaj chcete odstr\u00e1ni\u0165 tento produkt?"),
        arrayOf(BundleKeys.DLG_INFO_TITLE, "Inform\u00e1cie o kolekcii"),
        arrayOf(BundleKeys.DLG_SCRIPT_TITLE, "Spusti\u0165 skript"),
        arrayOf(BundleKeys.DLG_SCRIPT_FILE_LABEL, "Cesta k s\u00faboru skriptu"),
        arrayOf(BundleKeys.DLG_OBJECT_INFO_TITLE, "Detaily produktu"),

        arrayOf(BundleKeys.FORM_KEY, "K\u013e\u00fa\u010d kolekcie"),
        arrayOf(BundleKeys.FORM_NAME, "N\u00e1zov produktu"),
        arrayOf(BundleKeys.FORM_COORD_X, "Sú\u0159adnica X (max 321)"),
        arrayOf(BundleKeys.FORM_COORD_Y, "Sú\u0159adnica Y"),
        arrayOf(BundleKeys.FORM_PRICE, "Cena (> 0)"),
        arrayOf(BundleKeys.FORM_PART_NUMBER, "\u010c\u00edslo dielu (voliteln\u00e9)"),
        arrayOf(BundleKeys.FORM_MANUFACTURE_COST, "N\u00e1klady v\u00fdroby (voliteln\u00e9)"),
        arrayOf(BundleKeys.FORM_UNIT_OF_MEASURE, "Jednotka miery (voliteln\u00e9)"),
        arrayOf(BundleKeys.FORM_OWNER_NAME, "Meno vlastn\u00edka"),
        arrayOf(BundleKeys.FORM_OWNER_HEIGHT, "V\u00fd\u0161ka vlastn\u00edka (> 0)"),
        arrayOf(BundleKeys.FORM_OWNER_HAIR_COLOR, "Farba vlas\u00f3v vlastn\u00edka"),
        arrayOf(BundleKeys.FORM_OWNER_NATIONALITY, "N\u00e1rodnost\u0165 vlastn\u00edka (voliteln\u00e9)"),
        arrayOf(BundleKeys.FORM_ID_LABEL, "ID produktu"),

        arrayOf(BundleKeys.FILTER_PLACEHOLDER, "Filtrova\u0165\u2026"),

        arrayOf(BundleKeys.MSG_INSERT_OK, "Produkt bol \u00faspe\u0161ne vlo\u017een\u00fd"),
        arrayOf(BundleKeys.MSG_UPDATE_OK, "Produkt bol \u00faspe\u0161ne aktualizovan\u00fd"),
        arrayOf(BundleKeys.MSG_REMOVE_OK, "Produkt bol \u00faspe\u0161ne odstr\u00e1nen\u00fd"),
        arrayOf(BundleKeys.MSG_CLEAR_OK, "Va\u0161e produkty boli vymazan\u00e9"),
        arrayOf(BundleKeys.MSG_REGISTER_OK, "Registr\u00e1cia \u00faspe\u0161n\u00e1. Prihl\u00e1ste sa."),
        arrayOf(BundleKeys.MSG_LOGIN_OK, "Prihl\u00e1senie \u00faspe\u0161n\u00e9"),
        arrayOf(BundleKeys.MSG_REMOVED_COUNT, "Odstr\u00e1nen\u00fdch {0} produkt(ov)"),
        arrayOf(BundleKeys.MSG_COUNT_RESULT, "N\u00e1jden\u00fdch {0} produkt(ov) s vy\u0161\u0161ou cenou"),
        arrayOf(BundleKeys.MSG_REPLACE_REPLACED, "Produkt bol nahraден\u00fd"),
        arrayOf(BundleKeys.MSG_REPLACE_NOT_REPLACED, "Produkt nebol nahraден\u00fd: nov\u00e1 hodnota nie je v\u00e4\u010d\u0161ia"),

        arrayOf(BundleKeys.ERR_VALIDATION, "Chyba overenia"),
        arrayOf(BundleKeys.ERR_BLANK_FIELD, "Toto pole nesmie by\u0165 pr\u00e1zdne"),
        arrayOf(BundleKeys.ERR_INVALID_NUMBER, "Zadajte platn\u00e9 \u010d\u00edslo"),
        arrayOf(BundleKeys.ERR_COORD_X_MAX, "Sú\u0159adnica X nesmie presiahnu\u0165 321"),
        arrayOf(BundleKeys.ERR_PRICE_POSITIVE, "Cena mus\u00ed by\u0165 v\u00e4\u010d\u0161ia ako 0"),
        arrayOf(BundleKeys.ERR_HEIGHT_POSITIVE, "V\u00fd\u0161ka mus\u00ed by\u0165 v\u00e4\u010d\u0161ia ako 0"),
        arrayOf(BundleKeys.ERR_SERVER, "Chyba servera"),
        arrayOf(BundleKeys.ERR_NETWORK, "Chyba siete"),
        arrayOf(BundleKeys.ERR_NO_SELECTION, "Najprv vyberte produkt"),
        arrayOf(BundleKeys.ERR_NOT_OWNER, "M\u00f4\u017eete upravova\u0165 iba vlastn\u00e9 produkty")
    )
}
