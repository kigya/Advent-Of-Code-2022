package days.day20

import readInputList
import java.util.*

fun main() {

    fun solve(input: List<String>, part1: Boolean): Long {
        val q: Queue<Pair<Int, Long>> = LinkedList()
        for (i in input.indices)
            q.add(i to input[i].toLong().let { if (part1) it else it * 811589153 })
        repeat(if (part1) 1 else 10) {
            for (i in input.indices) {
                while (q.peek().first != i)
                    q.add(q.poll())
                val p = q.poll()
                val elementsToPop = p.second.mod(q.size)
                repeat(elementsToPop) { q.add(q.poll()) }
                q.add(p)
            }
        }
        val list = q.map { it.second }
        return (1..3).sumOf { list[(list.indexOf(0) + it * 1000) % list.size] }
    }

    val input = readInputList(20)
    println(solve(input, part1 = true))
    println(solve(input, part1 = false))
}
