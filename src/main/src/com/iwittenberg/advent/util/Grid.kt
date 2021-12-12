package com.iwittenberg.advent.util

typealias Point2d = Pair<Int, Int>
typealias Grid<A> = List<List<A>>
typealias MutableGrid<A> = List<MutableList<A>>

typealias IntGrid = Grid<Int>
typealias MutableIntGrid = MutableGrid<Int>

fun <A> gridFromInput(rawInput: List<String>, valueConversionFunc: (Char) -> A): Grid<A> {
    return rawInput.map { row -> row.map { col -> valueConversionFunc.invoke(col) }.toList() }
}

fun <A> mutableGridFromInput(rawInput: List<String>, valueConversionFunc: (Char) -> A): MutableGrid<A> {
    return rawInput.map { row -> row.map { col -> valueConversionFunc.invoke(col) }.toMutableList() }
}

fun Grid<*>.generateOrthogonalNeighbors(point: Point2d): List<Point2d> {
    return listOf(
        (point.first to point.second - 1),
        (point.first to point.second + 1),
        (point.first - 1 to point.second),
        (point.first + 1 to point.second)
    ).filter { this.pointIsValid(it) }
}

fun Grid<*>.generateDiagonalNeighbors(point: Point2d): List<Point2d> {
    return listOf(
        (point.first - 1 to point.second - 1),
        (point.first + 1 to point.second - 1),
        (point.first - 1 to point.second + 1),
        (point.first + 1 to point.second + 1),
    ).filter { this.pointIsValid(it) }
}

fun Grid<*>.generateAllNeighbors(point: Point2d): List<Point2d> {
    return listOf(this.generateOrthogonalNeighbors(point), this.generateDiagonalNeighbors(point)).flatten()
}

private fun Grid<*>.pointIsValid(it: Point2d): Boolean {
    return it.first < this.size && it.first >= 0 &&
            it.second < this[0].size && it.second >= 0
}

fun MutableGrid<Int>.increment(point: Point2d) {
    this.add(point, 1)
}

fun MutableGrid<Int>.add(point: Point2d, amount: Int) {
    this[point.first][point.second] += amount
}

fun <A> MutableGrid<A>.set(point: Point2d, value: A) {
    this[point.first][point.second] = value
}

fun <A> Grid<A>.valueAt(
    point: Point2d,
    wrapLeft: Boolean = false,
    wrapRight: Boolean = false,
    wrapUp: Boolean = false,
    wrapDown: Boolean = false
): A {
    var first = point.first
    var second = point.second

    if (wrapLeft && second < this[0].size) {
        second %= this[0].size
    }

    if (wrapRight && second >= this[0].size) {
        second %= this[0].size
    }

    if (wrapUp && first < this.size) {
        first %= this.size
    }

    if (wrapDown && first >= this.size) {
        first %= this.size
    }

    return this[first][second]
}

fun Grid<*>.printable() = this.joinToString("\n")