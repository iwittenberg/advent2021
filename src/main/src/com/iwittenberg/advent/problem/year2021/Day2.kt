package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis

typealias Movement = Pair<Direction, Int>
enum class Direction {
    FORWARD, DOWN, UP
}

abstract class Day2Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<List<Movement>, Int>(2021, 2, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<Movement> {
        return rawInput.map { line ->
            val split = line.split(" ")
            Movement(Direction.valueOf(split[0].uppercase()), split[1].toInt())
        }
    }

    override fun getTestCaseInput(): String {
        return """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """
    }
}

@RunThis
class Day2Part1 : Day2Part(1, 150, 1936494) {
    override fun solve(input: List<Movement>): Int {
        var horizontalPosition = 0;
        var depth = 0;

        input.forEach { movement ->
            when (movement.first) {
                Direction.FORWARD -> horizontalPosition += movement.second
                Direction.DOWN -> depth += movement.second
                Direction.UP -> depth -= movement.second
            }
        }

        return horizontalPosition * depth
    }
}

@RunThis
class Day2Part2 : Day2Part(2, 900, 1997106066) {
    override fun solve(input: List<Movement>): Int {
        var horPos = 0;
        var depth = 0;
        var aim = 0;

        input.forEach { movement ->
            when (movement.first) {
                Direction.FORWARD -> {
                    horPos += movement.second
                    depth += aim * movement.second
                }
                Direction.DOWN -> aim += movement.second
                Direction.UP -> aim -= movement.second
            }
        }

        return horPos * depth
    }
}