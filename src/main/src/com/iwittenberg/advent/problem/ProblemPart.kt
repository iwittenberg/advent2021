package com.iwittenberg.advent.problem

import kotlin.system.measureNanoTime

sealed interface PartResult<A>
class TestCaseFailure<A>(
    val testCaseResult: A,
    val testCaseExpectation: A,
    val testCaseRuntime: Long) : PartResult<A>
class RealCaseFailure<A>(
    val testCaseResult: A,
    val testCaseRuntime: Long,
    val realResult: A,
    val expectedResult: A,
    val realRuntime: Long) : PartResult<A>
class RealCaseUnknownSolution<A>(
    val testCaseResult: A,
    val testCaseRuntime: Long,
    val realResult: A,
    val realRuntime: Long) : PartResult<A>
class RealCase<A>(
    val testCaseResult: A,
    val testCaseRuntime: Long,
    val realResult: A,
    val realRuntime: Long) : PartResult<A>

abstract class ProblemPart<T, A>(
    val year: Int,
    val day: Int,
    val part: Int,
    private val expectedTestCaseResult: A,
    private val expectedRealAnswer: A?,
    private val usePartSpecificInput: Boolean = false
) {
    protected abstract fun solve(input: T): A
    protected abstract fun convertToInputType(rawInput: List<String>): T
    protected abstract fun getTestCaseInput(): String

    fun run(): PartResult<A> {
        val sampleValues = convertToInputType(getSampleInput())

        var testCaseResult: A
        val testCaseTime = measureNanoTime {
            testCaseResult = solve(sampleValues)
        }

        if (testCaseResult != expectedTestCaseResult) {
            return TestCaseFailure(testCaseResult, expectedTestCaseResult, testCaseTime)
        }

        val values = convertToInputType(getRealInput())
        val result: A
        val time = measureNanoTime {
            result = solve(values)
        }
        if (expectedRealAnswer != null) {
            if (expectedRealAnswer != result) {
                return RealCaseFailure(testCaseResult, testCaseTime, result, expectedRealAnswer, time)
            } else {
                return RealCase(testCaseResult, testCaseTime, result, time)
            }
        }

        return RealCaseUnknownSolution(testCaseResult, testCaseTime, result, time)
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