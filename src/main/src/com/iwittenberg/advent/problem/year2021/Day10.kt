package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import kotlin.collections.ArrayDeque

abstract class Day10Part(part: Int, testCaseAnswer: Long, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<List<String>, Long>(2021, 10, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>) = rawInput

    override fun getTestCaseInput(): String {
        return """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
        """.trimIndent()
    }

    private val mapping = mapOf(
        '[' to ']',
        '{' to '}',
        '(' to ')',
        '<' to '>'
    )

    /**
     * Sealed class implementation taken from https://todd.ginsberg.com/post/advent-of-code/2021/day10/
     * as a learning opportunity since at the time of writing, I haven't used these yet.
     */
    protected sealed interface ParseResult
    protected object Success : ParseResult
    protected class Corrupted(val actual: Char) : ParseResult
    protected class Incomplete(val remaining: List<Char>) : ParseResult

    protected fun parseLine(line: String): ParseResult {
        val stack = ArrayDeque<Char>()
        for (symbol in line) {
            when {
                symbol in mapping -> stack.addLast(symbol)
                mapping[stack.removeLast()] != symbol -> return Corrupted(symbol)
            }
        }
        return if (stack.isEmpty()) Success else Incomplete(stack.reversed().map { mapping[it]!! })
    }
}

@RunThis
class Day10Part1 : Day10Part(1, 26397, 288291) {

    private val score = mapOf<Char, Long>(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    override fun solve(input: List<String>): Long {
        return input.map { parseLine(it) }
            .filterIsInstance<Corrupted>()
            .sumOf { score[it.actual]!! }
    }
}

@RunThis
class Day10Part2 : Day10Part(2, 288957, 820045242) {

    private val score = mapOf<Char, Long>(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    override fun solve(input: List<String>): Long {
        return input.map { parseLine(it) }
            .filterIsInstance<Incomplete>()
            .map { incomplete -> incomplete.remaining.map{ score[it]!! }.reduce{ acc, score -> acc * 5 + score } }
            .sorted()
            .middle()
    }

    /**
     * Middle also taken from https://todd.ginsberg.com/post/advent-of-code/2021/day10/
     * Much nicer than saving input.map... to a val and then indexing
     */
    private fun <T> List<T>.middle() = this[lastIndex / 2]
}

/****************************************************

 Original Part 1 Implementation before finding the seal class implementation

    override fun solve(input: List<String>): Long {
        return input.map { corruptedChar(it) }
            .filterNot { it == null  }
            .sumOf { score[it]!! }
    }

    private fun corruptedChar(line: String): Char? {
        val stack = ArrayDeque<Char>()
        var retVal: Char? = null
        line.forEach { symbol ->
            when {
                symbol in mapping -> stack.addLast(symbol)
                mapping[stack.removeLast()] != symbol -> retVal = symbol
            }
        }
        return retVal
    }

 Original Part 2 Implementation before finding the seal class implementation

     override fun solve(input: List<String>): Long {
        return input.map { missingChars(it) }
            .filterNot { it.isEmpty() }
            .map { leftover -> leftover.map { score[it]!! }.reduce { acc, it -> acc * 5 + it } }
            .sorted()
            .midpoint()
    }

    private fun missingChars(line: String): List<Char> {
        val stack = ArrayDeque<Char>()
        for (symbol in line) {
            when {
                symbol in mapping -> stack.addLast(symbol)
                mapping[stack.removeLast()] != symbol -> return emptyList()
            }
        }
        return stack.reversed().map { mapping[it]!! }
    }

 ***************************************************/