package com.iwittenberg.advent.problem.year2020

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.util.Grid
import com.iwittenberg.advent.util.Point2d
import com.iwittenberg.advent.util.gridFromInput
import com.iwittenberg.advent.util.valueAt

abstract class Day3Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<Grid<Char>, Long>(2020, 3, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Grid<Char> {
        return gridFromInput(rawInput) { it }
    }

    override fun getTestCaseInput(): String {
        return """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
        """.trimIndent()
    }

    fun checkForTrees(input: Grid<Char>, slope: Pair<Int, Int>): Long {
        var trees = 0
        var point = Point2d(0, 0)
        while (point.first < input.size) {
            if (input.valueAt(point, wrapRight = true) == '#') {
                trees++
            }

            point = Point2d(point.first + slope.first, point.second + slope.second)
        }
        return trees.toLong()
    }
}

@RunThis
class Day3Part1 : Day3Part(1, 7, 242) {
    override fun solve(input: Grid<Char>): Long {
        return checkForTrees(input, 1 to 3)
    }
}

@RunThis
class Day3Part2 : Day3Part(2, 336, 2265549792) {
    override fun solve(input: Grid<Char>): Long {
        return checkForTrees(input, 1 to 1) *
                checkForTrees(input, 1 to 3) *
                checkForTrees(input, 1 to 5) *
                checkForTrees(input, 1 to 7) *
                checkForTrees(input, 2 to 1)
    }


}