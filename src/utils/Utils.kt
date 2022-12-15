import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.collections.*

/**
 * Reads lines from the given input txt file.
 */
fun readInputList(day: Int): List<String> {
    val dayStringValue = day.toString().padStart(2, '0')
    return File("src/days/day${dayStringValue}", "Day${dayStringValue}.txt")
        .readLines()
}

fun readInput(day: Int): String {
    val dayStringValue = day.toString().padStart(2, '0')
    return File("src/days/day${dayStringValue}", "Day${dayStringValue}.txt")
        .readText()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

val Int.hPos: HPos get() = HPos(this)
val Int.vPos: VPos get() = VPos(this)

@JvmInline
value class HPos(val intValue: Int) : Comparable<HPos> {

    override fun compareTo(other: HPos): Int =
        intValue.compareTo(other.intValue)

    operator fun div(value: Int): HPos =
        HPos(intValue - value)

    operator fun minus(value: Int): HPos =
        HPos(intValue - value)

    operator fun plus(value: Int): HPos =
        HPos(intValue + value)

    operator fun rem(value: Int): HPos =
        HPos(intValue % value)

}

@JvmInline
value class VPos(val intValue: Int) : Comparable<VPos> {

    override fun compareTo(other: VPos): Int =
        intValue.compareTo(other.intValue)

    operator fun div(value: Int): VPos =
        VPos(intValue / value)

    operator fun minus(value: Int): VPos =
        VPos(intValue - value)

    operator fun plus(value: Int): VPos =
        VPos(intValue + value)

    operator fun rem(value: Int): VPos =
        VPos(intValue % value)

}

data class GridPos(val x: HPos, val y: VPos) : Comparable<GridPos> {

    override fun compareTo(other: GridPos): Int =
        compareBy(GridPos::y).thenComparing(compareBy(GridPos::x)).compare(this, other)

}

fun <E> List<List<E>>.toGrid(): Grid<E> = Grid(
    grid = flatten(),
    height = size,
    width = map { it.size }.toSet().single()
)

fun <S, T> Grid<S>.map(transform: (pos: GridPos, value: S) -> T): Grid<T> = Grid(
    grid = positions.map { pos -> transform(pos, this[pos]) },
    width = width,
    height = height
)

fun <S, T> Grid<S>.map(transform: (x: HPos, y: VPos, value: S) -> T): Grid<T> = Grid(
    grid = horizontalIndices.flatMap { x -> verticalIndices.map { y -> transform(x, y, this[x, y]) } },
    width = width,
    height = height
)

fun <E> Grid<E>.toMap(): Map<GridPos, E> = buildMap {
    positions.forEach { pos -> this[pos] = this@toMap[pos] }
}

class Grid<E>(
    private val grid: List<E>,
    val width: Int,
    val height: Int
) : Iterable<E> {

    init {
        require(grid.size == width * height)
    }

    constructor(width: Int, height: Int, init: (GridPos) -> E) :
            this(ArrayList<E>(width * height).apply {
                for (i in 0 until (width * height)) {
                    val y = i / width
                    val x = i % width

                    add(init(GridPos(x.hPos, y.vPos)))
                }
            }, width, height)

    val horizontalIndices: List<HPos> by lazy { (0 until width).map { HPos(it) } }
    val verticalIndices: List<VPos> by lazy { (0 until height).map { VPos(it) } }

    val positions: SortedSet<GridPos> by lazy {
        verticalIndices.flatMap { y ->
            horizontalIndices.map { x ->
                GridPos(
                    x,
                    y
                )
            }
        }.toSortedSet()
    }
    val size: Int get() = width * height

    fun beam(pos: GridPos, shift: (GridPos) -> GridPos?): List<GridPos> = buildList {
        var cursor = shift(pos)

        while (cursor != null) {
            add(cursor)
            cursor = shift(cursor)
        }
    }

    operator fun contains(pos: GridPos): Boolean =
        pos.x in horizontalIndices && pos.y in verticalIndices

    operator fun get(pos: GridPos): E =
        this[pos.x, pos.y]

    operator fun get(x: HPos, y: VPos): E {
        require(x.intValue in 0 until width) { "HPos out of grid bounds: ${x.intValue} is not in [0, ${width - 1}]" }
        require(y.intValue in 0 until height) { "VPos out of grid bounds: ${y.intValue} is not in [0, ${height - 1}]" }

        return grid[y.intValue * width + x.intValue]
    }

    fun getOrDefault(x: HPos, y: VPos, default: E): E =
        if (x in horizontalIndices && y in verticalIndices) this[x, y] else default

    fun getAdjacentPositions(pos: GridPos, includeDiagonals: Boolean = false): List<GridPos> = buildList {
        pos.copy(x = pos.x - 1).also { if (it in this@Grid) add(it) }
        pos.copy(x = pos.x + 1).also { if (it in this@Grid) add(it) }
        pos.copy(y = pos.y - 1).also { if (it in this@Grid) add(it) }
        pos.copy(y = pos.y + 1).also { if (it in this@Grid) add(it) }

        if (includeDiagonals) {
            pos.copy(x = pos.x - 1, y = pos.y - 1).also { if (it in this@Grid) add(it) }
            pos.copy(x = pos.x - 1, y = pos.y + 1).also { if (it in this@Grid) add(it) }
            pos.copy(x = pos.x + 1, y = pos.y - 1).also { if (it in this@Grid) add(it) }
            pos.copy(x = pos.x + 1, y = pos.y + 1).also { if (it in this@Grid) add(it) }
        }
    }

    fun shiftLeft(pos: GridPos): GridPos? =
        (pos.x - 1).let { x -> if (x in horizontalIndices) pos.copy(x = x) else null }

    fun shiftLeftWrapped(pos: GridPos): GridPos =
        pos.copy(x = (pos.x - 1).let { x -> if (x in horizontalIndices) x else horizontalIndices.last() })

    fun shiftRight(pos: GridPos): GridPos? =
        (pos.x + 1).let { x -> if (x in horizontalIndices) pos.copy(x = x) else null }

    fun shiftRightWrapped(pos: GridPos): GridPos =
        pos.copy(x = (pos.x + 1).let { x -> if (x in horizontalIndices) x else horizontalIndices.first() })

    fun shiftUp(pos: GridPos): GridPos? =
        (pos.y - 1).let { y -> if (y in verticalIndices) pos.copy(y = y) else null }

    fun shiftUpWrapped(pos: GridPos): GridPos =
        pos.copy(y = (pos.y - 1).let { y -> if (y in verticalIndices) y else verticalIndices.last() })

    fun shiftDown(pos: GridPos): GridPos? =
        (pos.y + 1).let { y -> if (y in verticalIndices) pos.copy(y = y) else null }

    fun shiftDownWrapped(pos: GridPos): GridPos =
        pos.copy(y = (pos.y + 1).let { y -> if (y in verticalIndices) y else verticalIndices.first() })

    override fun iterator(): Iterator<E> =
        grid.iterator()

    override fun toString(): String =
        grid.chunked(width).joinToString(separator = System.lineSeparator()) { it.joinToString(separator = "") }

    fun toString(transform: (E) -> String): String =
        grid.chunked(width)
            .joinToString(separator = System.lineSeparator()) { it.joinToString(separator = "") { transform(it) } }

}
