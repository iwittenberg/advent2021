package com.iwittenberg.advent.problem.year2021

import com.iwittenberg.advent.problem.ProblemPart
import com.iwittenberg.advent.problem.RunThis
import java.lang.IllegalArgumentException

abstract class Day16Part(part: Int, testCaseAnswer: List<Long>, previouslySubmittedAnswer: Long? = null) :
    ProblemPart<String, Long>(2021, 16, part, testCaseAnswer, previouslySubmittedAnswer) {

    override fun convertToInputType(rawInput: List<String>): String {
        return rawInput.single().map { char ->
            val hexVal = char.digitToInt(16)

            Integer.toBinaryString(hexVal).padStart(4, '0')
        }.joinToString("")
    }

    protected fun String.parsePacket(): Packet {
        val (packet, _) = this.parsePacketInner()
        return packet
    }

    private fun String.parsePacketInner(): Pair<Packet, String> {
        val version = this.substring(0, 3).toInt(2)
        val type = this.substring(3, 6).toInt(2)
        val remainingData = this.substring(6)
        return when (type) {
            4 -> {
                val (value, remaining) = parseLiteralValue(remainingData)
                LiteralPacket(version, type, value) to remaining
            }
            else -> {
                val (packets, remaining) = parseOperatorPacket(remainingData)
                OperatorPacket(version, type, packets) to remaining
            }
        }
    }

    private fun parseLiteralValue(remainingData: String): Pair<Long, String> {
        var value = ""
        var index = 0
        do {
            val cont = remainingData[index]
            val data = remainingData.substring(index + 1, index + 5)
            value += data
            index += 5
        } while (cont.digitToInt() == 1)
        return value.toLong(2) to remainingData.substring(index)
    }

    private fun parseOperatorPacket(remainingData: String): Pair<List<Packet>, String> {
        val lengthType = remainingData[0].digitToInt()
        val remaining = remainingData.substring(1)
        return if (lengthType == 0) {
            parseOperatorBitLengthPacket(remaining)
        } else {
            parseOperatorNumPackets(remaining)
        }
    }

    private fun parseOperatorBitLengthPacket(remainingData: String): Pair<List<Packet>, String> {
        val packets = mutableListOf<Packet>()
        val subpacketBits = remainingData.substring(0, 15).toInt(2)
        var remaining = remainingData.substring(15, 15 + subpacketBits)
        do {
            val result = remaining.parsePacketInner()
            packets.add(result.first)
            remaining = result.second
        } while (remaining.isNotEmpty())
        return packets to remainingData.substring(15 + subpacketBits)
    }

    private fun parseOperatorNumPackets(remainingData: String): Pair<List<Packet>, String> {
        val subpackets = remainingData.substring(0, 11).toInt(2)
        var remaining = remainingData.substring(11)
        val packets = (0 until subpackets).fold(mutableListOf<Packet>()) { acc, _ ->
            val result = remaining.parsePacketInner()
            remaining = result.second
            acc.apply { add(result.first) }
        }
        return packets to remaining
    }

    protected sealed class Packet(open val version: Int, open val id: Int) {
        abstract fun value(): Long
    }

    protected class LiteralPacket(override val version: Int, override val id: Int, private val value: Long) :
        Packet(version, id) {
        override fun value() = value
    }

    protected class OperatorPacket(override val version: Int, override val id: Int, val subPackets: List<Packet>) :
        Packet(version, id) {
        override fun value(): Long {
            return when (id) {
                0 -> subPackets.sumOf { it.value() }
                1 -> subPackets.fold(1L) { acc, it -> acc * it.value() }
                2 -> subPackets.minOf { it.value() }
                3 -> subPackets.maxOf { it.value() }
                5 -> if (subPackets[0].value() > subPackets[1].value()) 1 else 0
                6 -> if (subPackets[0].value() < subPackets[1].value()) 1 else 0
                7 -> if (subPackets[0].value() == subPackets[1].value()) 1 else 0
                else -> throw IllegalArgumentException("invalid operator packet id")
            }
        }
    }
}

@RunThis
class Day16Part1 : Day16Part(1, listOf(16, 12, 23, 31), 897) {

    override fun getTestCaseInput(): List<String> {
        return listOf(
            "8A004A801A8002F478",
            "620080001611562C8802118E34",
            "C0015000016115A2E0802F182340",
            "A0016C880162017C3686B18A3D4780"
        )
    }

    override fun solve(input: String): Long {
        return sumPacketVersions(listOf(input.parsePacket()))
    }

    private fun sumPacketVersions(packets: List<Packet>): Long {
        return packets.sumOf {
            when(it) {
                is LiteralPacket -> it.version.toLong()
                is OperatorPacket -> it.version + sumPacketVersions(it.subPackets)
            }
        }
    }

}

@RunThis
class Day16Part2 : Day16Part(2, listOf(3, 54, 7, 9, 1, 0, 0, 1), 9485076995911) {

    override fun getTestCaseInput(): List<String> {
        return listOf(
            "C200B40A82",
            "04005AC33890",
            "880086C3E88112",
            "CE00C43D881120",
            "D8005AC2A8F0",
            "F600BC2D8F",
            "9C005AC2F8F0",
            "9C0141080250320F1802104A08"
        )
    }

    override fun solve(input: String): Long {
        return input.parsePacket().value()
    }
}