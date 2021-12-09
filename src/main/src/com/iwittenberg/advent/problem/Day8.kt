package com.iwittenberg.advent.problem

typealias SegmentOutput = Pair<List<String>, List<String>>

abstract class Day8Part(part: Int, testCaseAnswer: Int, previouslySubmittedAnswer: Int?) :
    ProblemPart<List<SegmentOutput>, Int>(2021, 8, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): List<SegmentOutput> {
        return rawInput.map { line ->
            val split = line.split(" | ")
            split[0].split(" ") to split[1].split(" ")
        }
    }

    override fun getTestCaseInput(): String = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()

}

@RunThis
class Day8Part1 : Day8Part(1, 26, 514) {
    override fun solve(input: List<SegmentOutput>): Int {
        var count = 0
        input.forEach { line ->
            line.second.forEach {
                when (it.length) {
                    2 -> count++
                    3 -> count++
                    4 -> count++
                    7 -> count++
                }
            }
        }

        return count
    }
}

@RunThis
class Day8Part2 : Day8Part(2, 61229, 1012272) {
    override fun solve(input: List<SegmentOutput>): Int {
        var total = 0
        input.forEach { line ->
            
            var oneStr = ""
            var sevenStr = ""
            var fourStr = ""
            var eightStr = ""
            val twoThreeOrFive = mutableListOf<String>()
            val zeroSixOrNine = mutableListOf<String>()
            line.first.forEach {
                when (it.length) {
                    2 -> oneStr = it
                    3 -> sevenStr = it
                    4 -> fourStr = it
                    5 -> twoThreeOrFive.add(it)
                    6 -> zeroSixOrNine.add(it)
                    7 -> eightStr = it
                }
            }

            val sixStr = zeroSixOrNine.single { !it.containsAll(oneStr) }
            val threeStr = twoThreeOrFive.single { it.containsAll(oneStr) }
            val nineStr = zeroSixOrNine.single { it.containsAll(threeStr) }
            val fiveStr = twoThreeOrFive.single { sixStr.containsAll(it) }
            val twoStr = twoThreeOrFive.single { it != threeStr && it != fiveStr }
            val zeroStr = zeroSixOrNine.single { it != sixStr && it != nineStr }

            val mapping = mapOf(
                zeroStr.toSortedSet() to 0,
                oneStr.toSortedSet() to 1,
                twoStr.toSortedSet() to 2,
                threeStr.toSortedSet() to 3,
                fourStr.toSortedSet() to 4,
                fiveStr.toSortedSet() to 5,
                sixStr.toSortedSet() to 6,
                sevenStr.toSortedSet() to 7,
                eightStr.toSortedSet() to 8,
                nineStr.toSortedSet() to 9
            )

            total += line.second.map { mapping[it.toSortedSet()] }.joinToString("").toInt()
        }
        return total

    }

    private fun String.containsAll(chars: CharSequence): Boolean {
        return chars.map { this.contains(it) }.all { it }
    }
}

