package uk.co.jakelee.pixelbookshop.utils

class RandomHelper {

    fun getDouble(max: Double) = Math.random() * max

    fun getInt(max: Int) = (0..max).random()
}