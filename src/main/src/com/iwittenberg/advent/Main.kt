package com.iwittenberg.advent

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

fun main() {
    val reflections = Reflections("com.iwittenberg.advent")
    val classes = reflections.getSubTypesOf(ProblemPart::class.java)
    classes.filter { it.isAnnotationPresent(RunThis::class.java) }
        .sortedBy { it.typeName }
        .forEach {
            val instance = it.kotlin.createInstance() as ProblemPart<*, *>
            instance.run()
        }
}