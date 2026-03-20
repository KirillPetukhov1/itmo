package src.basecollection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Hashtable;

import src.baseabstractions.ProductClientManager;
import src.baseobjects.Product;
import src.baseobjects.UnitOfMeasure;
import src.console.CommandManager;
import src.productcreation.LongIdManager;
import src.productcreation.ProductBuilder;
import src.baseabstractions.AbstractFileManager;
import src.baseabstractions.ParseKey;

/**
 * Manages a collection of products stored in a Hashtable.
 * Provides operations for manipulating and querying the collection.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <T> the type of products stored in the collection, must extend Product
 */
public class CollectionManager<K extends Comparable<K>, T extends Product> {
    Hashtable<K, T> products = new Hashtable<>();
    private LocalDateTime localDateTime = LocalDateTime.now();
    private ProductClientManager<T> productClientManager;
    private ParseKey<K> keyParser;
    private LongIdManager<T> idManager;
    private AbstractFileManager<Hashtable<K, T>> fileManager;

    /**
     * Sets the products hashtable.
     *
     * @param products the new products hashtable
     */
    public void setProducts(Hashtable<K, T> products) {
        this.products = products;
    }

    /**
     * Returns the products hashtable.
     *
     * @return the current products hashtable
     */
    public Hashtable<K, T> getProducts() {
        return products;
    }

    /**
     * Returns the collection initialization date and time.
     *
     * @return the LocalDateTime of initialization
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Sets the product client manager.
     *
     * @param productClientManager the product client manager to use
     */
    public void setProductClientManager(ProductClientManager<T> productClientManager) {
        this.productClientManager = productClientManager;
    }

    /**
     * Returns the product client manager.
     *
     * @return the current product client manager
     */
    public ProductClientManager<T> getProductClientManager() {
        return productClientManager;
    }

    /**
     * Sets the key parser for parsing string keys.
     *
     * @param keyParser the key parser to use
     */
    public void setKeyParser(ParseKey<K> keyParser) {
        this.keyParser = keyParser;
    }

    /**
     * Returns the key parser.
     *
     * @return the current key parser
     */
    public ParseKey<K> getKeyParser() {
        return keyParser;
    }

    /**
     * Sets the ID manager for managing product IDs.
     *
     * @param idManager the ID manager to use
     */
    public void setIdManager(LongIdManager<T> idManager) {
        this.idManager = idManager;
    }

    /**
     * Returns the ID manager.
     *
     * @return the current ID manager
     */
    public LongIdManager<T> getIdManager() {
        return idManager;
    }

