package src.console;

import java.time.LocalDateTime;

import src.baseabstractions.AbstractClientManager;
import src.baseabstractions.ProductClientManager;
import src.baseabstractions.PersonClientManager;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Product;
import src.baseobjects.Person;
import src.productcreation.ProductBuilder;
import src.productcreation.LongIdManager;
import src.productcreation.PersonBuilder;

/**
 * Base console implementation of client managers for both Product and Person
 * objects.
 * Provides interactive console-based creation and input handling for products
 * and persons.
 */
public class BaseConsoleClientManager extends AbstractClientManager
        implements ProductClientManager<Product>, PersonClientManager<Person> {

    /**
     * Constructs a BaseConsoleClientManager with the specified reader/writer.
     *
     * @param readerWriter the reader/writer for console I/O operations
     */
    public BaseConsoleClientManager(ReaderWriter readerWriter) {
        super(readerWriter);
    }

    /**
     * Creates a new Product object through interactive console input.
     * Handles all product fields including automatic ID generation and owner
     * creation.
     *
     * @return the created Product object
     */
    public Product getProduct() {
        ProductBuilder<Product> productBuilder = createProductBuilder(new Product());

        productBuilder = buildProductInput(productBuilder);

        productBuilder = productBuilder.setCreationDate(LocalDateTime.now());

        LongIdManager<Product> idManager = new LongIdManager<>();
        productBuilder = productBuilder.setId(idManager.generateId());
        Product product = productBuilder.create();
        idManager.addId(product);

        return product;
    }

    /**
     * Creates a product builder initialized with the specified product.
     *
     * @param product the product to initialize the builder with
     * @return a new product builder
     */
    public ProductBuilder<Product> createProductBuilder(Product product) {
        return new ProductBuilder<Product>(product);
    }

    /**
     * Builds product input by reading all product fields from console.
     * Includes reading of basic product fields and associated person (owner).
     *
     * @param productBuilder the builder to use for product construction
     * @return the updated product builder
     */
    public ProductBuilder<Product> buildProductInput(ProductBuilder<Product> productBuilder) {
        ProductReader<Product> productReader = new ProductReader<>(productBuilder, getReaderWriter());
        productReader.readName();
        productReader.readCoordinates();
        productReader.readPrice();
        productReader.readPartNumber();
        productReader.readManufactureCost();
        productReader.readUnitOfMeasure();

        Person person = getPerson();
        productBuilder = productBuilder.setOwner(person);

        return productBuilder;
    }

    /**
     * Creates a new Person object through interactive console input.
     * Handles all person fields.
     *
     * @return the created Person object
     */
    public Person getPerson() {
        PersonBuilder<Person> personBuilder = new PersonBuilder<>(new Person());
        PersonReader<Person> personReader = new PersonReader<>(personBuilder, getReaderWriter());
        personReader.readName();
        personReader.readHeight();
        personReader.readHairColor();
        personReader.readNationality();

        return personBuilder.create();
    }
}