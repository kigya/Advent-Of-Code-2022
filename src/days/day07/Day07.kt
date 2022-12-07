package days.day07

import readInput
import java.util.*

val input = readInput(day = 7)

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int {
    val (directories, subDirectories) = readCommands(input)
    return directories.map { (key, _) -> totalSize(key, directories, subDirectories) }
        .filter { it <= 100000 }.sum()
}

private fun part2(): Int {
    val (directories, subDirectories) = readCommands(input)
    val totalSize = totalSize(dir = "/", directories, subDirectories)
    val free = 70000000 - totalSize
    val required = 30000000 - free
    return directories.map { (key, _) -> totalSize(key, directories, subDirectories) }
        .filter { it >= required }.min()
}

private fun readCommands(text: String): Pair<MutableMap<String, Int>, MutableMap<String, MutableList<String>>> {
    val dirStack = Stack<String>()
    var dirPath = ""
    val directories = mutableMapOf<String, Int>()
    val subDirectories = mutableMapOf<String, MutableList<String>>()

    text.lines().forEach { line ->
        when {
            line.startsWith("\$ cd") -> {
                when {
                    line.endsWith("/") -> {
                        dirStack.clear()
                        dirStack.push("/")
                    }

                    line.endsWith("..") -> {
                        dirStack.pop()
                    }

                    else -> {
                        dirStack.push(line.substring(5))
                    }
                }
                dirPath = dirStack.joinToString("/")
                directories.putIfAbsent(dirPath, 0)
                subDirectories.putIfAbsent(dirPath, mutableListOf())
            }

            line.startsWith("\$ ls") -> Unit

            line.startsWith("dir") -> {
                subDirectories[dirPath]!!.add("$dirPath/${line.substring(4)}")
            }

            else -> {
                directories[dirPath] = (directories[dirPath] ?: 0) + line.substringBefore(' ').toInt()
            }
        }
    }
    return directories to subDirectories
}

fun totalSize(
    dir: String,
    directories: MutableMap<String, Int>,
    subDirectories: MutableMap<String, MutableList<String>>
): Int {
    return directories[dir]!! + subDirectories[dir]!!.sumOf { totalSize(it, directories, subDirectories) }
}
