package com.iwittenberg.advent

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

fun main() {
    val reflections = Reflections("com.iwittenberg.advent")
    val classes = reflections.getTypesAnnotatedWith(RunThis::class.java)
    classes.sortedBy { it.typeName }.forEach {
        assert(ProblemPart::class.java.isAssignableFrom(it))

        val instance: ProblemPart<*, *> = it.kotlin.createInstance() as ProblemPart<*, *>
        instance.run()
    }
}