package com.iwittenberg.advent.problem

import com.iwittenberg.advent.util.IntGrid
import com.iwittenberg.advent.util.generateOrthogonalNeighbors

private fun IntGrid.getOrMax(point: Pair<Int, Int>) = this.getOrNull(point.first)?.getOrNull(point.second) ?: 9

abstract class Day9Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<IntGrid, Int>(2021, 9, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): IntGrid {
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
    override fun solve(input: IntGrid): Int {
        return input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }
            .filter { input.generateOrthogonalNeighbors(it).all { neighbor -> input[it.first][it.second] < input.getOrMax(neighbor) } }
            .map { input.getOrMax(it)}
            .sumOf { it + 1 }
    }
}

@RunThis
class Day9Part2 : Day9Part(2, 1134, 1558722) {
    override fun solve(input: IntGrid): Int {
        return input.flatMapIndexed { rowIdx, row -> row.indices.map { colIdx -> rowIdx to colIdx } }.asSequence()
            .filter { input.generateOrthogonalNeighbors(it).all { neighbor -> input[it.first][it.second] < input.getOrMax(neighbor) } }
            .map { flow(input, it, mutableListOf()) }
            .filterNot { it == 0 }
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }
    }

    private fun flow(input: IntGrid, point: Pair<Int, Int>, visited: MutableList<Pair<Int, Int>>): Int {
        if (visited.contains(point)) {
            return 0
        }

        visited.add(point)

        if (input.getOrMax(point) == 9) {
            return 0
        }

        return 1 + input.generateOrthogonalNeighbors(point)
            .filterNot { visited.contains(it) }
            .filterNot { input.getOrMax(point) == 9 }
            .map { flow(input, it, visited) }
            .sumOf { it }
    }
}