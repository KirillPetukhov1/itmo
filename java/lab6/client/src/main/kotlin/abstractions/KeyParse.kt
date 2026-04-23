package abstractions

interface KeyParse<K> {
    fun getParsedObject(value: String): K
}