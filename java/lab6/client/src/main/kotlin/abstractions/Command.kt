package abstractions

import kotlinx.serialization.Serializable
import objects.Product

@Serializable
abstract class Command<K : Comparable<K>, V : Product> {
}