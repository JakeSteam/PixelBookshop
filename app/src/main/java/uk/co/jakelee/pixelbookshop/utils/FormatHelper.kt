package uk.co.jakelee.pixelbookshop.utils

import java.text.DecimalFormat

class FormatHelper {
    companion object {
        fun int(number: Int): String {
            val suffixes = arrayOf("", "K", "M", "B", "T", "P", "E")
            var index = 0
            var result = number
            while (result / 1000 >= 1) {
                result /= 1000
                index++
            }
            val decimalFormat = DecimalFormat("#.##")
            return String.format("%s%s", decimalFormat.format(result), suffixes[index])
        }
    }
}