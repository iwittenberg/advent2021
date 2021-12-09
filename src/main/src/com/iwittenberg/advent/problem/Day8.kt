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

            val a = getA(oneStr, sevenStr)
            val cOrF = oneStr
            val bOrD = getBOrD(cOrF, fourStr)
            val eOrG = getEOrD(a, cOrF, bOrD, eightStr)
            val threeStr = getThreeStr(cOrF, twoThreeOrFive)
            val (e, g) = getEAndG(eOrG, threeStr)
            val twoStr = getTwoStr(e, g, twoThreeOrFive)
            val fiveStr = twoThreeOrFive.filter { it != twoStr }.single { it != threeStr }
            val (c, d) = getCAndD(cOrF, bOrD, twoStr)
            val b = bOrD.single { it != d }
            val f = cOrF.single { it != c }
            val zeroStr = getZeroStr(a, b, c, e, f, g, zeroSixOrNine)
            val sixStr = getSixStr(a, b, d, e, f, g, zeroSixOrNine)
            val nineStr = zeroSixOrNine.filter { it != zeroStr }.single { it != sixStr }

            val mapping = mapOf(
                zeroStr.toSortedSet() to 0,
                oneStr.toSortedSet()to 1,
                twoStr.toSortedSet()to 2,
                threeStr.toSortedSet()to 3,
                fourStr.toSortedSet()to 4,
                fiveStr.toSortedSet()to 5,
                sixStr.toSortedSet()to 6,
                sevenStr.toSortedSet()to 7,
                eightStr.toSortedSet()to 8,
                nineStr.toSortedSet()to 9
            )

            total += line.second.map { mapping[it.toSortedSet()] }.joinToString("").toInt()
        }
        return total
    }

    private fun getA(oneStr: String, sevenStr: String) =  sevenStr.single { !oneStr.contains(it) }
    private fun getBOrD(cOrF: String, fourStr: String) = fourStr.filter { !cOrF.contains(it) }
    private fun getEOrD(a: Char, cOrF: String, bOrD: String, eightStr: String) = eightStr.filter { it != a }.filter { !cOrF.contains(it) }.filter { !bOrD.contains(it) }
    private fun getEAndG(eOrG: String, threeStr: String) = eOrG.single { threeStr.contains(it) } to eOrG.single { !threeStr.contains(it) }
    private fun getTwoStr(e: Char, g: Char, twoThreeOrFive: List<String>) = twoThreeOrFive.single { it.contains(e) && it.contains(g) }
    private fun getCAndD(cOrF: String, bOrD: String, twoStr: String) =  cOrF.single { twoStr.contains(it) } to bOrD.single { twoStr.contains(it) }

    private fun getThreeStr(cOrF: String, twoThreeOrFive: List<String>): String {
        return twoThreeOrFive.single { number ->
            cOrF.map { number.contains(it) }.count { it } == 2
        }
    }

    private fun getZeroStr(a: Char, b: Char, c: Char, e: Char, f: Char, g: Char, zeroSixOrNine: List<String>): String {
        return zeroSixOrNine.single {
            it.contains(a) && it.contains(b) && it.contains(c) && it.contains(e) && it.contains(
                f
            ) && it.contains(g)
        }
    }

    private fun getSixStr(a: Char, b: Char, d: Char, e: Char, f: Char, g: Char, zeroSixOrNine: List<String>): String {
        return zeroSixOrNine.single {
            it.contains(a) && it.contains(b) && it.contains(d) && it.contains(e) && it.contains(
                f
            ) && it.contains(g)
        }
    }

}