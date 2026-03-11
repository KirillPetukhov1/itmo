package src.baseabstractions;

import src.baseobjects.Product;
import src.productcreation.ProductBuilder;

public interface ProductClientManager<T extends Product> {
    T getProduct();

    ProductBuilder<T> buildProductInput(ProductBuilder<T> productBuilder);
}
