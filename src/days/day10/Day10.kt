package days.day10

import readInputList

val input = readInputList(day = 10)

fun main() {
    println(part1())
    println(part2())
}

fun part1(): Int {
    val arr = mutableListOf(0, 1)
    input.forEach {
        val tokens = it.split(" ")
        arr += arr.last()
        if (tokens[0] == "addx")
            arr += arr.last() + tokens[1].toInt()
    }
    return (20..220 step 40).sumOf { it * arr[it] }
}

fun part2() {
    var x = 1
    var i = 0
    val pixels = mutableListOf<Char>()
    input.forEach {
        val tokens = it.split(" ")
        pixels += if (i++ % 40 in (x - 1)..(x + 1)) '#' else '.'
        if (tokens[0] == "addx") {
            pixels += if (i++ % 40 in (x - 1)..(x + 1)) '#' else '.'
            x += tokens[1].toInt()
        }
    }
    pixels.chunked(40).forEach { row ->
        println(row.joinToString(" "))
    }
}
