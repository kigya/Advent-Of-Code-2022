package days.day16

import readInputList
import kotlin.math.max
import kotlin.math.min

private class Valve(val flowRate: Int, val totalIndex: Int) {
    var open = false
    val tunnelsIndices = mutableListOf<Int>()
    var indexInNonZero = -1
}

private var SHIFT = 58

fun calculateHorribleKey(currentValveInd: Int, elephantValveInd: Int, mask: Int): Int {
    return min(currentValveInd, elephantValveInd) + SHIFT * max(
        currentValveInd,
        elephantValveInd
    ) + SHIFT * SHIFT * mask
}

fun main() {
    val lastParsed = mutableListOf<Valve>()
    fun parseValves(input: List<String>): Pair<Valve, Int> {
        lastParsed.clear()
        val nameToValve = mutableMapOf<String, Valve>()
        input.withIndex().forEach {
            val (_, name, _, _, withFlow) = it.value.split(" ")
            lastParsed.add(Valve(withFlow.split("=", ";")[1].toInt(), it.index))
            nameToValve[name] = lastParsed.last()
        }
        input.forEach {
            val (_, name) = it.split(" ")
            val leadsTo = it.split(", ").map { it2 -> it2.takeLast(2) }
            nameToValve[name]?.tunnelsIndices?.addAll(leadsTo.mapNotNull { it2 -> nameToValve[it2]?.totalIndex })
                ?: error("Valve disappeared?")
        }
        var nonZeroAmount = 0
        nameToValve.forEach {
            if (it.value.flowRate != 0) {
                it.value.indexInNonZero = nonZeroAmount++
            }
        }
        SHIFT = (input.size + 1)
        return (nameToValve["AA"] ?: error("No starting valve?")) to nonZeroAmount
    }

    val memory: MutableMap<Pair<Pair<Int, Int>, Int>, Int> = mutableMapOf()
    fun walkExponentially(
        currentValve: Valve, mask: Int, currentFlow: Int = 0, timeLeft: Int = 30
    ): Int {
        if (timeLeft == 0) return 0
        if ((currentValve.totalIndex to timeLeft) to mask in memory) {
            return memory[(currentValve.totalIndex to timeLeft) to mask]!! + currentFlow // We have just checked it
        }
        var maxAchievedAnswerAfterUs = currentFlow * (timeLeft - 1) // To stay here
        if (!currentValve.open && currentValve.flowRate != 0) {
            currentValve.open = true
            maxAchievedAnswerAfterUs = max(
                maxAchievedAnswerAfterUs, walkExponentially(
                    currentValve, (mask or (1 shl currentValve.indexInNonZero)), /*currentPressure + currentFlow,*/
                    currentFlow + currentValve.flowRate, timeLeft - 1
                )
            )
            currentValve.open = false
        }
        currentValve.tunnelsIndices.forEach {
            maxAchievedAnswerAfterUs = max(
                maxAchievedAnswerAfterUs, walkExponentially(
                    lastParsed[it], mask, /*currentPressure + currentFlow, */
                    currentFlow, timeLeft - 1
                )
            )
        }
        memory[(currentValve.totalIndex to timeLeft) to mask] =
            max(maxAchievedAnswerAfterUs, memory[(currentValve.totalIndex to timeLeft) to mask] ?: 0)
        return maxAchievedAnswerAfterUs + currentFlow
    }

    val memoryTimed: MutableList<MutableList<Int>> = mutableListOf(mutableListOf(), mutableListOf()) // XD XD XD

    fun calculateMaskFlows(nonZeroAmount: Int): List<Int> {
        val nonZeroIndices = lastParsed.filter { it.flowRate != 0 }.map { it.totalIndex }
        val answer = MutableList(1 shl nonZeroAmount) { 0 }
        for (mask in 0 until (1 shl nonZeroAmount)) {
            var sum = 0
            for (i in 0 until nonZeroAmount) {
                if (((1 shl i) and mask) != 0) sum += lastParsed[nonZeroIndices[i]].flowRate
            }
            answer[mask] = sum
        }
        return answer
    }

    fun solveThatProblem(startInd: Int, nonZeroAmount: Int): Int {
        memoryTimed[0].clear()
        memoryTimed[0] = MutableList(SHIFT * SHIFT * (1 shl nonZeroAmount)) { 0 }
        memoryTimed[1].clear()
        memoryTimed[1] = MutableList(SHIFT * SHIFT * (1 shl nonZeroAmount)) { 0 }
        val masksValues = calculateMaskFlows(nonZeroAmount)

        for (timeLeft in 0..26) {
            for (mask in 0 until (1 shl nonZeroAmount)) {
                for (currentValveInd in 0 until lastParsed.size) {
                    for (elephantValveInd in 0 until lastParsed.size) {
                        val horribleKey = calculateHorribleKey(currentValveInd, elephantValveInd, mask)
                        if (timeLeft == 0) {
                            memoryTimed[0][horribleKey] = 0
                            continue
                        }
                        val currentFlow = masksValues[mask]
                        var maxAchievedAnswerAfterUs = currentFlow * (timeLeft - 1) // To stay here

                        if (!lastParsed[currentValveInd].open && lastParsed[currentValveInd].flowRate != 0) {
                            if (!lastParsed[elephantValveInd].open && lastParsed[elephantValveInd].flowRate != 0 && lastParsed[elephantValveInd].totalIndex != lastParsed[currentValveInd].totalIndex) {
                                maxAchievedAnswerAfterUs = max(
                                    maxAchievedAnswerAfterUs,
                                    currentFlow + memoryTimed[(timeLeft - 1) % 2][calculateHorribleKey(
                                        currentValveInd,
                                        elephantValveInd,
                                        mask or (1 shl lastParsed[currentValveInd].indexInNonZero) or (1 shl lastParsed[elephantValveInd].indexInNonZero)
                                    )]
                                )
                            }
                            lastParsed[elephantValveInd].tunnelsIndices.forEach {
                                maxAchievedAnswerAfterUs = max(
                                    maxAchievedAnswerAfterUs,
                                    currentFlow + memoryTimed[(timeLeft - 1) % 2][calculateHorribleKey(
                                        currentValveInd, it, mask or (1 shl lastParsed[currentValveInd].indexInNonZero)
                                    )]
                                )
                            }
                        }
                        lastParsed[currentValveInd].tunnelsIndices.forEach {
                            if (!lastParsed[elephantValveInd].open && lastParsed[elephantValveInd].flowRate != 0) {
                                maxAchievedAnswerAfterUs = max(
                                    maxAchievedAnswerAfterUs,
                                    currentFlow + memoryTimed[(timeLeft - 1) % 2][calculateHorribleKey(
                                        it,
                                        elephantValveInd,
                                        mask or (1 shl lastParsed[elephantValveInd].indexInNonZero)
                                    )]
                                )
                            }
                            lastParsed[elephantValveInd].tunnelsIndices.forEach { it2 ->
                                maxAchievedAnswerAfterUs = max(
                                    maxAchievedAnswerAfterUs,
                                    currentFlow + memoryTimed[(timeLeft - 1) % 2][calculateHorribleKey(
                                        it, it2, mask
                                    )]
                                )
                            }
                        }
                        memoryTimed[timeLeft % 2][horribleKey] = maxAchievedAnswerAfterUs
                    }
                }
            }
            println(timeLeft)
        }
        return memoryTimed[26 % 2][calculateHorribleKey(startInd, startInd, 0)]
    }

    fun part1(input: List<String>): Int {
        return walkExponentially(parseValves(input).first, 0)
    }

    fun part2(input: List<String>): Int {
        val (start, nonZero) = parseValves(input)
        return solveThatProblem(start.totalIndex, nonZero)
    }

    val input = readInputList(16)
    println(part1(input))
    println(part2(input))
}