package com.iwittenberg.advent.util

import java.util.PriorityQueue
import kotlin.math.abs

typealias Point2d = Pair<Int, Int>
typealias Grid<A> = List<List<A>>
typealias MutableGrid<A> = MutableList<MutableList<A>>

typealias IntGrid = Grid<Int>
typealias MutableIntGrid = MutableGrid<Int>

@Suppress("unused")
fun Grid<*>.printable() = this.joinToString("\n") { line -> line.joinToString("") }

fun pointsFromInput(rawInput: List<String>, separator: String = ","): List<Point2d> {
    return rawInput.map { point ->
        val (x, y) = point.split(separator)
        (x.toInt() to y.toInt())
    }
}

fun <A> gridFromInput(rawInput: List<String>, valueConversionFunc: (Char) -> A): Grid<A> {
    return rawInput.map { row -> row.map { col -> valueConversionFunc.invoke(col) }.toList() }
}

fun <A> mutableGridFromInput(rawInput: List<String>, valueConversionFunc: (Char) -> A): MutableGrid<A> {
    return rawInput.map { row -> row.map { col -> valueConversionFunc.invoke(col) }.toMutableList() }.toMutableList()
}

fun <A> mutableGridFromPoints(points: Collection<Point2d>, presentVal: A, defaultVal: A): MutableGrid<A> {
    val (x, y) = points.fold(0 to 0) { max, point ->
        val newX = if (point.first > max.first) point.first else max.first
        val newY = if (point.second > max.second) point.second else max.second
        newX to newY
    }

    return MutableList(y+1) { MutableList(x+1) { defaultVal } }
        .apply { points.forEach { this[it.second][it.first] = presentVal } }
}

fun <A> gridOf(rows: Int, cols: Int, init: (point: Point2d) -> A): Grid<A> {
    return List(rows) { row ->
        List(cols) { col ->
            init(row to col)
        }
    }
}

fun <A> mutableGridOf(rows: Int, cols: Int, init: (point: Point2d) -> A): MutableGrid<A> {
    return MutableList(rows) { row ->
        MutableList(cols) { col ->
            init(row to col)
        }
    }
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

fun <T, R> Grid<T>.mapPoints(transform: (T) -> R): Grid<R> {
    return this.mapPointsIndexed { _, point -> transform(point) }
}

fun <T, R> Grid<T>.mapPointsIndexed(transform: (Point2d, T) -> R): Grid<R> {
    return this.mapIndexed { row, values ->
        values.mapIndexed { col, value ->
            transform(row to col, value)
        }
    }
}


fun IntGrid.aStar(start: Point2d, end: Point2d, h: (Point2d, Point2d) -> Int = ::manhattanDistance): Pair<List<Point2d>, Int> {
    val score = mutableMapOf<Point2d, Int>()
    val prev = mutableMapOf<Point2d, Point2d>()
    val hscore = mutableMapOf<Point2d, Int>()
    val pq = PriorityQueue { p1: Pair<Point2d, Int>, p2: Pair<Point2d, Int> -> p1.second.compareTo(p2.second) }
    score[start] = 0
    hscore[start] = h(start, end)
    pq.add(start to hscore[start]!!)

    while (!pq.isEmpty()) {
        val head = pq.remove()
        if (head.second != hscore.getOrDefault(head.first, Int.MAX_VALUE)) continue
        if (head.first == end) break

        this.generateOrthogonalNeighbors(head.first).forEach {
            val newScore = score.getOrDefault(head.first, Int.MAX_VALUE) + this.valueAt(it)
            if (newScore < score.getOrDefault(it, Int.MAX_VALUE)) {
                score[it] = newScore
                prev[it] = head.first
                hscore[it] = newScore + h(it, end)
                pq.add(it to hscore[it]!!)
            }
        }
    }

    val path = mutableListOf<Point2d>()
    var current = end
    do {
        path.add(current)
        current = prev[current]!!
    } while (current != start)

    return path.reversed() to score[end]!!
}

fun manhattanDistance(first: Point2d, second: Point2d): Int {
    return abs(first.first - second.first) + abs(first.second + second.second)
}