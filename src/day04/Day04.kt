package day04

import readInput
import kotlin.math.pow

fun main() {
    val testInput = readInput("day04/Day04_test")
    val input = readInput("day04/Day04")

    val part1Check = part1(testInput)
    println("Day 04. Check part 1: $part1Check")
    check(part1Check == 13)

    val part1Result = part1(input)
    println("Day 04. Part 1: $part1Result")

    val part2Check = part2(testInput)
    println("Day 04. Check part 2: $part2Check")
    check(part2Check == 30)

    val part2Result = part2(input)
    println("Day 04. Part 2: $part2Result")
}

private fun part1(input: List<String>): Int {
    var result = 0
    for (line in input) {
        result += getCardResult(getCardIntersectsCount(line.split(":")[1]))
    }

    return result
}

private fun part2(input: List<String>): Int {
    var result = 0
    val cards = mutableMapOf<Int, Int>()
    for (lineIdx in input.indices) {
        cards[lineIdx] = 1
    }

    for (lineIdx in input.indices) {
        val line = input[lineIdx]
        val cardValue = getCardIntersectsCount(line.split(":")[1])
        for (iter in 1..cards[lineIdx]!!) {
            for (i in lineIdx + 1..lineIdx + cardValue) {
                cards[i] = cards[i]!! + 1
            }
        }
    }
    for (value in cards.values) {
        result += value
    }
    return result
}

private fun getCardIntersectsCount(line: String): Int {
    val winningNumbers = line.split("|")[0]
            .split(" ")
            .map{it.trim()}
            .filter{it.isNotEmpty()}
            .map{it.toInt()}
            .toSet()

    val myNumbers = line.split("|")[1]
            .split(" ")
            .map{it.trim()}
            .filter{it.isNotEmpty()}
            .map{it.toInt()}
            .toSet()

    return winningNumbers.intersect(myNumbers).size
}

private fun getCardResult(intersectionsCount: Int): Int {
    return if (intersectionsCount > 1) {
        2.0.pow(intersectionsCount.toDouble() - 1).toInt()
    } else {
        intersectionsCount
    }
}