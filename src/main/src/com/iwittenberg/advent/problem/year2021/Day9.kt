package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.util.Grid
import com.iwittenberg.advent.util.Point2d
import com.iwittenberg.advent.util.generateOrthogonalNeighbors
import com.iwittenberg.advent.util.gridValue

abstract class Day9Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<Grid, Int>(2021, 9, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Grid {
        return rawInput.map { row -> row.map { col -> col.digitToInt() }.toMutableList() }
    }

    override fun getTestCaseInput(): String {
        return """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
        """.trimIndent()
    }
}

@RunThis
class Day9Part1 : Day9Part(1, 15, 504) {
    override fun solve(input: Grid): Int {
        return input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }
            .filter { input.generateOrthogonalNeighbors(it).all { neighbor -> input[it.first][it.second] < input.gridValue(neighbor) } }
            .map { input.gridValue(it)}
            .sumOf { it + 1 }
    }
}

@RunThis
class Day9Part2 : Day9Part(2, 1134, 1558722) {
    override fun solve(input: Grid): Int {
        return input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }.asSequence()
            .filter { input.generateOrthogonalNeighbors(it).all { neighbor -> input[it.first][it.second] < input.gridValue(neighbor) } }
            .map { flow(input, it, mutableListOf()) }
            .filterNot { it == 0 }
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }
    }

    private fun flow(input: Grid, point: Point2d, visited: MutableList<Point2d>): Int {
        if (visited.contains(point)) {
            return 0
        }

        visited.add(point)

        if (input.gridValue(point) == 9) {
            return 0
        }

        return 1 + input.generateOrthogonalNeighbors(point)
            .filterNot { visited.contains(it) }
            .filterNot { input.gridValue(point) == 9 }
            .map { flow(input, it, visited) }
            .sumOf { it }
    }
}