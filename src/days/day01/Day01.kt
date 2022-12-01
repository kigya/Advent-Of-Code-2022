package days.day01

import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int =
        input.fold(0 to 0) { (weight, maxWeight), line ->
            if (line.isNotEmpty()) {
                weight + line.toInt() to maxWeight
            } else {
                0 to max(weight, maxWeight)
            }
        }.second

    fun part2(input: List<String>): Int =
        input.fold(0 to mutableListOf<Int>()) { (weight, weights), line ->
            if (line.isNotEmpty()) {
                weight + line.toInt() to weights
            } else {
                weights.add(weight)
                0 to weights
            }
        }.second.sorted().takeLast(3).sum()

    val input = readInput(day = 1)
    println(part1(input))
    println(part2(input))
}
