package abstractions

import objects.Product

abstract class AbstractCommandManager<K : Comparable<K>, V : Product> {
    abstract val commands: HashMap<K, Command<K, V>>
}