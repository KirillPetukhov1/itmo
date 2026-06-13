package connection

import kotlinx.serialization.Serializable
import objects.Product

/**
 * Structured entry returned by the show command.
 *
 * Pairs a collection key and the login of the user who created the product with the
 * product itself. This accompanies the legacy string representation so the graphical
 * client can render every field as a separate column, color objects by their creator,
 * and decide which objects the current user is allowed to modify.
 *
 * @property key the collection key under which the product is stored
 * @property creatorLogin the login of the user who created the product
 * @property product the product associated with the key
 */
@Serializable
data class ProductEntry(
    val key: String,
    val creatorLogin: String,
    val product: Product
)