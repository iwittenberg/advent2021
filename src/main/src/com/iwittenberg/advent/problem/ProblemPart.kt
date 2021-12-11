package com.iwittenberg.advent.problem

abstract class ProblemPart<T, A>(
    val year: Int,
    val day: Int,
    val part: Int,
    val expectedTestCaseResult: A,
    val expectedRealAnswer: A?,
    private val usePartSpecificInput: Boolean = false
) {
    protected abstract fun solve(input: T): A
    protected abstract fun convertToInputType(rawInput: List<String>): T
    protected abstract fun getTestCaseInput(): String

    fun solveSample() = solve(convertToInputType(getSampleInput()))
    fun solveReal() = solve(convertToInputType(getRealInput()))

    private fun getSampleInput(): List<String> {
        return getTestCaseInput().parseAsInput()
    }

    private fun getRealInput(): List<String> {
        var path = "/inputs/${year}/day${day}"
        if (usePartSpecificInput) {
            path += part.toString()
        }
        return path.resourceContents()?.parseAsInput()
            ?: throw IllegalArgumentException("Unable to find input file for $path")
    }

    private fun String.parseAsInput(): List<String> {
        return this.trimIndent().split("\n").map { it.trim() }
    }

    private fun String.resourceContents(): String? {
        return {}.javaClass.getResource(this)?.readText()
    }

    companion object {
        val naturalOrder = compareBy<ProblemPart<*, *>> { it.year }.thenBy { it.day }.thenBy { it.part }
    }
}