package com.iwittenberg.advent.problem

abstract class Day3Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int?) :
    ProblemPart<List<String>, Int>(2021, 3, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<String> {
        return rawInput
    }

    override fun getTestCaseInput(): String {
        return """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
        """
    }
}

@RunThis
class Day3Part1 : Day3Part(1, 198, 2250414) {
    override fun solve(input: List<String>): Int {
        val length = input[0].length

        var gamma = ""
        var epsilon = ""
        (0 until length).forEach {
            var zeros = 0
            var ones = 0
            input.forEach { line ->
                when (line[it].digitToInt()) {
                    0 -> zeros++
                    1 -> ones++
                }
            }

            val sum = ones - zeros
            gamma += if (sum > 0) "1" else "0"
            epsilon += if (sum > 0) "0" else "1"
        }

        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }
}

@RunThis
class Day3Part2 : Day3Part(2, 230, 6085575) {
    override fun solve(input: List<String>): Int {
        val oxygenValue = processList(input.toMutableList())
        val co2Value = processList(input.toMutableList(), true)
        return Integer.parseInt(oxygenValue, 2) * Integer.parseInt(co2Value, 2)
    }

    private fun processList(input: List<String>, inv: Boolean = false): String {
        val length = input[0].length

        var values = input.toMutableList()
        (0 until length).forEach {
            val zeros = mutableListOf<String>()
            val ones = mutableListOf<String>()

            if (values.size == 1)
                return@forEach

            values.forEach { line ->
                when (line[it].digitToInt()) {
                    0 -> zeros.add(line)
                    1 -> ones.add(line)
                }
            }

            val sum = ones.size - zeros.size
            values = if (sum >= 0) {
                if (!inv) ones else zeros
            } else {
                if (!inv) zeros else ones
            }
        }
        return values[0]
    }
}