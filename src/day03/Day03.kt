package day03

import readInput

fun main() {
    val testInput = readInput("day03/Day03_test")
    val input = readInput("day03/Day03")

    val part1Check = part1(testInput)
    println("Day 03. Check part 1: $part1Check")
    check(part1Check == 4361)

    val part1Result = part1(input)
    println("Day 03. Part 1: $part1Result")

    val part2Check = part2(testInput)
    println("Day 03. Check part 2: $part2Check")
    check(part2Check == 467835)

    val part2Result = part2(input)
    println("Day 03. Part 2: $part2Result")
}

private fun part1(input: List<String>): Int {
    val partNumbers = mutableListOf<Int>()
    var result = 0
    for (lineIdx in input.indices) {
        val line = input[lineIdx]
        var pos = 0
        var numberStr = ""
        while (pos < line.length) {
            val char = line[pos]
            if (isNumeric(char)) {
                numberStr += char
            } else {
                if (numberStr != "") {
                    if (isAdjacentToSymbol(input, lineIdx, pos - numberStr.length, pos - 1)) {
                        partNumbers.add(numberStr.toInt())
                        result += numberStr.toInt()
                    }
                    numberStr = ""
                }
            }
            pos++
        }
        if (numberStr != "") {
            if (isAdjacentToSymbol(input, lineIdx, line.length - numberStr.length, line.length - 1)) {
                partNumbers.add(numberStr.toInt())
                result += numberStr.toInt()
            }
        }
    }

    return result
}

private fun part2(input: List<String>): Int {
    var result = 0
    for (lineIdx in input.indices) {
        val line = input[lineIdx]
        var pos = 0
        while (pos < line.length) {
            if (line[pos] == '*') {
                result += getGearValue(input, lineIdx, pos)
            }
            pos++
        }
    }
    return result
}

private fun isAdjacentToSymbol(input: List<String>, line: Int, start: Int, end: Int): Boolean {
    val fromLine = maxOf(0, line - 1)
    val toLine = minOf(input.size - 1, line + 1)
    val fromCol = maxOf(0, start - 1)
    val toCol = minOf(input.first().length - 1, end + 1)

    for (currentLineIdx in (fromLine .. toLine)) {
        val currentLine = input[currentLineIdx]
        if (currentLineIdx == line) {
            if (fromCol < start) {
                val char = currentLine[fromCol]
                if (isSymbol(char)) {
                    return true
                }
            }
            if (toCol > end) {
                val char = currentLine[toCol]
                if (isSymbol(char)) {
                    return true
                }
            }
        } else {
            for (charIdx in fromCol .. toCol) {
                val char = currentLine[charIdx]
                if (isSymbol(char)) {
                    return true
                }
            }
        }
    }
    return false
}

private fun getGearValue(input: List<String>, line: Int, col: Int): Int {
    val fromLine = maxOf(0, line - 1)
    val toLine = minOf(input.size - 1, line + 1)
    val fromCol = maxOf(0, col - 1)
    val toCol = minOf(input.first().length - 1, col + 1)

    val partNumbers = mutableListOf<Int>()

    for (currentLineIdx in (fromLine .. toLine)) {
        val currentLine = input[currentLineIdx]
        if (currentLineIdx == line) {
            if (fromCol < col) {
                val char = currentLine[fromCol]
                if (isNumeric(char)) {
                    val partNumber = getNumberByCharPos(currentLine, fromCol)
                    partNumbers.add(partNumber)
                }
            }
            if (toCol > col) {
                val char = currentLine[toCol]
                if (isNumeric(char)) {
                    val partNumber = getNumberByCharPos(currentLine, toCol)
                    partNumbers.add(partNumber)
                }
            }
        } else {
            // ...   ..1   .11   1..  123   .1.   3.1   #32   43#   4#4   #%4
            var previousWasNumeric = false
            for (currentPos in fromCol .. toCol) {
                if (isNumeric(currentLine[currentPos])) {
                    if (!previousWasNumeric) {
                        val partNumber = getNumberByCharPos(currentLine, currentPos)
                        partNumbers.add(partNumber)
                        previousWasNumeric = true
                    }
                } else {
                    previousWasNumeric = false
                }
            }

        }
    }
    return if (partNumbers.size == 2) {
        partNumbers[0] * partNumbers[1]
    } else {
        0
    }
}

private fun isNumeric(char: Char): Boolean {
    return "1234567890".contains(char)
}

private fun isSymbol(char: Char): Boolean {
    return !isNumeric(char) && '.' != char
}

private fun getNumberByCharPos(line: String, pos: Int): Int {
    var result = ""
    var idx = pos - 1
    while (idx >= 0 && isNumeric(line[idx])) {
        result = line[idx] + result
        idx--
    }
    result += line[pos]
    idx = pos + 1
    while (idx < line.length && isNumeric(line[idx])) {
        result += line[idx]
        idx++
    }
    return result.toInt()
}