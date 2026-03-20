package src.baseabstractions;

import src.baseobjects.Product;
import src.productcreation.ProductBuilder;

/**
 * Interface for client managers that handle Product objects.
 * Provides methods for retrieving and building products.
 *
 * @param <T> the type of Product object this manager handles, must extend Product
 */
public interface ProductClientManager<T extends Product> {
    /**
     * Retrieves a Product object.
     *
     * @return the Product object of type T
     */
    T getProduct();

    /**
     * Builds product input using the specified product builder.
     *
     * @param productBuilder the builder to use for product construction
     * @return the updated product builder
     */
    ProductBuilder<T> buildProductInput(ProductBuilder<T> productBuilder);

    /**
     * Creates a product builder initialized with the specified product.
     *
     * @param product the product to initialize the builder with
     * @return a new product builder
     */
    ProductBuilder<T> createProductBuilder(T product);
}