package com.iwittenberg.advent.problem.year2020

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import java.lang.Math.abs

abstract class Day1Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<Set<Int>, Long>(2020, 1, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Set<Int> {
        return rawInput.map { it.toInt() }.toSet()
    }

    override fun getTestCaseInput(): String {
        return """
            1721
            979
            366
            299
            675
            1456
        """.trimIndent()
    }
}

@RunThis
class Day1Part1 : Day1Part(1, 514579, 100419) {
    override fun solve(input: Set<Int>): Long {
        input.forEach {
            val needToFind = 2020 - it
            if (input.contains(needToFind)) return needToFind.toLong() * it.toLong()
        }
        return 0
    }
}

@RunThis
class Day1Part2 : Day1Part(2, 241861950, 265253940) {
    override fun solve(input: Set<Int>): Long {
        input.forEach { first ->
            input.forEach { second ->
                val needToFind = 2020 - (first + second)
                if (needToFind in input) return first.toLong() * needToFind.toLong() * second.toLong()
            }
        }
        return 0
    }
}