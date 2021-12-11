package com.iwittenberg.advent.problem

import com.iwittenberg.advent.util.IntGrid
import com.iwittenberg.advent.util.generateAllNeighbors
import com.iwittenberg.advent.util.gridValue
import com.iwittenberg.advent.util.increment
import com.iwittenberg.advent.util.reset

abstract class Day11Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<IntGrid, Long>(2021, 11, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): IntGrid {
        return rawInput.map { row -> row.map { col -> col.digitToInt() }.toMutableList() }
    }

    override fun getTestCaseInput(): String {
        return """
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
        """.trimIndent()
    }

    fun charge(input: IntGrid, point: Pair<Int, Int>, neighbors: List<Pair<Int, Int>>, flashed: MutableSet<Pair<Int, Int>>) {
        if (point in flashed) {
            return
        }

        input.increment(point)
        if (input.gridValue(point) > 9) {
            flashed.add(point)
            neighbors.forEach {
                charge(input, it, input.generateAllNeighbors(it), flashed)
            }
        }
    }
}


@RunThis()
class Day11Part1 : Day11Part(1, 1656, 1667) {
    override fun solve(input: IntGrid): Long {
        val points = input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }

        return (0 until 100).sumOf { _ ->
            val flashed = mutableSetOf<Pair<Int, Int>>()
            points.forEach { charge(input, it, input.generateAllNeighbors(it), flashed) }
            flashed.forEach { input.reset(it) }
            
            flashed.size.toLong()
        }
    }
}

@RunThis
class Day11Part2 : Day11Part(2, 195, 488) {
    override fun solve(input: IntGrid): Long {
        val points = input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }

        var i = 0
        while (true) {
            i++
            val flashed = mutableSetOf<Pair<Int,Int>>()
            points.forEach { charge(input, it, input.generateAllNeighbors(it), flashed) }
            flashed.forEach { input.reset(it) }
            if (flashed.size == points.size) return i.toLong()
        }
    }

}