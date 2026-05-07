package objectCreation

import java.util.concurrent.atomic.AtomicLong

/**
 * Generates and tracks unique Long identifiers for products in the collection.
 * Thread-safe via [AtomicLong] counter.
 */
class IdManager {

    private val counter = AtomicLong(0)
    private val usedIds = mutableSetOf<Long>()

    /**
     * Generates the next available unique identifier.
     *
     * @return a positive Long that has not been issued before
     */
    fun nextId(): Long {
        var id = counter.incrementAndGet()
        while (usedIds.contains(id)) {
            id = counter.incrementAndGet()
        }
        usedIds.add(id)
        return id
    }

    /**
     * Registers an existing [id] so it will not be re-issued.
     * Used when loading a collection from file.
     *
     * @param id the identifier to mark as used
     */
    fun registerId(id: Long) {
        usedIds.add(id)
        if (id > counter.get()) {
            counter.set(id)
        }
    }

    /**
     * Releases an [id] so it may be reused in the future.
     *
     * @param id the identifier to release
     */
    fun releaseId(id: Long) {
        usedIds.remove(id)
    }

    /**
     * Clears all registered identifiers and resets the counter.
     */
    fun clear() {
        usedIds.clear()
        counter.set(0)
    }
}
