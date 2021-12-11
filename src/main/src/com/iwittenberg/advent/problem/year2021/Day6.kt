package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis

abstract class Day6Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<Int>, Long>(2021, 6, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<Int> {
        return rawInput[0].split(",").map { it.toInt() }
    }

    override fun getTestCaseInput() = "3,4,3,1,2"

    fun solveInternal(input: List<Int>, days: Int): Long {
        var fish = mutableMapOf<Int, Long>()
        input.forEach { fish[it] = fish.getOrDefault(it, 0) + 1 }
        repeat(days) {
            val tempFish = mutableMapOf<Int, Long>()
            fish.keys.forEach {
                when (it - 1) {
                    -1 -> {
                        tempFish[6] = tempFish.getOrDefault(6, 0).plus(fish[it]!!)
                        tempFish[8] = fish[it]!!
                    }
                    else -> tempFish[it - 1] = tempFish.getOrDefault(it - 1, 0).plus(fish[it]!!)
                }
            }
            fish = tempFish
        }

        return fish.values.sum()
    }
}

@RunThis
class Day6Part1 : Day6Part(1, 5934, 391888) {
    override fun solve(input: List<Int>) = solveInternal(input, 80)
}

@RunThis
class Day6Part2 : Day6Part(2, 26984457539, 1754597645339) {
    override fun solve(input: List<Int>) = solveInternal(input, 256)
}