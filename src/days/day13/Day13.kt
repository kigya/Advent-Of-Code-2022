package days.day13

import readInput
import readInputList

fun main() {
    part1().let(::println)
    part2().let(::println)
}

private val regex = """(,)|(?=\[)|(?=])|(?<=\[)|(?<=])""".toRegex()
val parsed = readInputList(13).filter(String::isNotBlank).map(::lineToNode)

private fun lineToNode(line: String) = buildList<MutableList<Node>> {
    add(mutableListOf())
    line.split(regex).filter(String::isNotBlank).forEach { t ->
        when (t) {
            "[" -> add(mutableListOf())
            "]" -> removeLast().also { last().add(Holder(*it.toTypedArray())) }
            else -> last().add(Value(t.toInt()))
        }
    }
}[0][0]


fun part1() = parsed.chunked(2).mapIndexed { index, (a, b) -> if (a < b) index + 1 else 0 }.sum()

fun part2(): Any {
    val packet1 = Holder(Holder(Value(2)))
    val packet2 = Holder(Holder(Value(6)))
    val list = (parsed + packet1 + packet2).sorted()
    return (1 + list.indexOf(packet1)) * (1 + list.indexOf(packet2))
}

sealed interface Node : Comparable<Node>

class Holder(vararg val nodes: Node) : Node {
    override fun compareTo(other: Node): Int = when (other) {
        is Value -> compareTo(Holder(other))
        is Holder -> nodes.zip(other.nodes, Node::compareTo).firstOrNull { it != 0 }
            ?: nodes.size.compareTo(other.nodes.size)
    }
}

class Value(val int: Int) : Node {
    override fun compareTo(other: Node): Int = when (other) {
        is Value -> int.compareTo(other.int)
        is Holder -> Holder(this).compareTo(other)
    }
}