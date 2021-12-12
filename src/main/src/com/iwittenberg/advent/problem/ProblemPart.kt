package com.iwittenberg.advent.problem

abstract class ProblemPart<T, A>(
    val year: Int,
    val day: Int,
    val part: Int,
    val expectedTestCaseResult: List<A>,
    val expectedRealAnswer: A?,
    private val usePartSpecificInput: Boolean = false
) {
    protected abstract fun solve(input: T): A
    protected abstract fun convertToInputType(rawInput: List<String>): T
    protected abstract fun getTestCaseInput(): List<String>
    protected open fun getAdditionalTestCaseInputs() = emptyList<String>()
    protected open fun getAdditionalTestCaseResults() = emptyList<A>()

    fun solveSample() = getTestCaseInput().map { it.parseAsInput() }.map { convertToInputType(it) }.map { solve(it) }
    fun solveReal() = solve(convertToInputType(getRealInput()))

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