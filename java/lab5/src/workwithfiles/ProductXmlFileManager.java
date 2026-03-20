package src.workwithfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import src.baseabstractions.AbstractFileManager;
import src.baseobjects.Coordinates;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.productcreation.LongIdManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * XML file manager for saving and loading Product collections using XStream
 * library.
 * Handles serialization/deserialization of Hashtable collections with Product
 * objects.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <T> the type of values stored in the collection, must extend Product
 */
public class ProductXmlFileManager<K extends Comparable<K>, T extends Product>
        extends AbstractFileManager<Hashtable<K, T>> {
    private LongIdManager<T> idManager;

    /**
     * Constructs a ProductXmlFileManager with the specified file name.
     * Creates a new LongIdManager instance for ID management.
     *
     * @param fileName the name of the XML file to manage
     */
    public ProductXmlFileManager(String fileName) {
        super(fileName);
        this.idManager = new LongIdManager<>();
    }

    /**
     * Constructs a ProductXmlFileManager with the specified file name and ID
     * manager.
     *
     * @param fileName  the name of the XML file to manage
     * @param idManager the ID manager to use for tracking product IDs
     */
    public ProductXmlFileManager(String fileName, LongIdManager<T> idManager) {
        super(fileName);
        this.idManager = idManager;
    }

    /**
     * Returns the ID manager used for tracking product IDs.
     *
     * @return the LongIdManager instance
     */
    public LongIdManager<T> getIdManager() {
        return idManager;
    }

    /**
     * Saves the products Hashtable to an XML file.
     * Uses XStream for XML serialization with UTF-8 encoding.
     *
     * @param products the Hashtable of products to save
     * @throws Exception if an error occurs during save operation
     */
    @Override
    public void save(Hashtable<K, T> products) throws Exception {
        XStream xstream = new XStream();
        xstream.allowTypesByWildcard(new String[] { "src.baseobjects.**", "src.baseabstractions.**" });
        xstream.alias("product", Product.class);
        xstream.alias("coordinates", Coordinates.class);
        xstream.alias("person", Person.class);

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(getFileName()), "UTF-8")) {
            xstream.toXML(products, writer);
        } catch (IOException e) {
            if (!(new File(getFileName())).canWrite()) {
                System.out.println("Нет прав на запись в данный файл.");
            } else {
                System.out.println("Что-то пошло не так. Данные не сохранены. ");
            }
        }
    }

    /**
     * Loads the products Hashtable from an XML file.
     * Uses XStream for XML deserialization and populates the ID manager with loaded
     * IDs.
     *
     * @return the loaded Hashtable of products, or an empty Hashtable if loading
     *         fails
     * @throws Exception if an error occurs during load operation
     */
    @Override
    public Hashtable<K, T> load() throws Exception {
        File file = new File(getFileName());

        if (!file.exists()) {
            return new Hashtable<>();
        }

        XStream xstream = new XStream();
        xstream.allowTypesByWildcard(new String[] { "src.baseobjects.**", "src.baseabstractions.**" });
        xstream.alias("product", Product.class);
        xstream.alias("coordinates", Coordinates.class);
        xstream.alias("person", Person.class);

        Hashtable<K, T> loadedProducts;
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file), "UTF-8")) {
            loadedProducts = (Hashtable<K, T>) xstream.fromXML(reader);
        } catch (NullPointerException | XStreamException e) {
            System.out.println("Что-то не так с файлом или он пуст. Коллекция не содержит элементов. ");
            return new Hashtable<>();
        } catch (FileNotFoundException e) {
            if (!(new File(getFileName()).canRead())) {
                System.out.println("Нет прав на чтение данного файла.");
            } else {
                System.out.println("Данный файл не найден.");
            }
            return new Hashtable<>();
        }

        if (loadedProducts != null) {
            for (var product : loadedProducts.values()) {
                idManager.addId(product);
            }
        } else {
            loadedProducts = new Hashtable<>();
        }

        return loadedProducts;
    }
}