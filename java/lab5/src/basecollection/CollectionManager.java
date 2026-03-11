package src.basecollection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

import src.baseabstractions.ProductClientManager;
import src.baseobjects.Product;
import src.console.CommandManager;
import src.productcreation.LongIdManager;
import src.productcreation.ProductBuilder;

public class CollectionManager<K, T extends Product> {
    Hashtable<K, T> products = new Hashtable<>();
    private LocalDateTime localDateTime = LocalDateTime.now();
    private String filename;
    private ProductClientManager<T> productClientManager;

    public void setProducts(Hashtable<K, T> products) {
        this.products = products;
    }

    public Hashtable<K, T> getProducts() {
        return products;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setProductClientManager(ProductClientManager<T> productClientManager) {
        this.productClientManager = productClientManager;
    }

    public ProductClientManager<T> getProductClientManager() {
        return productClientManager;
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

    public void update(long id) {
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

        ProductBuilder<T> productBuilder = new ProductBuilder<>(products.get(currentKey));
        productBuilder = productClientManager.buildProductInput(productBuilder);
        System.out.println("Коллекция ]успешно обновлена.");
    }
}
