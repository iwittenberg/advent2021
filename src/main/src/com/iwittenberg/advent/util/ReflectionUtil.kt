package com.iwittenberg.advent.util

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

private val reflections = Reflections("com.iwittenberg.advent")
private val classes = reflections.getSubTypesOf(ProblemPart::class.java).filter { it.isAnnotationPresent(RunThis::class.java) }
private val onlyThis = classes.filter { (it.annotations.find { annotation -> annotation is RunThis } as RunThis).andOnlyThis }

fun getProblemPartsToRun(): Map<Pair<Int, Int>, List<ProblemPart<*, *>>> {
    return when (onlyThis.size) {
        1 -> listOf(onlyThis.single().kotlin.createInstance() as ProblemPart<*, *>)
        else -> classes.map { it.kotlin.createInstance() as ProblemPart<*, *> }
    }.sortedWith(ProblemPart.naturalOrder).groupBy { it.year to it.day }
}