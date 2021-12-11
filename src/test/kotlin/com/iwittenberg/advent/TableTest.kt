package com.iwittenberg.advent

import org.testng.annotations.Test

import org.testng.Assert.*

class TableTest {

    @Test
    fun testTestToString() {
        val table = Table("a", listOf("b", "c", "d"), listOf(listOf("1", "2", "3")))

        val expected = """
            a
            +---------+---------+---------+
            | b       | c       | d       |
            +---------+---------+---------+
            | 1       | 2       | 3       |
            +---------+---------+---------+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }

    @Test
    fun testTestToString_1() {
        val table = Table("a really long title but short everything else", listOf("b", "c", "d"), listOf(listOf("1", "2", "3")))

        val expected = """
            a really long title but short everything else
            +---------+---------+---------+
            | b       | c       | d       |
            +---------+---------+---------+
            | 1       | 2       | 3       |
            +---------+---------+---------+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }

    @Test
    fun testTestToString_2() {
        val table = Table("a ", listOf("one really long column compared to everything else", "c", "d"), listOf(listOf("1", "2", "3")))

        val expected = """
            a 
            +--------------------------------------------------------+-----+-----+
            | one really long column compared to everything else     | c   | d   |
            +--------------------------------------------------------+-----+-----+
            | 1                                                      | 2   | 3   |
            +--------------------------------------------------------+-----+-----+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }

    @Test
    fun testTestToString_3() {
        val table = Table("a ", listOf("b", "one really long column compared to everything else", "d"), listOf(listOf("1", "2", "3")))

        val expected = """
            a 
            +-------+------------------------------------------------------+-----+
            | b     | one really long column compared to everything else   | d   |
            +-------+------------------------------------------------------+-----+
            | 1     | 2                                                    | 3   |
            +-------+------------------------------------------------------+-----+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }

    @Test
    fun testTestToString_4() {
        val table = Table("a ",
            listOf("b", "one really long column compared to everything else", "d"),
            listOf(listOf("1", "one really long data compared to everything else", "3")))

        val expected = """
            a 
            +---------+---------------------------------------------------------+--------+
            | b       | one really long column compared to everything else      | d      |
            +---------+---------------------------------------------------------+--------+
            | 1       | one really long data compared to everything else        | 3      |
            +---------+---------------------------------------------------------+--------+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }

    @Test
    fun testTestToString_5() {
        val table = Table("a ",
            listOf("b", "one really long column compared to everything else", "d"),
            listOf(listOf("1", "2", "one really long data compared to everything else")))

        val expected = """
            a 
            +---------+---------------------------------------------------------+--------+
            | b       | one really long column compared to everything else      | d      |
            +---------+---------------------------------------------------------+--------+
            | 1       | 2                                                       | one really long data compared to everything else      |
            +---------+---------------------------------------------------------+--------+
            
        """.trimIndent()
        assertEquals(expected, table.toString())
    }
}