package com.iwittenberg.advent.problem

abstract class Day9Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<List<String>, Int>(2021, 9, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<String> {
        return rawInput.map { line -> line }
    }

    override fun getTestCaseInput(): String {
        return """
        """.trimIndent()
    }
}

class Day9Part1 : Day9Part(1, 1) {
    override fun solve(input: List<String>): Int {
        TODO("Not yet implemented")
    }
}

class Day9Part2 : Day9Part(2, 1) {
    override fun solve(input: List<String>): Int {
        TODO("Not yet implemented")
    }
}