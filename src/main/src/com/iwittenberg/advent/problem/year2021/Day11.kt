package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.util.MutableIntGrid
import com.iwittenberg.advent.util.Point2d
import com.iwittenberg.advent.util.generateAllNeighbors
import com.iwittenberg.advent.util.valueAt
import com.iwittenberg.advent.util.increment
import com.iwittenberg.advent.util.mutableGridFromInput
import com.iwittenberg.advent.util.set

abstract class Day11Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<MutableIntGrid, Long>(2021, 11, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): MutableIntGrid {
        return mutableGridFromInput(rawInput, Char::digitToInt)
    }

    override fun getTestCaseInput(): List<String> {
        return listOf("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())
    }

    fun charge(input: MutableIntGrid, point: Point2d, neighbors: List<Point2d>, flashed: MutableSet<Point2d>) {
        if (point in flashed) {
            return
        }

        input.increment(point)
        if (input.valueAt(point) > 9) {
            flashed.add(point)
            neighbors.forEach {
                charge(input, it, input.generateAllNeighbors(it), flashed)
            }
        }
    }
}


@RunThis
class Day11Part1 : Day11Part(1, listOf(1656), 1667) {
    override fun solve(input: MutableIntGrid): Long {
        val points = input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }

        return (0 until 100).sumOf { _ ->
            val flashed = mutableSetOf<Point2d>()
            points.forEach { charge(input, it, input.generateAllNeighbors(it), flashed) }
            flashed.forEach { input.set(it, 0) }

            flashed.size.toLong()
        }
    }
}

@RunThis
class Day11Part2 : Day11Part(2, listOf(195), 488) {
    override fun solve(input: MutableIntGrid): Long {
        val points = input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }

        var i = 0
        while (true) {
            i++
            val flashed = mutableSetOf<Point2d>()
            points.forEach { charge(input, it, input.generateAllNeighbors(it), flashed) }
            flashed.forEach { input.set(it, 0) }
            if (flashed.size == points.size) return i.toLong()
        }
    }

}