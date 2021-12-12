package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart

abstract class Day12Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<String>, Long>(2021, 12, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<String> {
        return rawInput.map { line -> line }
    }

    override fun getTestCaseInput(): String {
        return """
        """.trimIndent()
    }
}

class Day12Part1 : Day12Part(1, 1) {
    override fun solve(input: List<String>): Long {
        TODO("Not yet implemented")
    }
}

class Day12Part2 : Day12Part(2, 1) {
    override fun solve(input: List<String>): Long {
        TODO("Not yet implemented")
    }
}