package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.util.IntGrid
import com.iwittenberg.advent.util.Utils
import com.iwittenberg.advent.util.aStar
import com.iwittenberg.advent.util.gridFromInput
import com.iwittenberg.advent.util.gridOf
import com.iwittenberg.advent.util.valueAt

abstract class Day15Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<IntGrid, Long>(2021, 15, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): IntGrid {
        return gridFromInput(rawInput) { ch -> ch.digitToInt() }
    }

    override fun getTestCaseInput(): List<String> {
        return listOf(
            """
                1163751742
                1381373672
                2136511328
                3694931569
                7463417111
                1319128137
                1359912421
                3125421639
                1293138521
                2311944581
            """.trimIndent(),
        )
    }


    override fun solve(input: IntGrid) = input.aStar(0 to 0, input.size - 1 to input[0].size - 1).second.toLong()
}

@RunThis
class Day15Part1 : Day15Part(1, listOf(40), 363)

@RunThis
class Day15Part2 : Day15Part(2, listOf(315), 2835) {

    override fun convertToInputType(rawInput: List<String>): IntGrid {
        val input = super.convertToInputType(rawInput)
        val rows = input.size
        val cols = input[0].size

        return gridOf(rows * 5, cols * 5) { point ->
            val (offsetRows, originalRow) = Utils.divMod(point.first, rows)
            val (offsetCols, originalCol) = Utils.divMod(point.second, cols)
            val newValue = input.valueAt(originalRow to originalCol) + offsetRows + offsetCols

            if (newValue > 9) newValue % 9 else newValue
        }
    }
}