package i18n

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import java.text.MessageFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.ResourceBundle

/**
 * Singleton that manages the active UI locale and provides access to localised strings,
 * number formatting, and date/time formatting.
 *
 * The four supported locales are Russian, Slovak, Albanian, and English (Canada).
 * Switching [locale] at runtime causes [localeProperty] to fire a change event; views
 * subscribe to this property and call their own text-refresh method in the listener.
 * No application restart is required.
 *
 * Localised strings are stored in [ListResourceBundle] subclasses in the same package:
 * [Messages] (fallback), [Messages_ru], [Messages_sk], [Messages_sq], [Messages_en_CA].
 * The resource bundle is resolved via [ResourceBundle.getBundle] using the base name
 * "i18n.Messages" and the current locale.
 *
 * Number and date/time values must be formatted through [formatLong], [formatDouble], and
 * [formatDateTime] to ensure correct decimal separators, digit grouping, and date patterns
 * for the active locale.
 */
object LocaleManager {

    private const val BUNDLE_BASE = "i18n.Messages"

    /** Russian locale. */
    val RUSSIAN: Locale = Locale("ru")

    /** Slovak locale. */
    val SLOVAK: Locale = Locale("sk")

    /** Albanian locale. */
    val ALBANIAN: Locale = Locale("sq")

    /** English (Canada) locale. */
    val ENGLISH_CANADA: Locale = Locale.CANADA

    /**
     * All locales available in the language selector, in display order.
     */
    val supportedLocales: List<Locale> = listOf(RUSSIAN, SLOVAK, ALBANIAN, ENGLISH_CANADA)

    /**
     * Display names shown in the language-selector control, keyed by locale.
     */
    val localeDisplayNames: Map<Locale, String> = mapOf(
        RUSSIAN to "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
        SLOVAK to "Sloven\u010dina",
        ALBANIAN to "Shqip",
        ENGLISH_CANADA to "English (Canada)"
    )

    /**
     * Observable property holding the currently active locale.
     * Views bind a listener to this property and refresh all text nodes when it fires.
     */
    val localeProperty: ObjectProperty<Locale> = SimpleObjectProperty(ENGLISH_CANADA)

    /**
     * The currently active locale.
     * Setting this value updates [localeProperty] and triggers all registered listeners.
     */
    var locale: Locale
        get() = localeProperty.get()
        set(value) = localeProperty.set(value)

    /**
     * Returns the localised string for [key] from the bundle for the current locale.
     * If the key is absent in the locale-specific bundle, the fallback bundle is used.
     *
     * @param key a constant from [BundleKeys]
     * @return the translated string
     * @throws java.util.MissingResourceException if the key is absent from all bundles
     */
    operator fun get(key: String): String =
        ResourceBundle.getBundle(BUNDLE_BASE, locale).getString(key)

    /**
     * Returns the localised string for [key] with [args] substituted using [MessageFormat].
     * Argument placeholders in bundle values use the syntax {0}, {1}, etc.
     *
     * @param key a constant from [BundleKeys]
     * @param args values to substitute at {0}, {1}, ... positions
     * @return the formatted string
     */
    fun get(key: String, vararg args: Any): String =
        MessageFormat.format(get(key), *args)

    /**
     * Formats [value] as an integer according to the current locale's number format.
     * Uses the locale's digit grouping separator (e.g. space in Russian, comma in English).
     *
     * @param value the long value to format
     * @return the locale-formatted string
     */
    fun formatLong(value: Long): String =
        NumberFormat.getIntegerInstance(locale).format(value)

    /**
     * Formats [value] as a decimal number according to the current locale.
     * Uses the locale's decimal separator (comma in Russian/Slovak/Albanian, period in English).
     *
     * @param value the double value to format
     * @return the locale-formatted string
     */
    fun formatDouble(value: Double): String =
        NumberFormat.getNumberInstance(locale).format(value)

    /**
     * Parses [isoString] as an ISO-8601 [LocalDateTime] and formats it according to the
     * current locale using [FormatStyle.MEDIUM] for both date and time parts.
     * Returns [isoString] unchanged when it is blank or unparseable.
     *
     * @param isoString ISO-8601 date-time string produced by the server
     * @return locale-formatted date-time string, or [isoString] on parse failure
     */
    fun formatDateTime(isoString: String): String {
        if (isoString.isBlank()) return ""
        return try {
            val dt = LocalDateTime.parse(isoString)
            val formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale)
            dt.format(formatter)
        } catch (_: Exception) {
            isoString
        }
    }
}
