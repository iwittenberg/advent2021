package com.iwittenberg.advent.problem

abstract class Day1Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int?) :
    ProblemPart<List<Int>, Int>(2021, 1, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun getTestCaseInput(): String {
        return """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
        """
    }

    override fun convertToInputType(rawInput: List<String>): List<Int> {
        return rawInput.map { it.toInt() }
    }
}

@RunThis
class Day1Part1 : Day1Part(1, 7, 1557) {
    override fun solve(input: List<Int>): Int {
        var greaterThan = 0
        for (i in 1 until input.size) {
            val prev = input[i - 1]
            val current = input[i]
            if (current > prev) greaterThan++
        }
        return greaterThan
    }
}

@RunThis
class Day1Part2 : Day1Part(2, 5, 1608) {
    override fun solve(input: List<Int>): Int {
        var greaterThan = 0
        for (i in 3 until input.size) {
            val first = input[i - 3]
            val second = input[i - 2]
            val third = input[i - 1]
            val fourth = input[i]

            val prev = first + second + third
            val current = second + third + fourth
            if (current > prev) greaterThan++
        }
        return greaterThan
    }
}
