package sortfilter

import connection.ProductEntry
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * Builds [Predicate] instances for [ProductEntry] objects using the Stream API.
 *
 * Filtering is implemented by streaming all searchable field values of an entry and
 * checking whether any of them contains the filter text as a case-insensitive substring.
 * Sorting comparators are provided for each column so callers can construct multi-level
 * sort chains via [Comparator.thenComparing] without depending on JavaFX internals.
 */
object TableFilter {

    /**
     * Returns a predicate that accepts entries whose any field value contains [filterText]
     * as a case-insensitive substring.
     * An empty or blank [filterText] produces a predicate that accepts every entry.
     *
     * @param filterText the substring to search for across all fields
     * @return a [Predicate] implemented with [Stream.anyMatch]
     */
    fun buildPredicate(filterText: String): Predicate<ProductEntry> {
        val text = filterText.trim().lowercase()
        if (text.isEmpty()) return Predicate { true }
        return Predicate { entry ->
            fieldValues(entry).anyMatch { field ->
                field.lowercase().contains(text)
            }
        }
    }

    /**
     * Returns a [Comparator] for [ProductEntry] objects that sorts by the column identified
     * by [columnId] in ascending order. Null field values sort before non-null values.
     *
     * @param columnId the column identifier matching a value returned by [columnIds]
     * @return a [Comparator] suitable for [java.util.stream.Stream.sorted]
     */
    fun comparatorFor(columnId: String): Comparator<ProductEntry> = when (columnId) {
        COL_KEY -> compareBy { it.key }
        COL_CREATOR -> compareBy { it.creatorLogin }
        COL_ID -> compareBy { it.product.id }
        COL_NAME -> compareBy { it.product.name }
        COL_COORD_X -> compareBy { it.product.coordinates.x }
        COL_COORD_Y -> compareBy { it.product.coordinates.y }
        COL_CREATION_DATE -> compareBy { it.product.creationDate }
        COL_PRICE -> compareBy { it.product.price }
        COL_PART_NUMBER -> compareBy { it.product.partNumber }
        COL_MANUFACTURE_COST -> compareBy { it.product.manufactureCost }
        COL_UNIT_OF_MEASURE -> compareBy { it.product.unitOfMeasure?.name }
        COL_OWNER_NAME -> compareBy { it.product.owner.name }
        COL_OWNER_HEIGHT -> compareBy { it.product.owner.height }
        COL_OWNER_HAIR_COLOR -> compareBy { it.product.owner.hairColor.name }
        COL_OWNER_NATIONALITY -> compareBy { it.product.owner.nationality?.name }
        else -> compareBy { it.product.price }
    }

    /**
     * Returns a [Stream] of every searchable string field value in [entry].
     * Null fields are represented as empty strings so the stream always contains
     * exactly fifteen elements.
     *
     * @param entry the entry whose fields are extracted
     * @return stream of lowercase-ready field strings
     */
    fun fieldValues(entry: ProductEntry): Stream<String> {
        val p = entry.product
        return Stream.of(
            entry.key,
            entry.creatorLogin,
            p.id.toString(),
            p.name,
            p.coordinates.x.toString(),
            p.coordinates.y.toString(),
            p.creationDate,
            p.price.toString(),
            p.partNumber ?: "",
            p.manufactureCost?.toString() ?: "",
            p.unitOfMeasure?.name ?: "",
            p.owner.name,
            p.owner.height.toString(),
            p.owner.hairColor.name,
            p.owner.nationality?.name ?: ""
        )
    }

    const val COL_KEY = "key"
    const val COL_CREATOR = "creator"
    const val COL_ID = "id"
    const val COL_NAME = "name"
    const val COL_COORD_X = "coordX"
    const val COL_COORD_Y = "coordY"
    const val COL_CREATION_DATE = "creationDate"
    const val COL_PRICE = "price"
    const val COL_PART_NUMBER = "partNumber"
    const val COL_MANUFACTURE_COST = "manufactureCost"
    const val COL_UNIT_OF_MEASURE = "unitOfMeasure"
    const val COL_OWNER_NAME = "ownerName"
    const val COL_OWNER_HEIGHT = "ownerHeight"
    const val COL_OWNER_HAIR_COLOR = "ownerHairColor"
    const val COL_OWNER_NATIONALITY = "ownerNationality"
}