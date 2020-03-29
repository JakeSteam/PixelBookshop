package uk.co.jakelee.pixelbookshop.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toCurrencyString() = this
    .setScale(2, RoundingMode.HALF_UP)
    .toString()