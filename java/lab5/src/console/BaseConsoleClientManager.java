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

public class BaseConsoleClientManager extends AbstractClientManager
        implements ProductClientManager<Product>, PersonClientManager<Person> {

    public BaseConsoleClientManager(ReaderWriter readerWriter) {
        super(readerWriter);
    }

    public Product getProduct() {
        ProductBuilder<Product> productBuilder = new ProductBuilder<Product>(new Product());
        
        productBuilder = buildProductInput(productBuilder);

        productBuilder = productBuilder.setCreationDate(LocalDateTime.now());

        LongIdManager<Product> idManager = new LongIdManager<>();
        productBuilder = productBuilder.setId(idManager.generateId());
        Product product = productBuilder.create();
        idManager.addId(product);
        
        return product;
    }

    // public Product getProductWithId(Long id) {
    //     ProductBuilder<Product> productBuilder = new ProductBuilder<Product>(new Product());

    //     productBuilder = buildProductWithoutId(productBuilder);


    // }

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
