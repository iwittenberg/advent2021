package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import kotlin.math.abs
import kotlin.math.round

abstract class Day7Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<Int>, Long>(2021, 7, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<Int> = rawInput[0].split(",").map { it.toInt() }
    override fun getTestCaseInput() = listOf("16,1,2,0,4,2,7,1,2,14")

}

@RunThis
class Day7Part1 : Day7Part(1, listOf(37), 349357) {
    override fun solve(input: List<Int>): Long {
        val sorted = input.sorted()
        val median = sorted[sorted.size / 2]

        return sorted.sumOf { abs(it - median) }.toLong()
    }
}

@RunThis
class Day7Part2 : Day7Part(2, listOf(168), 96708205) {
    override fun solve(input: List<Int>): Long {
        val average = round(input.sum().toDouble() / input.size.toDouble()).toInt()

        return ((average-1)..(average+1)).map { target ->
            input.sumOf {
                val n = abs(it - target)
                (n * (n + 1)) / 2
            }.toLong()
        }.minOf { it }
    }
}