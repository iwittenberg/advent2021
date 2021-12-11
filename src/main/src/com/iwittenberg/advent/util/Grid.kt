package com.iwittenberg.advent.util

typealias IntGrid = List<MutableList<Int>>

fun IntGrid.generateOrthogonalNeighbors(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        (point.first to point.second - 1),
        (point.first to point.second + 1),
        (point.first - 1 to point.second),
        (point.first + 1 to point.second)
    ).filter { this.pointIsInRange(it) }
}

fun IntGrid.generateAllNeighbors(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        (point.first to point.second - 1),
        (point.first to point.second + 1),
        (point.first - 1 to point.second),
        (point.first + 1 to point.second),
        (point.first - 1 to point.second - 1),
        (point.first + 1 to point.second - 1),
        (point.first - 1 to point.second + 1),
        (point.first + 1 to point.second + 1),
    ).filter { this.pointIsInRange(it) }
}

private fun IntGrid.pointIsInRange(it: Pair<Int, Int>): Boolean {
    return it.first < this.size && it.first >= 0 &&
            it.second < this[0].size && it.second >= 0
}

fun IntGrid.increment(point: Pair<Int, Int>) {
    this[point.first][point.second]++
}

fun IntGrid.reset(point: Pair<Int, Int>) {
    this[point.first][point.second] = 0
}

fun IntGrid.gridValue(point: Pair<Int, Int>): Int {
    return this[point.first][point.second]
}