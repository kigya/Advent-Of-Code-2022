package days.day03

import readInput

fun main() {
    fun Char.priority() = (('a'..'z') + ('A'..'Z')).indexOf(this) + 1

    fun part1(input: List<String>): Int = input.sumOf { s ->
        val a = s.take(s.length / 2).toSet()
        val b = s.drop(s.length / 2).toSet()
        a.intersect(b).first().priority()
    }

    fun part2(input: List<String>): Int = input
        .map { it.toSet() }
        .chunked(3)
        .sumOf { (a, b, c) ->
            a.intersect(b).intersect(c).first().priority()
        }

    val input = readInput(day = 3)
    println(part1(input))
    println(part2(input))
}
