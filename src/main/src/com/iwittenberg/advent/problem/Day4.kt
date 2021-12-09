package com.iwittenberg.advent.problem

abstract class Day4Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int? = null) :
    ProblemPart<Pair<List<Int>, List<Board>>, Int>(2021, 4, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = rawInput[0].split(",").map { it.toInt() }

        val boardCount = (rawInput.size - 1) / 6
        val boards = mutableListOf<Board>()
        (0 until boardCount).forEach { boardIndex ->
            val startLine = 2 + (boardIndex) + (boardIndex * 5)

            val boardNumbers = mutableListOf<Int>()
            (startLine until startLine + 5).forEach { rowIndex ->
                val rowNumbers = rawInput[rowIndex].split("\\s+".toRegex()).map { it.toInt() }
                boardNumbers.addAll(rowNumbers)
            }
            boards.add(Board(boardNumbers))
        }

        return Pair(numbers, boards)
    }

    override fun getTestCaseInput(): String {
        return """
        7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

        22 13 17 11  0
        8  2 23  4 24
        21  9 14 16  7
        6 10  3 18  5
        1 12 20 15 19

        3 15  0  2 22
        9 18 13 17  5
        19  8  7 25 23
        20 11 10 24  4
        14 21 16 12  6

        14 21 17 24  4
        10 16 15  9 19
        18  8 23 26 20
        22 11 13  6  5
        2  0 12  3  7
        """
    }
}

@RunThis
class Day4Part1 : Day4Part(1, 4512, 69579) {
    override fun solve(input: Pair<List<Int>, List<Board>>): Int {
        val picks = input.first
        val boards = input.second

        picks.forEach { pick ->
            boards.forEach { board ->
                if (board.mark(pick)) {
                    return board.score() * pick
                }
            }
        }
        throw IllegalArgumentException("A winner should have been found")
    }
}

@RunThis
class Day4Part2 : Day4Part(2, 1924, 14877) {
    override fun solve(input: Pair<List<Int>, List<Board>>): Int {
        val picks = input.first
        val boards = input.second

        val winners = mutableMapOf<Int, Boolean>()
        picks.forEach { pick ->
            boards.forEachIndexed { index, board ->
                if (board.mark(pick)) {
                    winners[index] = true
                    if (winners.keys.size == boards.size) {
                        return board.score() * pick
                    }
                }
            }
        }
        throw IllegalArgumentException("A winner should have been found")
    }
}

data class BingoSpot(val number: Int, var marked: Boolean = false)

class Board(numbers: List<Int>) {
    private val spots: List<BingoSpot>
    private val lookUp: Map<Int, Int>

    init {
        spots = numbers.map { BingoSpot(it, false) }
        lookUp = numbers.mapIndexed { index, i -> i to index }.toMap()
    }

    fun mark(number: Int): Boolean {
        val index: Int = if (number in lookUp) lookUp[number]!! else return false
        spots[index].marked = true

        val row = index / 5
        val col = index % 5

        var markedRows = 0
        var markedCols = 0
        (0 until 5).forEach {
            val rowIndex = it + (5 * row)
            val colIndex = col + (it * 5)

            markedRows += if (spots[rowIndex].marked) 1 else 0
            markedCols += if (spots[colIndex].marked) 1 else 0
        }
        return markedRows == 5 || markedCols == 5
    }

    fun score() = spots.sumOf { if (it.marked) 0 else it.number }
    override fun toString() = spots.joinToString { it.number.toString() }
}