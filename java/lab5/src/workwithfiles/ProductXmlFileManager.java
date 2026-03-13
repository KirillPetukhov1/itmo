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

public class ProductXmlFileManager<K extends Comparable<K>, T extends Product>
        extends AbstractFileManager<Hashtable<K, T>> {
    private LongIdManager<T> idManager;

    public ProductXmlFileManager(String fileName) {
        super(fileName);
        this.idManager = new LongIdManager<>();
    }

    public ProductXmlFileManager(String fileName, LongIdManager<T> idManager) {
        super(fileName);
        this.idManager = idManager;
    }

    public LongIdManager<T> getIdManager() {
        return idManager;
    }

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
