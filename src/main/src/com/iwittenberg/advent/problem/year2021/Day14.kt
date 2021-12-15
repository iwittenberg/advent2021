package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import java.lang.StringBuilder
import kotlin.math.max

abstract class Day14Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<Pair<String, Map<String, Char>>, Long>(2021, 14, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Pair<String, Map<String, Char>> {
        val polymer = rawInput[0]

        val rules = rawInput.takeLast(rawInput.size - 2).fold(mutableMapOf<String,Char>()) { acc, line ->
            val (double, single) = line.split(" -> ")
            acc[double] = single[0]
            acc
        }
        return polymer to rules
    }

    override fun getTestCaseInput(): List<String> {
        return listOf(
            """
                NNCB

                CH -> B
                HH -> N
                CB -> H
                NH -> C
                HB -> C
                HC -> B
                HN -> C
                NN -> C
                BH -> H
                NC -> B
                NB -> B
                BN -> B
                BB -> N
                BC -> B
                CC -> N
                CN -> C
            """.trimIndent()
        )
    }

    fun solve(input: Pair<String, Map<String, Char>>, iterations: Int): Long {
        var (runningCount,  pairs) = (1 until input.first.length).fold(mutableMapOf(input.first[0] to 1L) to mutableMapOf<String, Long>()) { acc, i ->
            acc.first.addSafe(input.first[i], 1)
            val pair = input.first.substring(i-1, i+1)
            acc.second.addSafe(pair, 1)
            acc
        }

        repeat(iterations) {
            pairs = pairs.keys.fold(mutableMapOf()) { acc, key ->
                val new = input.second[key]!!
                val left = "${key[0]}$new"
                val right = "$new${key[1]}"

                runningCount.addSafe(new, pairs[key]!!)
                acc.addSafe(left, pairs[key]!!)
                acc.addSafe(right, pairs[key]!!)

                acc
            }
        }

        return runningCount.maxOf { it.value } - runningCount.minOf { it.value }
    }

    private fun <A> MutableMap<A, Long>.addSafe(index: A, value: Long) = apply { this[index] = getOrDefault(index, 0) + value }
}

@RunThis
class Day14Part1 : Day14Part(1, listOf(1588), 4244) {
    override fun solve(input: Pair<String, Map<String, Char>>) = solve(input, 10)
}

@RunThis
class Day14Part2 : Day14Part(2, listOf(2188189693529L), 4807056953866) {
    override fun solve(input: Pair<String, Map<String, Char>>) =  solve(input, 40)
}