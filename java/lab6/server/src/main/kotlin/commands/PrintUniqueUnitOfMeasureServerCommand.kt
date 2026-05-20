package commands

import collection.CollectionManager
import connection.Response

/**
 * Collects all distinct UnitOfMeasure values present in the collection.
 *
 * @property collectionManager the collection to query
 */
class PrintUniqueUnitOfMeasureServerCommand(
    private val collectionManager: CollectionManager
) : ServerCommand {

    override fun execute(): Response =
        listResponse(
            "print_unique_unit_of_measure",
            "values",
            collectionManager.uniqueUnitOfMeasure().map { it.name }
        )
}
