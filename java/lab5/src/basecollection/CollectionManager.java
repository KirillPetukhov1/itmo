package src.basecollection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Hashtable;

import src.baseobjects.Product;
import src.console.CommandManager;

public class CollectionManager<K, T extends Product> {
    Hashtable<K, T> products = new Hashtable<>();
    private LocalDateTime localDateTime = LocalDateTime.now();
    private String filename;

    public void setProducts(Hashtable<K, T> products) {
        this.products = products;
    }

    public Hashtable<K, T> getProducts() {
        return products;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
            for (Product product : products.values()) {
                System.out.println(product.toString());
            }
        } else {
            System.out.println("Коллекция не содержит элементов.");
        }
    }
}