    /**
     * Sets the file manager for saving/loading the collection.
     *
     * @param fileManager the file manager to use
     */
    public void setFileManager(AbstractFileManager<Hashtable<K, T>> fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Returns the file manager.
     *
     * @return the current file manager
     */
    public AbstractFileManager<Hashtable<K, T>> getFileManager() {
        return fileManager;
    }

    /**
     * Displays help information for all available commands.
     */
    public void help() {
        CommandManager.getCommands().values().forEach(command -> System.out.println(command.getDescription()));
    }

    /**
     * Displays information about the collection (type, initialization date, size).
     */
    public void info() {
        String info = "Тип коллекции: " + products.getClass().getSimpleName() + "\nДата инициализации: "
                + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\nКоличество элементов: "
                + products.size();
        System.out.println(info);
    }

    /**
     * Displays all elements in the collection.
     */
    public void show() {
        if (!products.isEmpty()) {
            for (K product_k : products.keySet()) {
                System.out.println(product_k + ": " + products.get(product_k).toString());
            }
        } else {
            System.out.println("Коллекция не содержит элементов.");
        }
    }

    /**
     * Inserts a new product with the specified key using default product client
     * manager.
     *
     * @param key the key for the new product
     */
    public void insert(K key) {
        insert(key, productClientManager);
    }

    /**
     * Inserts a new product with the specified key using the given product client
     * manager.
     *
     * @param key                  the key for the new product
     * @param productClientManager the product client manager to use
     */
    public void insert(K key, ProductClientManager<T> productClientManager) {
        long oldProductId = 0;
        if (products.containsKey(key)) {
            System.out.println("Коллекция уже содержит элемент с таким ключом. Будет произведена перезапись.");
            oldProductId = products.get(key).getId();
        }
        T product = productClientManager.getProduct();
        products.put(key, product);
        System.out.println("Коллекция успешно обновлена.");
        if (oldProductId != 0) {
            idManager.removeId(oldProductId);
        }
    }

    /**
     * Updates a product with the specified ID using default product client manager.
     *
     * @param id the ID of the product to update
     */
    public void update(long id) {
        update(id, productClientManager);
    }

    /**
     * Updates a product with the specified ID using the given product client
     * manager.
     *
     * @param id                   the ID of the product to update
     * @param productClientManager the product client manager to use
     */
    public void update(long id, ProductClientManager<T> productClientManager) {
        if (!idManager.isIdExists(id)) {
            System.out.println("Коллекция не содержит элемент с таким индексом.");
            return;
        }

        K currentKey = null;

        for (var key : products.keySet()) {
            if (products.get(key).getId() == id) {
                currentKey = key;
                break;
            }
        }

        if (currentKey == null) {
            System.out.println("Коллекция не содержит элемент с таким индексом.");
            return;
        }

        ProductBuilder<T> productBuilder = productClientManager.createProductBuilder(products.get(currentKey));
        productBuilder = productClientManager.buildProductInput(productBuilder);
        products.replace(currentKey, productBuilder.create());
        System.out.println("Коллекция успешно обновлена.");
    }

    /**
     * Removes a product with the specified key.
     *
     * @param key the key of the product to remove
     */
    public void removeKey(K key) {
        idManager.removeId(products.remove(key));
        System.out.println("Коллекция успешно обновлена.");
    }

    /**
     * Clears all products from the collection.
     */
    public void clear() {
        products = new Hashtable<>();
        idManager.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    /**
     * Saves the collection to a file using the file manager.
     */
    public void save() {
        try {
            fileManager.save(products);
            System.out.println("Коллекция успешно сохранена.");
        } catch (Exception e) {
            System.out.println("Что-то пошло не так. ");
        }
    }

    /**
     * Exits the application.
     */
    public void exit() {
        System.out.println("Работа завершена, до свидания!");
        System.exit(0);
    }

    /**
     * Removes all products lower than the default product client manager's product.
     */
    public void removeLower() {
        removeLower();
    }

    /**
     * Removes all products lower than the specified product client manager's
     * product.
     *
     * @param productClientManager the product client manager providing the
     *                             comparison product
     */
    public void removeLower(ProductClientManager<T> productClientManager) {
        T product = productClientManager.getProduct();

        ArrayList<K> deleteKeys = new ArrayList<>();

        for (var key : products.keySet()) {
            if (product.compareTo(products.get(key)) > 0) {
                deleteKeys.add(key);
            }
        }

        for (var key : deleteKeys) {
            idManager.removeId(products.remove(key));
        }

        if (deleteKeys.size() > 0) {
            System.out.println("Коллекция успешно обновлена.");
        }
    }

    /**
     * Replaces a product if the new product is greater than the existing one, using
     * default client manager.
     *
     * @param key the key of the product to potentially replace
     */
    public void replaceIfGreater(K key) {
        replaceIfGreater(key, productClientManager);
    }

    /**
     * Replaces a product if the new product is greater than the existing one.
     *
     * @param key                  the key of the product to potentially replace
     * @param productClientManager the product client manager providing the new
     *                             product
     */
    public void replaceIfGreater(K key, ProductClientManager<T> productClientManager) {
        T product = productClientManager.getProduct();

        if (product.compareTo(products.get(key)) < 0) {
            idManager.removeId(products.get(key));
            products.replace(key, product);
            System.out.println("Коллекция успешно обновлена.");
        }
    }

    /**
     * Removes all products with keys greater than the specified key.
     *
     * @param comparedKey the key to compare against
     */
    public void removeGreaterKey(K comparedKey) {
        ArrayList<K> deleteKeys = new ArrayList<>();

        for (var key : products.keySet()) {
            if (comparedKey.compareTo(key) > 0) {
                deleteKeys.add(key);
            }
        }

        for (var key : deleteKeys) {
            idManager.removeId(products.remove(key));
        }

        if (deleteKeys.size() > 0) {
            System.out.println("Коллекция успешно обновлена.");
        }
    }

    /**
     * Counts products with price greater than the specified value.
     *
     * @param price the price threshold
     */
    public void countGreaterThanPrice(long price) {
        int count = 0;

        for (var product : products.values()) {
            if (product.getPrice() > price) {
                count++;
            }
        }

        String info = "Коллекция содержит " + count + " элементов дороже заданного.";
        System.out.println(info);
    }

    /**
     * Prints all unique unit of measure values present in the collection.
     */
    public void printUniqueUnitOfMeasure() {
        EnumSet<UnitOfMeasure> unitOfMeasureSet = EnumSet.noneOf(UnitOfMeasure.class);
        for (var product : products.values()) {
            if (product.getUnitOfMeasure() != null) {
                unitOfMeasureSet.add(product.getUnitOfMeasure());
            }
        }

        String info = "Коллекция содержит следующие уникальные значения unitOfMeasure: ";
        for (var element : unitOfMeasureSet) {
            info += "\n" + element;
        }
        System.out.println(info);
    }

    /**
     * Prints all product prices in descending order.
     */
    public void printFieldDescendingPrice() {
        String info = "Значения price в порядке убывания: ";
        ArrayList<T> productsList = new ArrayList<T>(products.values());
        Collections.sort(productsList);
        Collections.reverse(productsList);
        for (T product : productsList) {
            info += "\n" + product.getPrice();
        }
        System.out.println(info);
    }
}