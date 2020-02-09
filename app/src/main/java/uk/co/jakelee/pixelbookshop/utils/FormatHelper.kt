package uk.co.jakelee.pixelbookshop.utils

import kotlin.math.ln
import kotlin.math.pow

class FormatHelper {
    companion object {
        fun int(number: Int): String {
            if (number < 1000) return "" + this
            val exp = (ln(number.toDouble()) / ln(1000.0)).toInt()
            return String.format(
                "%.1f%c",
                number / 1000.0.pow(exp.toDouble()),
                "kMGTPE"[exp - 1]
            )
        }
    }
}