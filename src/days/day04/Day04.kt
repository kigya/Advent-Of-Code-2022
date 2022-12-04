package days.day04

import readInput

fun main() {

    fun part1(input: List<String>): Int = input.count { line ->
        val (s1, e1, s2, e2) = line.split(Regex("[,-]")).map { it.toInt() }
        (s1 <= s2 && e1 >= e2) || (s2 <= s1 && e2 >= e1)
    }

    fun part2(input: List<String>): Int = input.count { line ->
        val (s1, e1, s2, e2) = line.split(Regex("[,-]")).map { it.toInt() }
        s1 <= e2 && e1 >= s2
    }

    val input = readInput(day = 4)
    println(part1(input))
    println(part2(input))
}
