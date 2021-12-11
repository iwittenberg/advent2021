package com.iwittenberg.advent

import com.iwittenberg.advent.problem.PartResult
import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RealCase
import com.iwittenberg.advent.problem.RealCaseFailure
import com.iwittenberg.advent.problem.RealCaseUnknownSolution
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.problem.TestCaseFailure
import org.reflections.Reflections
import kotlin.math.max
import kotlin.reflect.full.createInstance

fun main() {
    val reflections = Reflections("com.iwittenberg.advent")
    val classes = reflections.getSubTypesOf(ProblemPart::class.java)
        .filter { it.isAnnotationPresent(RunThis::class.java) }

    val onlyThis =
        classes.filter { (it.annotations.find { annotation -> annotation is RunThis } as RunThis).andOnlyThis }
    val toProcess = when (onlyThis.size) {
        0 -> {
            classes.map { it.kotlin.createInstance() as ProblemPart<*, *> }
                .map { it to it.run() }
                .groupBy { it.second.javaClass.kotlin }
        }
        1 -> {
            val problem = onlyThis.single().kotlin.createInstance() as ProblemPart<*, *>
            val result = problem.run()
            mapOf(result.javaClass.kotlin to listOf(problem to result))
        }
        else -> throw IllegalStateException("Multiple classes tagged with RunThis(andOnlyThis=true), $onlyThis")
    }
    toProcess.forEach { processGroup(it.value) }
}

private fun processGroup(group: List<Pair<ProblemPart<*, *>, PartResult<*>>>) {
    val values = group.sortedWith(naturalOrder)
    when (group[0].second.javaClass) {
        TestCaseFailure::class.java -> processTestCaseFailures(values)
        RealCaseFailure::class.java -> processRealCaseFailures(values)
        RealCaseUnknownSolution::class.java -> processRealCaseUnknownSolution(values)
        RealCase::class.java -> processRealCase(values)
    }
}


val naturalOrder = Comparator<Pair<ProblemPart<*, *>, PartResult<*>>> { a, b ->
    val aSimpleName = a.first.javaClass.simpleName
    val bSimpleName = b.first.javaClass.simpleName
    val aDay = aSimpleName.substring(3, aSimpleName.indexOf('P')).toInt()
    val bDay = bSimpleName.substring(3, bSimpleName.indexOf('P')).toInt()
    val aPart = aSimpleName[aSimpleName.length - 1].digitToInt()
    val bPart = bSimpleName[bSimpleName.length - 1].digitToInt()
    when {
        aDay < bDay -> -1
        aDay > bDay -> 1
        else -> when {
            aPart < bPart -> -1
            aPart > bPart -> 1
            else -> 0
        }
    }
}

fun processTestCaseFailures(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>) {
    val header = "Test Case Failures:"
    val colHeaders = listOf("Problem", "Expected", "Got")
    val rows = value.map {
        val result = it.second as TestCaseFailure
        listOf(
            "${it.first.year} Day ${it.first.day} Part ${it.first.part}",
            "${result.testCaseExpectation}",
            "${result.testCaseResult}"
        )
    }
    printTable(header, colHeaders, rows)
}

fun processRealCaseFailures(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>) {
    val header = "Previously successful problems now failing:"
    val colHeaders = listOf("Problem", "Expected", "Got")
    val rows = value.map {
        val result = it.second as RealCaseFailure
        listOf(
            "${it.first.year} Day ${it.first.day} Part ${it.first.part}",
            "${result.expectedResult}",
            "${result.realResult}"
        )
    }
    printTable(header, colHeaders, rows)
}

fun processRealCaseUnknownSolution(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>) {
    val header = "Problems that pass test case - no known solution to real case provided:"
    val colHeaders = listOf("Problem", "Solution", "Runtime")
    val rows = value.map {
        val result = it.second as RealCaseUnknownSolution
        listOf(
            "${it.first.year} Day ${it.first.day} Part ${it.first.part}",
            "${result.realResult}",
            "${result.realRuntime / 1e6} ms"
        )
    }
    printTable(header, colHeaders, rows)
}

fun processRealCase(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>) {
    val header = "Solutions that successfully produce a result that matches the previously correct value"
    val colHeaders = listOf("Problem", "Solution", "Runtime")
    val rows = value.map {
        val result = it.second as RealCase
        listOf(
            "${it.first.year} Day ${it.first.day} Part ${it.first.part}",
            "${result.realResult}",
            "${result.realRuntime / 1e6} ms"
        )
    }
    printTable(header, colHeaders, rows)
}

private fun printTable(header: String, columnHeaders: List<String>, rows: List<List<String>>) {
    val totalColumnHeaderSize = columnHeaders.map { it.length }.sumOf { it }
    val totalSizeOfLongestRow = rows.map { row -> row.map { it.length }.sumOf { it } }.maxOf { it } + 10

    val columnHeaderPad = columnHeaders.indices.map { it ->
        max(
            columnHeaders[it].length,
            rows.map { it2 -> it2[it].length }.maxOf { it })
    }

    val maxTotalWidth = listOf(totalColumnHeaderSize, totalSizeOfLongestRow).maxOf { it } + 8
    val colSpaceRemaining = maxTotalWidth - columnHeaderPad.sum()
    val addToEach = (colSpaceRemaining / columnHeaders.size)
    val addExtraToFirst = colSpaceRemaining % columnHeaders.size

    val formatStr = columnHeaderPad.mapIndexed { index, it ->
        when (index) {
            0 -> it + addExtraToFirst + addToEach
            else -> it + addToEach
        }
    }.joinToString(" | ", "| ", " |\n") { "%-${it}s" }

    val separatorStr = columnHeaderPad.mapIndexed { index, it ->
        when (index) {
            0 -> it + addExtraToFirst + addToEach + 2
            columnHeaderPad.lastIndex -> it + addToEach + 2
            else -> it + addToEach + 2
        }
    }.joinToString("+", "+", "+") { "-".repeat(it) }

    println(header)
    println(separatorStr)
    System.out.format(formatStr, *columnHeaders.toTypedArray())
    println(separatorStr)
    rows.forEach { System.out.format(formatStr, *it.toTypedArray()) }
    println(separatorStr)
}