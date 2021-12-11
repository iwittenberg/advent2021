package com.iwittenberg.advent

import com.iwittenberg.advent.problem.PartResult
import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RealCase
import com.iwittenberg.advent.problem.RealCaseFailure
import com.iwittenberg.advent.problem.RealCaseUnknownSolution
import com.iwittenberg.advent.problem.RunThis
import com.iwittenberg.advent.problem.TestCaseFailure
import org.reflections.Reflections
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
    toProcess.forEach { println(processGroup(it.value)) }
}

private fun processGroup(group: List<Pair<ProblemPart<*, *>, PartResult<*>>>): Table {
    val values = group.sortedWith(ProblemPart.naturalOrder)
    return when (group[0].second.javaClass) {
        TestCaseFailure::class.java -> processTestCaseFailures(values)
        RealCaseFailure::class.java -> processRealCaseFailures(values)
        RealCaseUnknownSolution::class.java -> processRealCaseUnknownSolution(values)
        RealCase::class.java -> processRealCase(values)
        else -> throw RuntimeException("Unhandled clas type in processGroup")
    }
}

fun processTestCaseFailures(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>): Table {
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
    return Table(header, colHeaders, rows)
}

fun processRealCaseFailures(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>): Table {
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
    return Table(header, colHeaders, rows)
}

fun processRealCaseUnknownSolution(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>): Table {
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
    return Table(header, colHeaders, rows)
}

fun processRealCase(value: List<Pair<ProblemPart<*, *>, PartResult<*>>>): Table {
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
    return Table(header, colHeaders, rows)
}