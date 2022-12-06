package days.day05

import readInput
import java.util.*

val input = readInput(day = 5)
fun String.remove(remove: String) = replace(remove, "")
fun <T> Stack<T>.popOrNull(): T? = if (empty()) null else pop()

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): String {
    val (stacks, moves) = read()
    moves.forEach { (times, from, to) ->
        (1..times).forEach {
            val item = stacks[from - 1].popOrNull() ?: return@forEach
            stacks[to - 1].push(item)
        }
    }
    return stacks.joinToString(separator = "") {
        it.pop().toString()
    }
}

private fun part2(): String {
    val (stacks, moves) = read()
    moves.forEach { (times, from, to) ->
        val itemToMove = Stack<Char>()
        (1..times).forEach {
            val item = stacks[from - 1].popOrNull() ?: return@forEach
            itemToMove.push(item)
        }
        while (!itemToMove.empty()) {
            val item = itemToMove.pop()
            stacks[to - 1].push(item)
        }
    }
    return stacks.joinToString(separator = "") {
        it.pop().toString()
    }
}

private fun read(): Pair<List<Stack<Char>>, List<Move>> {
    var stackNumber: Int
    val (stackList, moveList) = input.partition {
        ".*\\[[A-Z]{1}\\].*".toRegex().matches(it)
    }.let {
        stackNumber = it.second[0].trim().last().toString().toInt()
        it.copy(second = it.second.drop(2))
    }
    val stack = List(stackNumber) { Stack<Char>() }
    stackList
        .reversed()
        .map {
            var count = 0
            it.chunked(4) {
                if (it.isNotBlank()) {
                    stack[count].push(it[1])
                }
                count++
            }
        }
    val operators = moveList.map {
        val raw = it
            .remove("move ")
            .remove("from ")
            .remove("to ")
            .split(" ")
        Move(raw[0].toInt(), raw[1].toInt(), raw[2].toInt())
    }
    return stack to operators
}
