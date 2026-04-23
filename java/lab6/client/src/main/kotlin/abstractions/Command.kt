package abstractions

import objects.Product

abstract class Command<K : Comparable<K>, V : Product> {
    abstract val description: String

    abstract val isShouldBeSent: Boolean

    abstract fun start(args: Array<String>)

    abstract fun finish()
}