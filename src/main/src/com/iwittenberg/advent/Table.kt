package com.iwittenberg.advent

import kotlin.math.max

class Table(
    private val header: String,
    private val columnHeaders: List<String>,
    private val rows: List<List<String>>
) {
    override fun toString(): String {
        val totalColumnHeaderSize = columnHeaders.map { it.length }.sumOf { it }
        val totalSizeOfLongestRow = rows.map { row -> row.map { it.length }.sumOf { it } }.maxOf { it } + 10

        val columnHeaderPad = columnHeaders.indices.map { it ->
            max(
                columnHeaders[it].length,
                rows.map { it2 -> it2[it].length }.maxOf { it })
        }

        val maxTotalWidth = listOf(totalColumnHeaderSize, totalSizeOfLongestRow).maxOf { it } + 8
        val colSpaceRemaining = maxTotalWidth - columnHeaderPad.sum()
        val addToEach = (colSpaceRemaining / columnHeaders.size)
        val addExtraToFirst = colSpaceRemaining % columnHeaders.size

        val formatStr = columnHeaderPad.mapIndexed { index, it ->
            when (index) {
                0 -> it + addExtraToFirst + addToEach
                else -> it + addToEach
            }
        }.joinToString(" | ", "| ", " |\n") { "%-${it}s" }

        val separatorStr = columnHeaderPad.mapIndexed { index, it ->
            when (index) {
                0 -> it + addExtraToFirst + addToEach + 2
                columnHeaderPad.lastIndex -> it + addToEach + 2
                else -> it + addToEach + 2
            }
        }.joinToString("+", "+", "+\n") { "-".repeat(it) }

        return buildString {
            val data = rows.joinToString("", separatorStr, separatorStr) { row -> String.format(formatStr, *row.toTypedArray())}

            append(header).append("\n")
                .append(separatorStr)
                .append(String.format(formatStr, *columnHeaders.toTypedArray()))
                .append(data)
        }
    }
}