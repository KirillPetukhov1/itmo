package console

import abstractions.AbstractReaderWriter
import abstractions.EofException
import objectCreation.PersonBuilder
import objectCreation.ProductBuilder
import objects.Color
import objects.Coordinates
import objects.Country
import objects.Product
import objects.UnitOfMeasure

/**
 * Interactive form that reads all fields of a [Product] from an [AbstractReaderWriter].
 * Each field is read in a retry loop.
 *
 * Field invitations are sent via [AbstractReaderWriter.writeRequest] and are suppressed
 * in script mode. Validation error messages are sent via [AbstractReaderWriter.writeError]
 * and are always shown.
 *
 * If the input stream ends (EOF or Ctrl+D) while a field is being read, [EofException]
 * is thrown so the caller can perform a graceful shutdown.
 *
 * @property readerWriter the I/O channel to use for requests, errors, and input
 */
class ProductConsoleForm(private val readerWriter: AbstractReaderWriter) {

    /**
     * Reads all product fields and returns a fully populated [Product].
     *
     * @return the constructed product (without server-assigned id and creationDate)
     * @throws EofException if EOF is reached before all fields are supplied
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

    private fun nextLine(): String =
        readerWriter.readLine() ?: throw EofException()

    private fun readName(): String {
        while (true) {
            readerWriter.writeRequest("Enter product name:")
            val input = nextLine().trim()
            if (input.isNotBlank()) return input
            readerWriter.writeError("Name must not be blank. Try again.")
        }
    }

    private fun readCoordinates(): Coordinates {
        while (true) {
            try {
                readerWriter.writeRequest("Enter X coordinate (max 321):")
                val x = nextLine().trim().toLong()
                readerWriter.writeRequest("Enter Y coordinate:")
                val y = nextLine().trim().toDouble()
                return Coordinates(x, y)
            } catch (e: EofException) {
                throw e
            } catch (e: NumberFormatException) {
                readerWriter.writeError("Invalid number: ${e.message}. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("${e.message}. Try again.")
            }
        }
    }

    private fun readPrice(): Long {
        while (true) {
            readerWriter.writeRequest("Enter price (must be > 0):")
            try {
                val price = nextLine().trim().toLong()
                require(price > 0) { "Price must be greater than 0" }
                return price
            } catch (e: EofException) {
                throw e
            } catch (e: NumberFormatException) {
                readerWriter.writeError("Invalid number. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("${e.message}. Try again.")
            }
        }
    }

    private fun readPartNumber(): String? {
        readerWriter.writeRequest("Enter part number (leave blank for null):")
        val input = nextLine().trim()
        return input.takeIf { it.isNotBlank() }
    }

    private fun readManufactureCost(): Long? {
        readerWriter.writeRequest("Enter manufacture cost (leave blank for null):")
        val input = nextLine().trim()
        if (input.isBlank()) return null
        try {
            return input.toLong()
        } catch (e: NumberFormatException) {
            readerWriter.writeError("Invalid number. Try again.")
            val retry = nextLine().trim()
            if (retry.isBlank()) return null
            return try {
                retry.toLong()
            } catch (_: NumberFormatException) {
                readerWriter.writeError("Invalid number. Skipping field.")
                null
            }
        }
    }

    private fun readUnitOfMeasure(): UnitOfMeasure? {
        readerWriter.writeRequest("Enter unit of measure (leave blank for null): ${UnitOfMeasure.entries}")
        while (true) {
            val input = nextLine().trim()
            if (input.isBlank()) return null
            try {
                return UnitOfMeasure.valueOf(input.uppercase())
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("Unknown value. Valid values: ${UnitOfMeasure.entries}. Try again.")
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
            readerWriter.writeRequest("Enter owner name:")
            val input = nextLine().trim()
            if (input.isNotBlank()) return input
            readerWriter.writeError("Name must not be blank. Try again.")
        }
    }

    private fun readPersonHeight(): Long {
        while (true) {
            readerWriter.writeRequest("Enter owner height (must be > 0):")
            try {
                val height = nextLine().trim().toLong()
                require(height > 0) { "Height must be greater than 0" }
                return height
            } catch (e: EofException) {
                throw e
            } catch (e: NumberFormatException) {
                readerWriter.writeError("Invalid number. Try again.")
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("${e.message}. Try again.")
            }
        }
    }

    private fun readPersonHairColor(): Color {
        readerWriter.writeRequest("Enter hair color: ${Color.entries}")
        while (true) {
            try {
                return Color.valueOf(nextLine().trim().uppercase())
            } catch (e: EofException) {
                throw e
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("Unknown color. Valid values: ${Color.entries}. Try again.")
            }
        }
    }

    private fun readPersonNationality(): Country? {
        readerWriter.writeRequest("Enter nationality (leave blank for null): ${Country.entries}")
        while (true) {
            val input = nextLine().trim()
            if (input.isBlank()) return null
            try {
                return Country.valueOf(input.uppercase())
            } catch (e: IllegalArgumentException) {
                readerWriter.writeError("Unknown country. Valid values: ${Country.entries}. Try again.")
            }
        }
    }
}
