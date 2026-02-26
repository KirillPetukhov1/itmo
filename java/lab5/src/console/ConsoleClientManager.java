package src.console;

import src.baseabstractions.ReaderWriter;
import src.baseobjects.Product;
import src.productcreation.ProductBuilder;

public class ConsoleClientManager {
    private ReaderWriter readerWriter;

    public ConsoleClientManager(ReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }

    public ReaderWriter getReaderWriter() {
        return readerWriter;
    }

    public Product getProduct() {
        ProductBuilder<Product> productBuilder = new ProductBuilder<>();
    }
}
