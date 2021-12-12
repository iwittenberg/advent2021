package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import kotlin.math.abs
import kotlin.math.max

abstract class Day5Part(part: Int, testCaseAnswer: List<Int>, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<List<Vent>, Int>(2021, 5, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<Vent> {
        return rawInput.map { line ->
            val pairs = line.split(" -> ").map { pair ->
                val values = pair.split(",")
                values[0].toInt() to values[1].toInt()
            }
            Vent(pairs[0], pairs[1])
        }
    }

    override fun getTestCaseInput(): List<String> {
        return listOf("""
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent())
    }

    fun innerSolve(input: List<Vent>): Int {
        val hits = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach { vent ->
            vent.getAllPoints().forEach { point ->
                hits.putIfAbsent(point, 0)
                hits[point] = hits[point]!! + 1
            }
        }

        return hits.count { it.value >= 2 }
    }
}

@RunThis
class Day5Part1 : Day5Part(1, listOf(5), 4728) {
    override fun solve(input: List<Vent>): Int {
        val filtered = input.filter { it.extent1.first == it.extent2.first || it.extent1.second == it.extent2.second }
        return innerSolve(filtered)
    }
}

@RunThis
class Day5Part2 : Day5Part(2, listOf(12), 17717) {
    override fun solve(input: List<Vent>) = innerSolve(input)
}

class Vent(
    val extent1: Pair<Int, Int>,
    val extent2: Pair<Int, Int>
) {
    override fun toString(): String {
        return "$extent1 -> $extent2"
    }

    fun getAllPoints(): List<Pair<Int, Int>> {
        val addX = if (extent1.first < extent2.first) 1 else if (extent1.first == extent2.first) 0 else -1
        val addY = if (extent1.second < extent2.second) 1 else if (extent1.second == extent2.second) 0 else -1

        val pairs = mutableListOf<Pair<Int, Int>>()
        val range = max(abs(extent1.first - extent2.first), abs(extent1.second - extent2.second))
        (0..range).forEach {
            pairs.add(Pair(extent1.first + (addX * it), extent1.second + (addY * it)))
        }
        return pairs
    }
}