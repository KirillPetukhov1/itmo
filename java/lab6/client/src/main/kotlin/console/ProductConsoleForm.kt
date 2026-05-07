package console

import abstractions.AbstractReaderWriter
import objectCreation.PersonBuilder
import objectCreation.ProductBuilder
import objects.Color
import objects.Coordinates
import objects.Country
import objects.UnitOfMeasure
import objects.Product

/**
 * Interactive form that reads all fields of a [Product] from an [AbstractReaderWriter].
 * Each field is read in a retry loop: the user is re-prompted on any validation error.
 *
 * @property readerWriter the I/O channel to use for prompts and input
 */
class ProductConsoleForm(private val readerWriter: AbstractReaderWriter) {

    /**
     * Reads all product fields interactively and returns a fully populated [Product].
     *
     * @return the constructed product (without server-assigned id and creationDate)
     */
    fun readProduct(): Product {
        val builder = ProductBuilder()
        builder.setName(readName())
        builder.setCoordinates(readCoordinates())
        builder.setPrice(readPrice())
        builder.setPartNumber(readPartNumber())
        builder.setManufactureCost(readManufactureCost())
        builder.setUnitOfMeasure(readUnitOfMeasure())
        builder.setOwner(readPerson())
        return builder.build()
    }

    private fun readName(): String {
        while (true) {
            readerWriter.write("Enter product name:")
            val input = readerWriter.readLine().trim()
            if (input.isNotBlank()) return input
            readerWriter.write("Name must not be blank. Try again.")
        }
    }

    private fun readCoordinates(): Coordinates {
        while (true) {
            try {
                readerWriter.write("Enter X coordinate (max 321):")
                val x = readerWriter.readLine().trim().toLong()
                readerWriter.write("Enter Y coordinate:")
                val y = readerWriter.readLine().trim().toDouble()
                return Coordinates(x, y)
            } catch (e: NumberFormatException) {
                readerWriter.write("Invalid number: ${e.message}. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.write("${e.message}. Try again.")
            }
        }
    }

    private fun readPrice(): Long {
        while (true) {
            readerWriter.write("Enter price (must be > 0):")
            try {
                val price = readerWriter.readLine().trim().toLong()
                require(price > 0) { "Price must be greater than 0" }
                return price
            } catch (e: NumberFormatException) {
                readerWriter.write("Invalid number. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.write("${e.message}. Try again.")
            }
        }
    }

    private fun readPartNumber(): String? {
        readerWriter.write("Enter part number (leave blank for null):")
        val input = readerWriter.readLine().trim()
        return input.takeIf { it.isNotBlank() }
    }

    private fun readManufactureCost(): Long? {
        readerWriter.write("Enter manufacture cost (leave blank for null):")
        val input = readerWriter.readLine().trim()
        if (input.isBlank()) return null
        while (true) {
            try {
                return input.toLong()
            } catch (e: NumberFormatException) {
                readerWriter.write("Invalid number. Try again.")
                val retry = readerWriter.readLine().trim()
                if (retry.isBlank()) return null
                try { return retry.toLong() } catch (_: NumberFormatException) {
                    readerWriter.write("Invalid number. Skipping field.")
                    return null
                }
            }
        }
    }

    private fun readUnitOfMeasure(): UnitOfMeasure? {
        readerWriter.write("Enter unit of measure (leave blank for null): ${UnitOfMeasure.entries}")
        while (true) {
            val input = readerWriter.readLine().trim()
            if (input.isBlank()) return null
            try {
                return UnitOfMeasure.valueOf(input.uppercase())
            } catch (e: IllegalArgumentException) {
                readerWriter.write("Unknown value. Valid values: ${UnitOfMeasure.entries}. Try again.")
            }
        }
    }

    private fun readPerson(): objects.Person {
        val builder = PersonBuilder()
        builder.setName(readPersonName())
        builder.setHeight(readPersonHeight())
        builder.setHairColor(readPersonHairColor())
        builder.setNationality(readPersonNationality())
        return builder.build()
    }

    private fun readPersonName(): String {
        while (true) {
            readerWriter.write("Enter owner name:")
            val input = readerWriter.readLine().trim()
            if (input.isNotBlank()) return input
            readerWriter.write("Name must not be blank. Try again.")
        }
    }

    private fun readPersonHeight(): Long {
        while (true) {
            readerWriter.write("Enter owner height (must be > 0):")
            try {
                val height = readerWriter.readLine().trim().toLong()
                require(height > 0) { "Height must be greater than 0" }
                return height
            } catch (e: NumberFormatException) {
                readerWriter.write("Invalid number. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.write("${e.message}. Try again.")
            }
        }
    }

    private fun readPersonHairColor(): Color {
        readerWriter.write("Enter hair color: ${Color.entries}")
        while (true) {
            try {
                return Color.valueOf(readerWriter.readLine().trim().uppercase())
            } catch (e: IllegalArgumentException) {
                readerWriter.write("Unknown color. Valid values: ${Color.entries}. Try again.")
            }
        }
    }

    private fun readPersonNationality(): Country? {
        readerWriter.write("Enter nationality (leave blank for null): ${Country.entries}")
        while (true) {
            val input = readerWriter.readLine().trim()
            if (input.isBlank()) return null
            try {
                return Country.valueOf(input.uppercase())
            } catch (e: IllegalArgumentException) {
                readerWriter.write("Unknown country. Valid values: ${Country.entries}. Try again.")
            }
        }
    }
}
