package days.day15

import readInputList
import kotlin.math.max

fun main() {
    fun getBlockedOnLine(
        sensors: List<List<Int>>,
        shouldCountUsed: Boolean,
        whichRow: Int,
        whichLimit: Int? = null
    ): Pair<Int, Int> {
        val blockedRangesOnRowEvents: MutableList<Pair<Int, Int>> = mutableListOf()
        sensors.forEach { (x1, y1, x2, y2) ->
            run {
                val dist = kotlin.math.abs(x1 - x2) + kotlin.math.abs(y1 - y2)
                val distOnThatRow = dist - kotlin.math.abs(whichRow - y1)
                if (distOnThatRow >= 0) {
                    blockedRangesOnRowEvents.add(x1 - distOnThatRow to 1)
                    blockedRangesOnRowEvents.add(x1 + distOnThatRow + 1 to -1)
                }
                if (y2 == whichRow && shouldCountUsed) {
                    blockedRangesOnRowEvents.add(x2 to 0)
                }

            }
        }
        if (whichLimit != null) {
            blockedRangesOnRowEvents.add(whichLimit to -1000)
        }
        blockedRangesOnRowEvents.sortWith(compareBy({ it.first }, { it.second }))
        var answer = 0
        var currentOpened = 0
        var previous = if (whichLimit == null) blockedRangesOnRowEvents[0].first else 0
        var firstEmpty = -1
        blockedRangesOnRowEvents.forEach { (pos, type) ->
            run {
                if (currentOpened > 0) {
                    answer += max(0, pos - previous - if (type == 0) 1 else 0)
                } else {
                    if (pos > previous && whichLimit != null && previous < whichLimit) {
                        firstEmpty = previous
                    }
                }
                previous = max(pos, previous)
                currentOpened += type
            }
        }
        return answer to firstEmpty
    }

    fun part1(input: List<String>, whichRow: Int): Int {
        val sensors = input.map {
            it.split("=", ",", ":").mapNotNull { it2 -> it2.toIntOrNull() }
        }
        return getBlockedOnLine(sensors, true, whichRow).first
    }

    fun part2(input: List<String>, highestCoordinate: Int): Long {
        val mySensors = input.map {
            it.split("=", ",", ":").mapNotNull { it2 -> it2.toIntOrNull() }
        }
        (0..highestCoordinate).forEach {
            val (amount, candidate) = getBlockedOnLine(mySensors, false, it, highestCoordinate)
            if (highestCoordinate - amount > 0) {
                return 4000000L * candidate + it
            }
        }
        return -1L
    }

    val input = readInputList(15)
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}