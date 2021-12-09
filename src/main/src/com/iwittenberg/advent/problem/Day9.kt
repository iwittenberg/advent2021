package com.iwittenberg.advent.problem

typealias Grid = List<List<Int>>
private fun Grid.getOrMax(i: Int, j: Int) = this.getOrNull(i)?.getOrNull(j) ?: 9

abstract class Day9Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<Grid, Int>(2021, 9, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Grid {
        return rawInput.map { row -> row.map { col -> col.digitToInt() } }
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

    protected fun generateNeighbors(i: Int, j: Int) = listOf((i to j - 1), (i to j + 1), (i - 1 to j), (i + 1 to j))
}

@RunThis
class Day9Part1 : Day9Part(1, 15) {
    override fun solve(input: Grid): Int {
        val list = mutableListOf<Int>()
        (input.indices).forEach { row ->
            (input[row].indices).forEach { col ->
                val neighbors = generateNeighbors(row, col)
                val current = input[row][col]

                if (neighbors.all { current < input.getOrMax(it.first, it.second) }) {
                    list.add(current)
                }
            }
        }
        return list.sumOf { it + 1 }
    }
}

@RunThis
class Day9Part2 : Day9Part(2, 1134) {
    override fun solve(input: Grid): Int {
        val basins = mutableListOf<Int>()
        val visited = mutableListOf<Pair<Int, Int>>()
        (input.indices).forEach { row ->
            (input[row].indices).forEach { col ->
                val flow = flow(input, row, col, visited)
                if (flow > 0) basins.add(flow)
            }
        }

        basins.sortDescending()
        return basins[0] * basins[1] * basins[2]
    }

    private fun flow(input: Grid, row: Int, col: Int, visited: MutableList<Pair<Int, Int>>): Int {
        if (visited.contains(row to col)) {
            return 0
        }

        visited.add(row to col)

        if (input.getOrMax(row, col) == 9) {
            return 0
        }

        val pairs = generateNeighbors(row, col).filter { !visited.contains(it) }.filter { input.getOrMax(row, col) != 9 }
        return 1 + pairs.sumOf { flow(input, it.first, it.second, visited) }
    }
}