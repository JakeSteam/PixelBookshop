package uk.co.jakelee.pixelbookshop.utils

import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class Xp {
    companion object {

        private const val levelModifier = 0.05

        fun xpToLevel(xp: Long) = (levelModifier * sqrt(xp.toDouble())).toInt()

        fun levelToXp(level: Int) = (level / levelModifier).pow(2).toInt()

        fun getLevelProgress(xp: Long): Int {
            val currentLevel = xpToLevel(xp)

            val currentLevelXp = levelToXp(currentLevel)
            val nextLevelXp = levelToXp(currentLevel + 1)

            val neededXP = (nextLevelXp - currentLevelXp).toDouble()
            val earnedXP = nextLevelXp - xp

            val progress = 100 - (earnedXP / neededXP * 100)
            return floor(progress).toInt()
        }
    }

}