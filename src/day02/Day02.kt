package day02

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    val input = readInput("day02/Day02")

    val part1Check = part1(testInput)
    println("Day 02. Check part 1: $part1Check")
    check(part1Check == 8)

    val part1Result = part1(input)
    println("Day 02. Part 1: $part1Result")

    val part2Check = part2(testInput)
    println("Day 02. Check part 2: $part2Check")
    check(part2Check == 2286)

    val part2Result = part2(input)
    println("Day 02. Part 2: $part2Result")
}

class Cube (val color: String, val cnt: Int)
class Attempt (val cubesList: List<Cube>)
class Game (val id: Int, val attemptsList: List<Attempt>)

fun part1(input: List<String>): Int {
    val redCnt = 12
    val greenCnt = 13
    val blueCnt = 14
    var result = 0

    for (line in input) {
        val game = parseGame(line)
        var isPossible = true
        for (attempt in game.attemptsList) {
            for (cube in attempt.cubesList) {
                val color = cube.color
                val cnt = cube.cnt
                if ((color == "red" && cnt > redCnt) ||
                        (color == "green" && cnt > greenCnt) ||
                        (color == "blue" && cnt > blueCnt)) {
                    isPossible = false
                    break
                }
            }
            if (!isPossible) {
                break
            }
        }
        if (isPossible) {
            result += game.id
        }
    }

    return result
}

fun part2(input: List<String>): Int {
    var result = 0

    for (line in input) {
        val game = parseGame(line)
        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0
        for (attempt in game.attemptsList) {
            for (cube in attempt.cubesList) {
                val color = cube.color
                val cnt = cube.cnt

                if (color == "red" && cnt > maxRed) {
                    maxRed = cnt
                }

                if (color == "green" && cnt > maxGreen) {
                    maxGreen = cnt
                }

                if (color == "blue" && cnt > maxBlue) {
                    maxBlue = cnt
                }
            }
        }

        val gameResult = maxRed * maxGreen * maxBlue;
        result += gameResult
    }

    return result
}


fun parseGame(gameStr: String): Game {

    fun parseAttempt(attemptStr: String): Attempt {

        fun parseCube(cubeStr: String): Cube {
            val parsedCube = cubeStr.split(" ") // "1","blue"
            val cnt = parsedCube[0].toInt() // 1
            val color = parsedCube[1] // blue
            return Cube(color, cnt)
        }

        val cubes = attemptStr.split(", ") // "1 blue, 2 green"
        val cubesList = mutableListOf<Cube>()
        cubes.forEach{
            cubesList.add(parseCube(it))
        }

        return Attempt(cubesList)
    }

    val gameSplits = gameStr.split(": ") // ["Game 2","1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"]
    val id = gameSplits[0].substring(5).toInt() // 2
    val attempts = gameSplits[1].split("; ") // ["1 blue, 2 green","3 green, 4 blue, 1 red","1 green, 1 blue"]
    val attemptsList = mutableListOf<Attempt>()
    attempts.forEach{
        attemptsList.add(parseAttempt(it))
    }

    return Game(id, attemptsList)
}