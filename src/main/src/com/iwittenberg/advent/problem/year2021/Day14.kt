package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis

abstract class Day14Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<String>, Long>(2021, 14, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<String> {
        return rawInput.map { line -> line }
    }

    override fun getTestCaseInput(): List<String> {
        return listOf(
            """
            """.trimIndent()
        )
    }
}

@RunThis
class Day14Part1 : Day14Part(1, listOf(1)) {
    override fun solve(input: List<String>): Long {
        return 0
    }
}

class Day14Part2 : Day14Part(2, listOf(1)) {
    override fun solve(input: List<String>): Long {
        return 0
    }
}