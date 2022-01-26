package com.alexpaxom.homework_2.helpers

import kotlin.math.pow

class NumAbbreviationFormatter {
    companion object {
        const val NOT_DEFINED_ABBR = "NN"
        const val NOT_DEFINED_VALUE = -1

        private val abbreviationMap = mapOf<Int, String>(
            0   to "",
            3   to "k",
            6   to "M",
            9   to "G",
        )

        fun convertNumToAbbreviation(num: Int):String {
            val p = when(num) {
                in 0..999 -> 0
                in 1000..999999 -> 3
                in 1000000..999999999 -> 6
                else -> NOT_DEFINED_VALUE
            }

            val leadNum = num.div(10f.pow(p)).toInt() // первые цифры
            val abbrPow = abbreviationMap[p] ?: NOT_DEFINED_ABBR // аббривеатура степени

            return "$leadNum$abbrPow"
        }
    }
}