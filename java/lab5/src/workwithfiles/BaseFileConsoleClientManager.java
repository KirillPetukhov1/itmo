package src.workwithfiles;

import java.time.LocalDateTime;

import src.baseabstractions.AbstractClientManager;
import src.baseabstractions.PersonClientManager;
import src.baseabstractions.ProductClientManager;
import src.baseabstractions.ReaderWriter;
import src.baseobjects.Person;
import src.baseobjects.Product;
import src.productcreation.LongIdManager;
import src.productcreation.PersonBuilder;
import src.productcreation.ProductBuilder;

public class BaseFileConsoleClientManager extends AbstractClientManager
        implements ProductClientManager<Product>, PersonClientManager<Person> {

    public BaseFileConsoleClientManager(ReaderWriter readerWriter) {
        super(readerWriter);
    }

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

    public ProductBuilder<Product> createProductBuilder(Product product) {
        return new ProductBuilder<Product>(product);
    }

    public ProductBuilder<Product> buildProductInput(ProductBuilder<Product> productBuilder) {
        ProductFileReader<Product> productReader = new ProductFileReader<>(productBuilder, getReaderWriter());
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
        PersonFileReader<Person> personReader = new PersonFileReader<>(personBuilder, getReaderWriter());
        personReader.readName();
        personReader.readHeight();
        personReader.readHairColor();
        personReader.readNationality();

        return personBuilder.create();
    }
}
