package com.iwittenberg.advent.util

typealias Point2d = Pair<Int, Int>
typealias Grid = List<List<Int>>
typealias MutableGrid = List<MutableList<Int>>

fun Grid.generateOrthogonalNeighbors(point: Point2d): List<Point2d> {
    return listOf(
        (point.first to point.second - 1),
        (point.first to point.second + 1),
        (point.first - 1 to point.second),
        (point.first + 1 to point.second)
    ).filter { this.pointIsValid(it) }
}

fun Grid.generateDiagonalNeighbors(point: Point2d): List<Point2d> {
    return listOf(
        (point.first - 1 to point.second - 1),
        (point.first + 1 to point.second - 1),
        (point.first - 1 to point.second + 1),
        (point.first + 1 to point.second + 1),
    ).filter { this.pointIsValid(it) }
}

fun Grid.generateAllNeighbors(point: Point2d): List<Point2d> {
    return listOf(this.generateOrthogonalNeighbors(point), this.generateDiagonalNeighbors(point)).flatten()
}

private fun Grid.pointIsValid(it: Point2d): Boolean {
    return it.first < this.size && it.first >= 0 &&
            it.second < this[0].size && it.second >= 0
}

fun MutableGrid.increment(point: Point2d) {
    this.add(point, 1)
}

fun MutableGrid.add(point: Point2d, amount: Int) {
    this[point.first][point.second] += amount
}

fun MutableGrid.set(point: Point2d, value: Int) {
    this[point.first][point.second] = value
}

fun Grid.gridValue(point: Point2d): Int {
    return this[point.first][point.second]
}