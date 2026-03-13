// package src.workwithfiles;

// import java.io.FileNotFoundException;

// import src.baseabstractions.AbstractFileConsoleReader;
// import src.baseobjects.Color;
// import src.baseobjects.Coordinates;
// import src.baseobjects.Country;
// import src.baseobjects.Person;
// import src.baseobjects.Product;
// import src.baseobjects.UnitOfMeasure;
// import src.productcreation.PersonBuilder;
// import src.productcreation.ProductBuilder;

// public class ProductFileConsoleReader<T extends Product> extends AbstractFileConsoleReader<ProductBuilder<Product>> {
//     private ProductBuilder<T> productBuilder = new ProductBuilder<T>(new Product());
//     private PersonBuilder<Person> personBuilder = new PersonBuilder<>(new Person());

//     public ProductBuilder<Product> getProductBuilder() {
//         return this.productBuilder;
//     }

//     public void setProductBuilder(ProductBuilder<Product> productBuilder) {
//         this.productBuilder = productBuilder;
//     }

//     public PersonBuilder<Person> getPersonBuilder() {
//         return this.personBuilder;
//     }

//     public void setPersonBuilder(PersonBuilder<Person> personBuilder) {
//         this.personBuilder = personBuilder;
//     }

//     public ProductFileConsoleReader(String fileName) throws FileNotFoundException {
//         super(fileName);
//     }

//     public void readName() {
//         String name = getScanner().next().trim();
//         productBuilder.setName(name);
//     }

//     public void readCoordinates() {
//         Long x = null;
//         Double y = null;

//         x = getScanner().nextLong();
//         y = getScanner().nextDouble();

//         productBuilder.setCoordinates(new Coordinates(x, y));
//     }

//     public void readPrice() {
//         long price = getScanner().nextLong();
//         productBuilder.setPrice(price);
//     }

//     public void readPartNumber() {
//         String partNumber = getScanner().next().trim();
//         productBuilder.setPartNumber(partNumber);
//     }

//     public void readManufactureCost() {
//         Long manufactureCost = getScanner().nextLong();
//         productBuilder.setManufactureCost(manufactureCost);
//     }

//     public void readUnitOfMeasure() {
//         String result = getScanner().next().trim();
//         if (result == "") {
//             productBuilder.setUnitOfMeasure(null);
//         }
//         UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(result.toUpperCase());
//         productBuilder.setUnitOfMeasure(unitOfMeasure);
//     }

//     public void readPerson() {
//         readPersonName();
//         readHeight();
//         readHairColor();
//         readNationality();
//         productBuilder.setOwner(personBuilder.create());
//     }

//     public void readPersonName() {
//         String name = getScanner().next().trim();
//         personBuilder.setName(name);
//     }

//     public void readHeight() {
//         long height = getScanner().nextLong();
//         personBuilder.setHeight(height);
//     }

//     public void readHairColor() {
//         String result = getScanner().next().trim().toUpperCase();
//         Color hairColor = Color.valueOf(result.toUpperCase());
//         personBuilder.setHairColor(hairColor);
//     }

//     public void readNationality() {
//         String result = getScanner().next().trim();
//         if (result == "") {
//             personBuilder.setNationality(null);
//         }
//         Country nationality = Country.valueOf(result.toUpperCase());
//         personBuilder.setNationality(nationality);
//     }

//     public void readProduct() {
//         readName();
//         readCoordinates();
//         readPrice();
//         readPartNumber();
//         readManufactureCost();
//         readUnitOfMeasure();
//         readPerson();
//     }

//     public ProductBuilder<Product> getMainBuilder() {
//         return productBuilder;
//     }
// }
