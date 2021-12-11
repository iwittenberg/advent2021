package com.iwittenberg.advent

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.util.getProblemPartsToRun
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestMethodOrder
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.fail

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AdventOfCode {

    private val toRun = getProblemPartsToRun()

    @TestFactory
    @Order(1)
    fun `sample case`(): List<DynamicContainer> {
        return toRun.map { group ->
            val tests = group.value.map {
                val (result, time, throwable) = runSolveFunc(it, ProblemPart<*, *>::solveSample)

                DynamicTest.dynamicTest("Part ${it.part} - ${time / 1e6}ms") {
                    failIfThrown(throwable)
                    assertEquals(it.expectedTestCaseResult, result, "Sample case didn't match")
                }
            }
            DynamicContainer.dynamicContainer("${group.key.first} Day ${group.key.second}", tests)
        }
    }

    @TestFactory
    @Order(2)
    fun `real case - regression`(): List<DynamicContainer> {
        return toRun.map { group ->
            val tests = group.value.filterNot { it.expectedRealAnswer == null }.map {
                val (result, time, throwable) = runSolveFunc(it)

                DynamicTest.dynamicTest("Part ${it.part} - ${time / 1e6}ms") {
                    failIfThrown(throwable)

                    System.out.format(
                        "%-18s - %s\n",
                        "${it.year} Day ${it.day} Part ${it.part}",
                        "Got $result in ${time / 1e6}ms"
                    )

                    assertEquals(it.expectedRealAnswer, result, "This problem has regressed and no longer matches the reported previously correct solution")
                }
            }
            DynamicContainer.dynamicContainer("${group.key.first} Day ${group.key.second}", tests)
        }
    }

    @TestFactory
    @Order(3)
    fun `real case - no known solution`(): List<DynamicContainer> {
        return toRun.map { group ->
            val tests = group.value.filter { it.expectedRealAnswer == null }.map {
                val (result, time, throwable) = runSolveFunc(it)

                DynamicTest.dynamicTest("Part ${it.part} - ${time / 1e6}ms") {
                    failIfThrown(throwable)

                    System.out.format(
                        "%-18s - %s\n",
                        "${it.year} Day ${it.day} Part ${it.part}",
                        "Got $result in ${time / 1e6}ms"
                    )

                    fail("No actual answer provided - if this is correct, consider updating the corresponding ProblemPart")
                }
            }

            when(tests.isNotEmpty()) {
                true -> DynamicContainer.dynamicContainer("${group.key.first} Day ${group.key.second}", tests)
                else -> null
            }
        }.filterNotNull().map { it }
    }

    private fun failIfThrown(t: Throwable?) {
        if (t != null) {
            fail("Test failed due to thrown exception, $t")
        }
    }

    private fun runSolveFunc(part: ProblemPart<*, *>, solveFunc: (ProblemPart<*, *>) -> Any? = ProblemPart<*, *>::solveReal): Triple<Any?, Long, Throwable?> {
        var result: Any? = null
        var throwable: Throwable? = null
        val time = measureNanoTime {
            try {
                result = solveFunc.invoke(part)
            } catch (t: Throwable) {
                throwable = t
            }
        }
        return Triple(result, time, throwable)
    }
}