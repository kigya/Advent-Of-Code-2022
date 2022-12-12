package days.day12

import readInputList

val input = readInputList(day = 12)

fun bfs(testInput: List<String>, sy: Int, sx: Int): Int {
    val input = testInput.map { it.replace('S', 'a').replace('E', 'z') }

    val dist = Array(input.size) { IntArray(input.first().length) { 100000 } }
    dist[sy][sx] = 0

    val visited = mutableSetOf<Pair<Int, Int>>()
    val toVisit = mutableListOf(sy to sx)

    val nOffsets = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    while (toVisit.isNotEmpty()) {
        val (y, x) = toVisit.removeFirst()

        nOffsets
            .map { (dy, dx) -> (y + dy) to (x + dx) }
            .forEach { (ny, nx) ->
                val neighbor = input.getOrNull(ny)?.getOrNull(nx) ?: return@forEach
                if (neighbor - input[y][x] <= 1) {
                    dist[ny][nx] = minOf(dist[ny][nx], dist[y][x] + 1)

                    if ((ny to nx) !in visited && (ny to nx) !in toVisit) {
                        toVisit += (ny to nx)
                    }
                }
            }

        visited += (y to x)
    }

    val ty = testInput.indexOfFirst { it.contains('E') }
    val tx = testInput[ty].indexOf('E')
    return dist[ty][tx]
}

fun part1(testInput: List<String>): Int {
    val sy = testInput.indexOfFirst { it.contains('S') }
    val sx = testInput[sy].indexOf('S')
    return bfs(testInput = testInput, sy = sy, sx = sx)
}

fun part2(testInput: List<String>): Int {
    return testInput.indices.minOf { index ->
        bfs(testInput = testInput, sy = index, sx = 0)
    }
}


fun main() {
    println(part1(input))
    println(part2(input))
}
