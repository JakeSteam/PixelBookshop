package uk.co.jakelee.pixelbookshop.data

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.Player
import uk.co.jakelee.pixelbookshop.database.entity.Shop

val ownedFurniture = OwnedFurniture(
    1,
    0,
    0,
    true,
    Furniture.Lectern.id
)

val ownedBook = OwnedBook(
    1,
    Book.Orwell1984.id,
    ownedFurniture.id,
    OwnedBookDefect.FoldedPages.id,
    OwnedBookQuality.Poor.id,
    OwnedBookSource.Gift.id,
    OwnedBookType.Paperback.id
)

val player = Player(
    "Jake",
    100,
    2000,
    System.currentTimeMillis()
)

val shop = Shop(
    1,
    "Jake's Shop",
    Floor.Marble.id,
    Wall.Brick.id,
    System.currentTimeMillis()
)