package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.util.Point2d
import com.iwittenberg.advent.util.mutableGridFromPoints
import com.iwittenberg.advent.util.pointsFromInput
import com.iwittenberg.advent.util.printable
import kotlin.math.min

abstract class Day13Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<Pair<List<Point2d>, List<Pair<Char, Int>>>, Long>(
        2021,
        13,
        part,
        testCaseAnswer,
        previouslySubmittedAnswer
    ) {

    override fun convertToInputType(rawInput: List<String>): Pair<List<Point2d>, List<Pair<Char, Int>>> {
        val splitPoint = rawInput.indexOf("")
        val points = pointsFromInput(rawInput.take(splitPoint))
        val folds = rawInput.takeLast(rawInput.size - (splitPoint + 1)).map { line ->
            val (axis, number) = line.split(" ")[2].split("=")
            (axis[0] to number.toInt())
        }
        return points to folds
    }

    override fun getTestCaseInput(): List<String> {
        return listOf(
            """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()
        )
    }

    protected fun fold(points: Set<Point2d>, fold: Pair<Char, Int>): Set<Point2d> {
        return points.map { point ->
            val axis = fold.first
            val number = fold.second

            when(axis) {
                'x' -> min(point.first, 2 * number - point.first) to point.second
                'y' -> point.first to min(point.second, 2 * number - point.second)
                else -> throw IllegalArgumentException("Invalid axis parameter")
            }
        }.toSet()
    }
}

@RunThis
class Day13Part1 : Day13Part(1, listOf(17), 638) {
    override fun solve(input: Pair<List<Point2d>, List<Pair<Char, Int>>>): Long {
        val firstFold = input.second[0]
        return fold(input.first.toSet(), firstFold).size.toLong()
    }
}

@RunThis
class Day13Part2 : Day13Part(2, listOf(-680322658), -635245186) {
    override fun solve(input: Pair<List<Point2d>, List<Pair<Char, Int>>>): Long {
        val final = input.second.fold(input.first.toSet()) { acc, foldPoint -> fold(acc, foldPoint) }
        val grid = mutableGridFromPoints(final, '#', ' ')

        // Technically the answer is CJCKBAPB for the final answer but rather than mapping to the actual characters,
        // just check against the hashcode to ensure no regression
        // println(grid.printable())
        return grid.hashCode().toLong()
    }
}