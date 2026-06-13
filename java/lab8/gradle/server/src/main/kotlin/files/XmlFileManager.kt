package files

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.XStreamException
import objects.Coordinates
import objects.Person
import objects.Product
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Hashtable

/**
 * Persists and restores the product collection to and from an XML file.
 * Read operations use [InputStreamReader], write operations use [OutputStreamWriter],
 * both with UTF-8 encoding, as required by the specification.
 *
 * @property filePath absolute or relative path to the XML file
 */
class XmlFileManager(private val filePath: String) {

    private fun buildXStream(): XStream = XStream().apply {
        allowTypesByWildcard(arrayOf("objects.**"))
        alias("product", Product::class.java)
        alias("coordinates", Coordinates::class.java)
        alias("person", Person::class.java)
    }

    /**
     * Saves [products] to the XML file, overwriting any previous content.
     *
     * @param products the collection to persist
     * @throws IllegalStateException if the file cannot be written
     */
    fun save(products: Hashtable<String, Product>) {
        val file = File(filePath)
        if (file.exists() && !file.canWrite()) {
            throw IllegalStateException("No write permission for file: $filePath")
        }
        OutputStreamWriter(FileOutputStream(file), Charsets.UTF_8).use { writer ->
            buildXStream().toXML(products, writer)
        }
    }

    /**
     * Loads the collection from the XML file.
     * Returns an empty [Hashtable] when the file does not exist or contains invalid XML.
     *
     * @return the loaded collection, or an empty [Hashtable] on any recoverable error
     * @throws IllegalStateException if the file exists but cannot be read
     */
    @Suppress("UNCHECKED_CAST")
    fun load(): Hashtable<String, Product> {
        val file = File(filePath)
        if (!file.exists()) return Hashtable()
        if (!file.canRead()) {
            throw IllegalStateException("No read permission for file: $filePath")
        }
        return try {
            InputStreamReader(FileInputStream(file), Charsets.UTF_8).use { reader ->
                (buildXStream().fromXML(reader) as? Hashtable<String, Product>) ?: Hashtable()
            }
        } catch (e: XStreamException) {
            Hashtable()
        } catch (e: NullPointerException) {
            Hashtable()
        }
    }
}
