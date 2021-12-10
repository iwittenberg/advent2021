package com.iwittenberg.advent.problem

import kotlin.system.measureNanoTime

abstract class ProblemPart<T, A>(
    private val year: Int,
    private val day: Int,
    private val part: Int,
    private val testCaseAnswer: A,
    private val submittedAnswer: A?,
    private val usePartSpecificInput: Boolean = false
) {
    protected abstract fun solve(input: T): A
    protected abstract fun convertToInputType(rawInput: List<String>): T
    protected abstract fun getTestCaseInput(): String

    fun run() {
        println("Year $year Day $day Part $part")

        val sampleValues = convertToInputType(getSampleInput())

        var sampleAnswer: A
        var time = measureNanoTime {
            sampleAnswer = solve(sampleValues)
        }
        try {
            assert(sampleAnswer == testCaseAnswer)
            println("Sample answers matched!")
            println("Total time: ${time / 1e6} ms")
        } catch (e: AssertionError) {
            println("Result from the sample set of $sampleAnswer didn't match $testCaseAnswer, skipping real run")
            println()
            return
        }

        val values = convertToInputType(getRealInput())
        val answer: A
        time = measureNanoTime {
            answer = solve(values)
        }
        println("Real input answer: $answer")
        if (submittedAnswer != null) {
            try {
                assert(submittedAnswer == answer)
                println("This matches the previously submitted answer!")
                println("Total time: ${time / 1e6} ms")
            } catch (e: AssertionError) {
                println("The newly computed answer of $answer didn't match $submittedAnswer from before!")
            }
        }

        println()
    }

    private fun getSampleInput(): List<String> {
        return getTestCaseInput().parseAsInput()
    }

    private fun getRealInput(): List<String> {
        var path = "/inputs/${year}/day${day}"
        if (usePartSpecificInput) {
            path += part.toString()
        }
        return path.resourceContents()?.parseAsInput() ?: throw IllegalArgumentException("Unable to find input file for $path")
    }

    private fun String.parseAsInput(): List<String> {
        return this.trimIndent().split("\n").map { it.trim() }
    }

    private fun String.resourceContents(): String? {
        return {}.javaClass.getResource(this)?.readText()
    }
}