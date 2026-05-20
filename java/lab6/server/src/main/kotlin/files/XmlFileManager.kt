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
 * If the file does not exist when [load] is called, it is created immediately
 * with an empty collection so subsequent saves always have a writable target.
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
     * Creates the file and any missing parent directories if they do not exist.
     *
     * @param products the collection to persist
     * @throws IllegalStateException if the file cannot be written
     */
    fun save(products: Hashtable<String, Product>) {
        val file = File(filePath)
        file.parentFile?.mkdirs()
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!file.canWrite()) {
            throw IllegalStateException("No write permission for file: $filePath")
        }
        OutputStreamWriter(FileOutputStream(file), Charsets.UTF_8).use { writer ->
            buildXStream().toXML(products, writer)
        }
    }

    /**
     * Loads the collection from the XML file.
     * If the file does not exist, it is created with an empty collection and an empty
     * [Hashtable] is returned. If the file exists but contains invalid XML, an empty
     * [Hashtable] is returned without modifying the file.
     *
     * @return the loaded collection, or an empty [Hashtable] when the file is absent or unreadable
     * @throws IllegalStateException if the file exists but cannot be read
     */
    @Suppress("UNCHECKED_CAST")
    fun load(): Hashtable<String, Product> {
        val file = File(filePath)
        if (!file.exists()) {
            save(Hashtable())
            return Hashtable()
        }
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
