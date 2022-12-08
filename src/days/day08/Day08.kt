package days.day08

import readInputList
import java.util.*

val input = readInputList(day = 8)

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int = input.indices.sumOf { i ->
    input[0].indices.count { j ->
        val left = (j - 1 downTo 0).map { i to it }
        val right = (j + 1 until input[0].length).map { i to it }
        val up = (i - 1 downTo 0).map { it to j }
        val down = (i + 1 until input.size).map { it to j }
        listOf(left, right, up, down).any { trees ->
            trees.all { (x, y) -> input[x][y] < input[i][j] }
        }
    }
}


private fun part2(): Int = input.indices.maxOf { i ->
    input[0].indices.maxOf { j ->
        val left = (j - 1 downTo 0).map { i to it }
        val right = (j + 1 until input[0].length).map { i to it }
        val up = (i - 1 downTo 0).map { it to j }
        val down = (i + 1 until input.size).map { it to j }
        listOf(left, right, up, down).map { trees ->
            minOf(trees.takeWhile { (x, y) -> input[x][y] < input[i][j] }.size + 1, trees.size)
        }.reduce(Int::times)
    }
}