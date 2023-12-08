package day05

import day01.getSumOfFirstAndLastDigitsInLine
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import readInput

fun main() {
    val puzzleDay = "05";

    val testInput = readInput("day${puzzleDay}/Day${puzzleDay}_test")
    val input = readInput("day${puzzleDay}/Day${puzzleDay}")

    val part1Check = part1(testInput)
    println("Day $puzzleDay. Check part 1: $part1Check")
    check(part1Check == 35L)

    val part1Result = part1(input)
    println("Day $puzzleDay. Part 1: $part1Result")

//    val part2Check = part2(testInput)
//    println("Day $puzzleDay. Check part 2: $part2Check")
//    check(part2Check == 46L)

    val part2Result = part2(input)
    println("Day $puzzleDay. Part 2: $part2Result")
}
private fun part1(input: List<String>): Long {
    val (seeds, maps) = parseInput(input)
    var minimumResult = Long.MAX_VALUE

    for (seed in seeds.map { it.first }) {
        val seedResult = getSeedResult(seed, maps)
        if (minimumResult > seedResult) {
            minimumResult = seedResult
        }
    }
    for (seed in seeds.map { it.last - it.first + 1 }) {
        val seedResult = getSeedResult(seed, maps)
        if (minimumResult > seedResult) {
            minimumResult = seedResult
        }
    }
    return minimumResult
}

private fun part2(input: List<String>): Long {
    val (seeds, maps) = parseInput(input)

    var minimumResult = Long.MAX_VALUE
    for (seedRange in seeds) {
        println("seed count: ${seedRange.count()}")

        for (seed in seedRange) {
            val seedResult = getSeedResult(seed, maps)
            if (seedResult < minimumResult) {
                minimumResult = seedResult
            }
        }
    }
    return minimumResult
}

private data class MapEntry (val range: LongRange, val dest: Long)

private fun parseInput(input: List<String>): Pair<List<LongRange>, Map<String, List<MapEntry>>> {
    val categories = mutableMapOf<String, MutableList<MapEntry>>()
    var currentCategory: String? = null
    var seeds = listOf<LongRange>()
    input.filter { it.isNotEmpty() }.forEach{ line ->
        when {
            line.startsWith("seeds: ") -> {
                val seedInfo = line
                        .split(":")[1]
                        .trim()
                        .split(Regex("\\D+"))
                        .map { it.toLong() }

                seeds = seedInfo
                        .windowed(2, step = 2, partialWindows = false)
                        .map { (start, len) -> start ..<start + len }

            }
            line.endsWith("map:") -> {
                currentCategory = line.split(" ")[0].replace("-", "_")
                categories[currentCategory!!] = mutableListOf()
            }
            else -> {
                val (dest, source, count) = line.split(Regex("\\D+")).map { it.toLong() }
                currentCategory?.let {
                    categories[it]?.add(MapEntry(source ..<source + count, dest))
                }
            }
        }
    }
    return seeds to categories
}


private fun getSeedResult(seed: Long, almanac: Map<String, List<MapEntry>>): Long {
    val chain = mutableListOf(seed)

    var previousCategory = seed
    var categoryDestination = -1L

    for (category in almanac.values) {
        for (map in category) {
            if (previousCategory in map.range) {
                categoryDestination = map.dest - map.range.first + previousCategory
            }
        }
        if (categoryDestination == -1L) {
            categoryDestination = previousCategory
        }
        chain.add(categoryDestination)
        previousCategory = categoryDestination
    }
    return chain.last()
}