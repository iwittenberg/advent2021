package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis

abstract class Day12Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<Day12Part.CaveStart, Long>(2021, 12, part, testCaseAnswer, previouslySubmittedAnswer) {

    sealed class Cave(open val name: String, open val openings: MutableList<Cave> = emptyList<Cave>().toMutableList())
    open class SmallCave(override val name: String) : Cave(name)
    class BigCave(override val name: String) : Cave(name)
    class CaveStart : SmallCave("Start")
    object CaveEnd : SmallCave("End")

    override fun convertToInputType(rawInput: List<String>): CaveStart {
        val initial = mutableMapOf<String, Cave>(
            "start" to CaveStart(),
            "end" to CaveEnd
        )

        return rawInput.fold(initial) { accumulated, line ->
            val caves = line.split("-")
            val cave1 = getCave(caves[0], accumulated)
            val cave2 = getCave(caves[1], accumulated)
            cave1.openings.add(cave2)
            cave2.openings.add(cave1)
            accumulated
        }["start"] as CaveStart
    }

    private fun getCave(rawCave: String, caveMap: MutableMap<String, Cave>): Cave {
        return when (rawCave) {
            "start" -> caveMap[rawCave]
            "end" -> caveMap[rawCave]
            else -> {
                if (rawCave.all { it.isUpperCase() }) caveMap.putIfAbsent(rawCave, BigCave(rawCave))
                if (rawCave.all { it.isLowerCase() }) caveMap.putIfAbsent(rawCave, SmallCave(rawCave))
                caveMap[rawCave]
            }
        }!!
    }

    override fun getTestCaseInput(): List<String> {
        return listOf(
            """
                start-A
                start-b
                A-c
                A-b
                b-d
                A-end
                b-end
            """.trimIndent(),
            """
                dc-end
                HN-start
                start-kj
                dc-start
                dc-HN
                LN-dc
                HN-end
                kj-sa
                kj-HN
                kj-dc
            """.trimIndent(),
            """
                fs-end
                he-DX
                fs-he
                start-DX
                pj-DX
                end-zg
                zg-sl
                zg-pj
                pj-he
                RW-he
                fs-DX
                pj-RW
                zg-RW
                start-pj
                he-WI
                zg-he
                pj-fs
                start-RW
            """.trimIndent(),
        )
    }

    protected fun traverse(
        cave: Cave,
        path: List<Cave>,
        visited: Set<Cave>,
        twiceAlready: Boolean = true
    ): List<MutableList<Cave>> {
        if (cave is SmallCave && cave is CaveEnd) return listOf(path.toMutableList().apply { add(cave) })

        val newTwiceAlready = twiceAlready || cave is SmallCave && cave in visited && !twiceAlready
        var newVisited = visited
        if (cave is SmallCave && cave !in visited) newVisited = newVisited.toMutableSet().apply { add(cave) }

        return cave.openings
            .asSequence()
            .filterNot { it is CaveStart || (it is SmallCave && it in visited && newTwiceAlready) }
            .map { traverse(it, path.toMutableList().apply { add(cave) }, newVisited, newTwiceAlready) }
            .flatten()
            .filter { it.last() is CaveEnd }
            .toList()
    }
}

@RunThis
class Day12Part1 : Day12Part(1, listOf(10, 19, 226), 3887) {
    override fun solve(input: CaveStart) = traverse(input, mutableListOf(), mutableSetOf()).size.toLong()
}

@RunThis
class Day12Part2 : Day12Part(2, listOf(36, 103, 3509), 104834) {
    override fun solve(input: CaveStart) = traverse(input, mutableListOf(), mutableSetOf(), false).size.toLong()
}
