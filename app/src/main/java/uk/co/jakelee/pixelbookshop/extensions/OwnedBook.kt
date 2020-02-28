package uk.co.jakelee.pixelbookshop.extensions

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

// E.g. 10 * Common(1.0) poor (0.5) reviewCopy(1.0) with missingCover(0.4) = £2.00
// E.g. 10 * Rare(1.5) fair(0.65) paperback(1.0) with foldedPages(0.9) = £8.78
// E.g. 10 * ExtremelyRare(3.0) new(1.0) signed(4.0) edition with no defect(1.0) = £120
fun OwnedBook.defaultValue() = 10
    .times(book.rarity.modifier)
    .times(bookQuality.modifier)
    .times(bookType.modifier)
    .times(bookDefect.modifier)
    .toBigDecimal()