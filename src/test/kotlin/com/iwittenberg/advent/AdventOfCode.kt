package com.iwittenberg.advent

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestMethodOrder
import org.reflections.Reflections
import kotlin.reflect.full.createInstance
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AdventOfCode {

    private val reflections = Reflections("com.iwittenberg.advent")
    private val classes = reflections.getSubTypesOf(ProblemPart::class.java).filter { it.isAnnotationPresent(RunThis::class.java) }
    private val onlyThis = classes.filter { (it.annotations.find { annotation -> annotation is RunThis } as RunThis).andOnlyThis }
    private val toRun = when (onlyThis.size) {
        1 -> listOf(onlyThis.single().kotlin.createInstance() as ProblemPart<*, *>)
        else -> classes.map { it.kotlin.createInstance() as ProblemPart<*, *> }
    }.sortedWith(ProblemPart.naturalOrder).groupBy { it.year to it.day }

    @TestFactory
    @Order(1)
    fun `sample case`(): List<DynamicContainer> {
        return toRun.map { group ->
            val tests = group.value.map {
                var testCaseResult: Any?
                val testCaseTime = measureNanoTime {
                    testCaseResult = it.solveSample()
                }
                DynamicTest.dynamicTest("Part ${it.part} - ${testCaseTime / 1e6}ms") {
                    assertEquals(it.expectedTestCaseResult, testCaseResult, "Sample case didn't match")
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
                var result: Any?
                val time = measureNanoTime {
                    result = it.solveReal()
                }

                DynamicTest.dynamicTest("Part ${it.part} - ${time / 1e6}ms") {
                    System.out.format(
                        "%-18s - %s\n",
                        "${it.year} Day ${it.day} Part ${it.part}",
                        "Got $result in ${time / 1e6}ms"
                    )
                    assertEquals(it.expectedRealAnswer, result, "Sample solution didn't match")
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
                var result: Any?
                val time = measureNanoTime {
                    result = it.solveReal()
                }

                DynamicTest.dynamicTest("Part ${it.part} - ${time / 1e6}ms") {
                    System.out.format(
                        "%-18s - %s\n",
                        "${it.year} Day ${it.day} Part ${it.part}",
                        "Got $result in ${time / 1e6}ms"
                    )
                    fail("No actual answer provided - if this is correct, consider updating the corresponding ProblemPart")
                }
            }

            if (tests.isEmpty()) {
                null
            } else {
                DynamicContainer.dynamicContainer("${group.key.first} Day ${group.key.second}", tests)
            }
        }.filterNot { it == null }.map { it!! }
    }
}