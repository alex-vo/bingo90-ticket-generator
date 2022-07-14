package com.bingo

fun main() {
//    val start = System.currentTimeMillis()
//    for (i in 1..10000) {
//        BingoTickets()
//    }
//    println("Elapsed ${System.currentTimeMillis() - start} millis.")
    println(BingoTickets())
}

const val ROWS = 18
const val COLUMNS = 9
const val ROWS_PER_CARD = 3
const val NUMBERS_PER_ROW = 5
const val NUMBERS_PER_FIRST_COLUMN = 9
const val NUMBERS_PER_DEFAULT_COLUMN = 10
const val NUMBERS_PER_LAST_COLUMN = 11

class BingoTickets {
    val strip: List<List<List<Int>>>

    init {
        val rawMatrix = generateNumberMatrix()
        val matrixWithValues = placeValues(rawMatrix)

        strip = matrixWithValues.map { it.toList() }
            .toList()
            .chunked(ROWS_PER_CARD)
    }

    private fun generateNumberMatrix(): Array<Array<Boolean>> {
        val matrix = Array(ROWS) { Array(COLUMNS) { false } }
        val columnsToFillBy = (1..COLUMNS)
            .associateWith { if (it == 1) NUMBERS_PER_FIRST_COLUMN else if (it == COLUMNS) NUMBERS_PER_LAST_COLUMN else NUMBERS_PER_DEFAULT_COLUMN }
            .toMutableMap()
        (0 until ROWS).forEach { row ->
            val mustHaveAndArbitrary = columnsToFillBy.entries
                .filter { it.value > 0 }
                .partition { it.value >= ROWS - row }
            val mustHave = mustHaveAndArbitrary.first.map { it.key }
            val arbitrary = mustHaveAndArbitrary.second.map { it.key }.shuffled()
            val options = (mustHave + arbitrary.subList(0, NUMBERS_PER_ROW - mustHave.size))
            for (opt in options.shuffled()) {
                columnsToFillBy[opt] = columnsToFillBy[opt]!! - 1
                matrix[row][opt - 1] = true
            }
        }

        return matrix
    }

    private fun placeValues(rawMatrix: Array<Array<Boolean>>): Array<Array<Int>> {
        val matrixWithValues = Array(ROWS) { Array(COLUMNS) { 0 } }
        var value = 1
        (0 until COLUMNS).forEach { column ->
            (0 until ROWS).mapIndexed { index, row -> if (rawMatrix[row][column]) index / ROWS_PER_CARD else -1 }
                .filter { it != -1 }
                .shuffled()
                .forEach { cardIndex ->
                    matrixWithValues.setValue(rawMatrix, cardIndex, column, value++)
                }
        }
        return matrixWithValues
    }

    private fun Array<Array<Int>>.setValue(
        matrix: Array<Array<Boolean>>,
        cardIndex: Int,
        column: Int,
        number: Int
    ) {
        val rowIndex = cardIndex * ROWS_PER_CARD
        (0 until ROWS_PER_CARD)
            .find { row -> matrix[rowIndex + row][column] }!!
            .let { row ->
                this[rowIndex + row][column] = number
                matrix[rowIndex + row][column] = false
                return
            }
    }

    override fun toString(): String {
        return "---------------------------\n" +
            strip.joinToString("\n---------------------------\n") { card ->
                card.joinToString("\n") { row ->
                    row.joinToString("") { number ->
                        if (number == 0) "|  "
                        else "|${number.toString().padStart(2)}"
                    } + "|"
                }
            } +
            "\n---------------------------"
    }
}
