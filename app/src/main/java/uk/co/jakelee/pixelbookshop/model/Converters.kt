package uk.co.jakelee.pixelbookshop.model

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun bookToInt(value: Book) = value.id
    @TypeConverter fun intToBook(id: Int) = Book.values().first { it.id == id }

    @TypeConverter fun bookGenreToInt(value: BookGenre) = value.id
    @TypeConverter fun intToBookGenre(id: Int) = BookGenre.values().first { it.id == id }

    @TypeConverter fun bookRarityToInt(value: BookRarity) = value.id
    @TypeConverter fun intToBookRarity(id: Int) = BookRarity.values().first { it.id == id }

    @TypeConverter fun floorToInt(value: Floor) = value.id
    @TypeConverter fun intToFloor(id: Int) = Floor.values().first { it.id == id }

    @TypeConverter fun furnitureToInt(value: Furniture) = value.id
    @TypeConverter fun intToFurniture(id: Int) = Furniture.values().first { it.id == id }

    @TypeConverter fun furnitureTypeToInt(value: FurnitureType) = value.id
    @TypeConverter fun intToFurnitureType(id: Int) = FurnitureType.values().first { it.id == id }

    @TypeConverter fun ownedBookDefectToInt(value: OwnedBookDefect) = value.id
    @TypeConverter fun intToOwnedBookDefect(id: Int) = OwnedBookDefect.values().first { it.id == id }

    @TypeConverter fun ownedBookQualityToInt(value: OwnedBookQuality) = value.id
    @TypeConverter fun intToOwnedBookQuality(id: Int) = OwnedBookQuality.values().first { it.id == id }

    @TypeConverter fun ownedBookSourceToInt(value: OwnedBookSource) = value.id
    @TypeConverter fun intToOwnedBookSource(id: Int) = OwnedBookSource.values().first { it.id == id }

    @TypeConverter fun ownedBookTypeToInt(value: OwnedBookType) = value.id
    @TypeConverter fun intToOwnedBookType(id: Int) = OwnedBookType.values().first { it.id == id }

    @TypeConverter fun wallToInt(value: Wall) = value.id
    @TypeConverter fun intToWall(id: Int) = Wall.values().first { it.id == id }
}