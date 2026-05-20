package abstractions

/**
 * Parses a raw string token into a typed collection key.
 *
 * @param K the key type
 */
interface KeyParse<K> {

    /**
     * Converts [value] into a key of type [K].
     *
     * @param value the string to parse
     * @return the parsed key
     * @throws IllegalArgumentException if [value] cannot be converted
     */
    fun parse(value: String): K
}
