package com.iwittenberg.advent.util

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

private val reflections = Reflections("com.iwittenberg.advent")
private val classes =
    reflections.getSubTypesOf(ProblemPart::class.java).filter { it.isAnnotationPresent(RunThis::class.java) }
private val onlyThis =
    classes.filter { (it.annotations.find { annotation -> annotation is RunThis } as RunThis).andOnlyThis }

fun getProblemPartsToRun(): Map<Int, Map<Int, List<ProblemPart<*, *>>>> {
    return when (onlyThis.size) {
        0 -> classes.map { it.kotlin.createInstance() as ProblemPart<*, *> }
        1 -> listOf(onlyThis.single().kotlin.createInstance() as ProblemPart<*, *>)
        else -> throw IllegalStateException("Cannot have more than one RunThis(andOnlyThis=true) - $onlyThis")
    }.sortedWith(ProblemPart.naturalOrder)
        .groupBy { it.year }
        .mapValues { entry -> entry.value.groupBy { it.day } }
}