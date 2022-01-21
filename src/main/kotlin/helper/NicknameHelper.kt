package helper

import getRandomSeed

object NicknameHelper {
    fun getRandom(): String {
        return "user${getRandomSeed()}"
    }
}