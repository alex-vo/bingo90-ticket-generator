package com.bingo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BingoGeneratorTest {

    companion object {
        val COLUMNS = mapOf(
            0 to (1..9).toList(),
            1 to (10..19).toList(),
            2 to (20..29).toList(),
            3 to (30..39).toList(),
            4 to (40..49).toList(),
            5 to (50..59).toList(),
            6 to (60..69).toList(),
            7 to (70..79).toList(),
            8 to (80..90).toList(),
        )
    }

    val tickets = BingoTickets()

    @Test
    fun `should generate 6 cards 3x9 each`() {
        assertEquals(6, tickets.strip.size)
        tickets.strip.forEach { card ->
            assertEquals(3, card.size)
            card.forEach { row ->
                assertEquals(9, row.size)
            }
        }
    }

    @Test
    fun `should fill each column with 5 numbers`() {
        tickets.strip.forEach { card ->
            card.forEach { row ->
                assertEquals(5, row.count { it > 0 })
            }
        }
    }

    @Test
    fun `should order numbers in columns ASC`() {
        tickets.strip.forEach { card ->
            for (i in 0 until 9) {
                val columnValues = card.map { row -> row[i] }.filter { it != 0 }
                assertThat(columnValues).isSorted
            }
        }
    }

    @Test
    fun `should fill columns with correct numbers`() {
        (0 until 9).forEach { column ->
            assertEquals(
                COLUMNS[column],
                tickets.strip.flatMap { card -> card.map { row -> row[column] }.filter { it != 0 } }.sorted()
            )
        }
    }

    @Test
    fun `should fill cards with numbers 1 to 90 without duplicates`() {
        assertEquals((1..90).toList(), tickets.strip.flatMap { it.flatMap { it.filter { it != 0 } } }.sorted())
    }

}