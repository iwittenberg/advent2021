package com.iwittenberg.advent.problem

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

abstract class Day7Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long?) :
    ProblemPart<List<Int>, Long>(2021, 7, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<Int> {
        return rawInput[0].split(",").map { it.toInt() }
    }

    override fun getTestCaseInput(): String {
        return "16,1,2,0,4,2,7,1,2,14"
    }
}

@RunThis
class Day7Part1 : Day7Part(1, 37, 349357) {
    override fun solve(input: List<Int>): Long {
        val sorted = input.sorted()
        val mean = sorted[sorted.size / 2]

        return sorted.sumOf { abs(it - mean) }.toLong()
    }
}

@RunThis
class Day7Part2 : Day7Part(2, 168, 96708205) {
    override fun solve(input: List<Int>): Long {
        val average = round(input.sum().toDouble() / input.size.toDouble() ).toInt()

        val one = input.sumOf {
            val n = abs(it - (average+1))
            (n * (n + 1)) / 2
        }.toLong()

        val two = input.sumOf {
            val n = abs(it - average)
            (n * (n + 1)) / 2
        }.toLong()

        val three = input.sumOf {
            val n = abs(it - (average-1))
            (n * (n + 1)) / 2
        }.toLong()

        return minOf(one, two, three)
    }
}