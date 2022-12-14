package days.day14

import Grid
import hPos
import map
import readInputList
import vPos

fun main() {
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point)

    val lines = readInputList(day = 14).flatMap { path ->
        path.split(" -> ")
            .map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            }
            .zipWithNext()
            .map { (start, end) -> Line(start, end) }
    }

    var maxX = 0
    var maxY = 0

    for (line in lines) {
        maxX = maxOf(maxX, line.start.x, line.end.x)
        maxY = maxOf(maxY, line.start.y, line.end.y)
    }

    val width = maxOf(maxX, maxY + 502)
    val height = maxY + 3

    data class GridElement(
        var occupiance: Char = '.'
    )

    val grid = Grid(width, height) { GridElement() }

    for (line in lines) {
        if (line.start.y == line.end.y) {
            for (x in minOf(line.start.x, line.end.x)..maxOf(line.start.x, line.end.x)) {
                grid[x.hPos, line.start.y.vPos].occupiance = '#'
            }
        } else if (line.start.x == line.end.x) {
            for (y in minOf(line.start.y, line.end.y)..maxOf(line.start.y, line.end.y)) {
                grid[line.start.x.hPos, y.vPos].occupiance = '#'
            }
        } else {
            error("Unexpected line: $line")
        }
    }

    grid.horizontalIndices.forEach { x -> grid[x, grid.verticalIndices.last()].occupiance = '#' }

    fun fall(grid: Grid<GridElement>, startX: Int = 500, startY: Int = 0): Point {
        var x = startX.hPos
        var y = startY.vPos

        loop@ while (y.intValue < height - 2) {
            val ny = y + 1

            when {
                grid[x, ny].occupiance == '.' -> {}
                grid[x - 1, ny].occupiance == '.' -> x -= 1
                grid[x + 1, ny].occupiance == '.' -> x += 1
                else -> break@loop
            }

            y = ny
        }

        grid[x, y].occupiance = '+'
        return Point(x.intValue, y.intValue)
    }

    fun part1(): Int {
        val g = grid.map { _, it -> it.copy() }

        return generateSequence { fall(g) }
            .takeWhile { it.y < maxY }
            .count()
    }

    fun part2(): Int {
        val g = grid.map { _, it -> it.copy() }

        return generateSequence { fall(g) }
            .takeWhile { it.y != 0 }
            .count() + 1
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}