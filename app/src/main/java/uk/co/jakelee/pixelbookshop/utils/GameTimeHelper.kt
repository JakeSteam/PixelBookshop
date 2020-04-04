package uk.co.jakelee.pixelbookshop.utils

class GameTimeHelper {


    companion object {
        const val START_HOUR = 0
        const val END_HOUR = 8

        fun isDuringDay(hour: Int) = hour in 1..10
    }
}