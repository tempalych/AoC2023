fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test")
    check(part1(testInput1) == 142)

    val testInput2 = readInput("Day01_1_test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int {
    var result = 0
    for (line in input) {
        result += getSumOfFirstAndLastDigitsInLine(line)
    }
    return result
}

fun part2(input: List<String>): Int {
    var result = 0


    for (line in input) {
        var translatedLine = ""
        var pos = 0
        while (pos < line.length) {
            if ("123456789".contains(line[pos])) {
                translatedLine += line[pos]
            } else {
                val searchLine = line.substring(pos..<line.length)
                val lexerResult = lexer(searchLine) // lexerResult: [token, stopPosition]
                if (lexerResult != 0) {
                    translatedLine += lexerResult.toString()
                }
            }
            pos++
        }
        val lineResult = getSumOfFirstAndLastDigitsInLine(translatedLine)
        result += lineResult
    }
    return result
}

val numbersMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9)

fun getSumOfFirstAndLastDigitsInLine(line: String): Int {
    var first = -1
    var last = -1

    for (char in line) {
        if ("1234567890".contains(char)) {
            if (first == -1) {
                first = char.toString().toInt()
            }
            last = char.toString().toInt()
        }
    }
    return  first * 10 + last
}

fun lexer(input: String): Int {
    fun tokenMatchesWord(token: String): Boolean {
        numbersMap.forEach{
            if (it.key.startsWith(token))
                return true
        }
        return false
    }

    var pos = 0
    while (pos < input.length) {
        val token = input.substring(0 ..pos)
        if (numbersMap.contains(token)) {
            return numbersMap[token]!!
        }
        if (!tokenMatchesWord(token)) {
            return 0
        }
        pos++
    }
    return 0
}