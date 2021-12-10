package com.iwittenberg.advent

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

fun main() {
    val reflections = Reflections("com.iwittenberg.advent")
    val classes = reflections.getSubTypesOf(ProblemPart::class.java)
        .filter { it.isAnnotationPresent(RunThis::class.java) }

    val onlyThis = classes.filter { (it.annotations.find { anno -> anno is RunThis } as RunThis).andOnlyThis }
    when (onlyThis.size) {
        0 -> {
            classes.sortedWith(naturalOrder)
                .forEach {
                    val instance = it.kotlin.createInstance() as ProblemPart<*, *>
                    instance.run()
                }
        }
        1 -> (onlyThis[0].kotlin.createInstance() as ProblemPart<*, *>).run()
        else -> {
            throw IllegalStateException("Multiple classes tagged with RunThis(andOnlyThis=true), $onlyThis")
        }
    }


}

val naturalOrder = Comparator<Class<out ProblemPart<*, *>>> { a, b ->
    val aDay = a.simpleName.substring(3, a.simpleName.indexOf('P'))
    val bDay = a.simpleName.substring(3, b.simpleName.indexOf('P'))
    val aPart = a.simpleName[a.simpleName.length - 1]
    val bPart = b.simpleName[b.simpleName.length - 1]
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