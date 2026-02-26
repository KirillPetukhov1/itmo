package src.productcreation;

import src.baseobjects.Product;

import java.util.TreeSet;

public class IdManager {
    private static TreeSet<Long> ids = new TreeSet<>();

    public long generateId() {
        long maxId = ids.last();
        return maxId + 1;
    }

    public boolean addId(Product product) {
        return ids.add(product.getId());
    }

    public boolean removeId(Product product) {
        return ids.remove(product.getId());
    }

    public TreeSet<Long> getIds() {
        return ids;
    }

    public boolean isIdExists(Product product) {
        return ids.contains(product.getId());
    }
}
