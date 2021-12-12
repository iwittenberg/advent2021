package com.iwittenberg.advent.problem.year2020

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis

private typealias PolicyCheck = Triple<IntRange, Char, String>

abstract class Day2Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<PolicyCheck>, Long>(2020, 2, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<PolicyCheck> {
        return rawInput.map { line ->
            val (range, char, password) = line.split(" ")
            val (start, end) = range.split("-")
            val neededChar = char[0]

            Triple(IntRange(start.toInt(), end.toInt()), neededChar, password)
        }
    }

    override fun getTestCaseInput(): List<String> {
        return listOf("""
            1-3 a: abcde
            1-3 b: cdefg
            2-9 c: ccccccccc
        """.trimIndent())
    }
}

@RunThis
class Day2Part1 : Day2Part(1, listOf(2), 560) {
    override fun solve(input: List<PolicyCheck>): Long {
        return input.count {
            it.first.contains(it.third.count { letter -> letter == it.second })
        }.toLong()
    }
}

@RunThis
class Day2Part2 : Day2Part(2, listOf(1), 303) {
    override fun solve(input: List<PolicyCheck>): Long {
        return input.count {
            val password = it.third
            val firstPos = password[it.first.first - 1]
            val secondPos = password[it.first.last - 1]

            (firstPos == it.second || secondPos == it.second) && firstPos != secondPos
        }.toLong()
    }
}