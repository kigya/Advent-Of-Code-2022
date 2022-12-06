package days.day06

import readInput

val input = readInput(day = 6)

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    (4 until input.length).firstOrNull { input.substring(it - 4, it).toSet().size == 4 } ?: 0

private fun part2() =
    (14 until input.length).firstOrNull { input.substring(it - 14, it).toSet().size == 14 } ?: 0
