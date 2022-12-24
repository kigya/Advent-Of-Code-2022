package days.day24

import readInputList


typealias CharArray2 = Array<CharArray>

fun main() {
    day24Part(1)
    day24Part(2)
}

fun day24Part(part: Int) {
    val dayId = "24"
    val input = readInputList(24)
    val a = input.toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    data class B(val i: Int, val j: Int, val d: Int)

    val bs = a.mapIndexed2NotNull { i, j, c ->
        val d = when (c) {
            '>' -> 0
            'v' -> 1
            '<' -> 2
            '^' -> 3
            '#', '.' -> -1
            else -> error(a[i][j])
        }
        if (d >= 0) B(i, j, d) else null
    }
    var pc = HashSet<P2>()
    var pn = HashSet<P2>()
    var s = P2(0, 1)
    var f = P2(n - 1, m - 2)
    pc += s
    val totalPhases = if (part == 1) 0 else 2
    var phase = 0
    var t = 0
    while (phase < totalPhases || f !in pc) {
        if (f in pc) {
            phase++
            pc.clear()
            f = s.also { s = f }
            pc += s
        }
        val bn = bs.map { b ->
            P2(
                (b.i + di[b.d] * (t + 1) - 1).mod(n - 2) + 1,
                (b.j + dj[b.d] * (t + 1) - 1).mod(m - 2) + 1
            )
        }.toSet()
        for (p in pc) {
            for (d in 0..3) {
                val p1 = P2(p.i + di[d], p.j + dj[d])
                if (p1 in bn) continue
                if (p1.i in 0 until n && p1.j in 0 until m && a[p1.i][p1.j] != '#') {
                    pn += p1
                }
            }
            if (p !in bn) pn += p
        }
        pc = pn.also { pn = pc }
        pn.clear()
        t++
    }
    println("part$part = $t")
}

fun List<String>.toCharArray2() = Array(size) { get(it).toCharArray() }

fun CharArray2.size2(): P2 {
    val n = size
    val m = get(0).size
    for (i in 1 until n) require(get(i).size == m) { "Row $i has size ${get(i)}, but expected $m" }
    return P2(n, m)
}

inline fun CharArray2.forEachIndexed2(action: (i: Int, j: Int, c: Char) -> Unit) {
    for (i in indices) {
        val b = get(i)
        for (j in b.indices) {
            action(i, j, b[j])
        }
    }
}

inline fun <R> CharArray2.mapIndexed2NotNull(transform: (i: Int, j: Int, c: Char) -> R?): List<R> = buildList {
    forEachIndexed2 { i, j, c ->
        transform(i, j, c)?.let { add(it) }
    }
}

inline fun CharArray2.toListOfP2If(predicate: (c: Char) -> Boolean): List<P2> = buildList {
    forEachIndexed2 { i, j, c ->
        if (predicate(c)) add(P2(i, j))
    }
}

data class P2(val i: Int, val j: Int)

val RDLU_DIRS: Pair<IntArray, IntArray> = Pair(
    intArrayOf(0, 1, 0, -1),
    intArrayOf(1, 0, -1, 0)
)
