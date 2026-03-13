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

public class CollectionManager<K extends Comparable<K>, T extends Product> {
    Hashtable<K, T> products = new Hashtable<>();
    private LocalDateTime localDateTime = LocalDateTime.now();
    private ProductClientManager<T> productClientManager;
    private ParseKey<K> keyParser;
    private LongIdManager<T> idManager;
    private AbstractFileManager<Hashtable<K, T>> fileManager;

    public void setProducts(Hashtable<K, T> products) {
        this.products = products;
    }

    public Hashtable<K, T> getProducts() {
        return products;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setProductClientManager(ProductClientManager<T> productClientManager) {
        this.productClientManager = productClientManager;
    }

    public ProductClientManager<T> getProductClientManager() {
        return productClientManager;
    }

    public void setKeyParser(ParseKey<K> keyParser) {
        this.keyParser = keyParser;
    }

    public ParseKey<K> getKeyParser() {
        return keyParser;
    }

    public void setIdManager(LongIdManager<T> idManager) {
        this.idManager = idManager;
    }

    public LongIdManager<T> getIdManager() {
        return idManager;
    }

    public void setFileManager(AbstractFileManager<Hashtable<K, T>> fileManager) {
        this.fileManager = fileManager;
    }

    public AbstractFileManager<Hashtable<K, T>> getFileManager() {
        return fileManager;
    }

    public void help() {
        CommandManager.getCommands().values().forEach(command -> System.out.println(command.getDescription()));
    }

    public void info() {
        String info = "Тип коллекции: " + products.getClass().getSimpleName() + "\nДата инициализации: "
                + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\nКоличество элементов: "
                + products.size();
        System.out.println(info);
    }

    public void show() {
        if (!products.isEmpty()) {
            for (T product : products.values()) {
                System.out.println(product.toString());
            }
        } else {
            System.out.println("Коллекция не содержит элементов.");
        }
    }

    public void insert(K key) {
        insert(key, productClientManager);
    }

    public void insert(K key, ProductClientManager<T> productClientManager) {
        if (products.containsKey(key)) {
            System.out.println("Коллекция уже содержит элемент с таким ключом. Будет произведена перезапись.");
        }
        T product = productClientManager.getProduct();
        products.put(key, product);
        System.out.println("Коллекция успешно обновлена.");
    }

    public void update(long id) {
        update(id, productClientManager);
    }

    public void update(long id, ProductClientManager<T> productClientManager) {
        if (!(new LongIdManager<T>()).isIdExists(id)) {
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

    public void removeKey(K key) {
        idManager.removeId(products.remove(key));
        System.out.println("Коллекция успешно обновлена.");
    }

    public void clear() {
        products = new Hashtable<>();
        idManager.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    public void save() {
        try {
            fileManager.save(products);
            System.out.println("Коллекция успешно сохранена.");
        } catch (Exception e) {
            System.out.println("Что-то пошло не так. ");
        }
    }

    public void exit() {
        System.out.println("Работа завершена, до свидания!");
        System.exit(0);
    }

    public void removeLower() {
        removeLower();
    }

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

    public void replaceIfGreater(K key) {
        replaceIfGreater(key, productClientManager);
    }

    public void replaceIfGreater(K key, ProductClientManager<T> productClientManager) {
        T product = productClientManager.getProduct();

        if (product.compareTo(products.get(key)) < 0) {
            idManager.removeId(products.get(key));
            products.replace(key, product);
            System.out.println("Коллекция успешно обновлена.");
        }
    }

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
