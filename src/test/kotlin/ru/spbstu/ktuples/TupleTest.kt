package ru.spbstu.ktuples

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class TupleTest {

    @Test
    fun `sanity check`() {
        val tup2 = Tuple(2, "Hello")
        assertTrue(tup2 is Tuple2)
        assertEquals("Tuple(2, Hello)", tup2.toString())
    }

    @Test
    fun `formatting tuples is basically same as other collections`() {
        val tup = Tuple(5, "foo", Tuple(4))
        val lst = listOf(5, "foo", Tuple(4))

        for(i in ((-1)..3)) {
            assertEquals(
                    lst.joinToString(
                            separator = "|>>|",
                            prefix = "``",
                            postfix = "''",
                            truncated = "!!!",
                            limit = i
                    ),
                    tup.joinToString(
                            separator = "|>>|",
                            prefix = "``",
                            postfix = "''",
                            truncated = "!!!",
                            limit = i
                    )
            )
        }
    }

    @Test
    fun `toArray should work`() {
        val tup = Tuple(5, "foo", Tuple(4), listOf('A', 'B', 'C'))
        val lst = listOf(5, "foo", Tuple(4), listOf('A', 'B', 'C'))

        assertEquals(lst, tup.toArray().toList())
        assertEquals(lst, tup.toTypedArray().toList())
        assertEquals(lst, tup.toList())
    }

    @Test
    fun `tuple equality and ordering should work`() {
        assertEquals(Tuple(2, "hello", 3), Tuple(2, "hello", 3))
        assertNotEquals(Tuple(2, "hello", 3), Tuple(2, "hello", 4))

        assertTrue(Tuple(2, "hello", 3) > Tuple(1, "hello", 3))
        assertTrue(Tuple(2, "hello", 4) > Tuple(2, "hello", 3))
        assertTrue(Tuple(2, 4) >= Tuple(2, 4))
        assertTrue(Tuple(2, 4) <= Tuple(2, 4))
    }

    @Test
    fun `letAll uncurries the functions`() {
        val app = { i: Int, s: String -> i + s.length }
        assertEquals(7, Tuple(2, "Hello").letAll(app))
        assertEquals(8, Tuple(2, "Hello", 4, 3.15).letAll { i, _, j, _ -> i * j })
    }

    @Test
    fun `logical functions should work as in collections`() {
        Tuple(2, "hello").all { it.toString().isNotEmpty() }
        Tuple(2, "hello").any { it.toString().length > 4 }
        Tuple(2, "hello").none { it.toString().length > 5 }
    }

    @Test
    fun `compareTo should compare`() {
        val v0 = Tuple(1, "Hello")
        val v1 = Tuple(0, "Alloha")
        val v2 = Tuple(1, "Alloha")

        assertTrue(v0 > v1)
        assertTrue(v2 > v1)
        assertTrue(v2 < v0)
    }

    @Test
    fun `comparator() should compare`() {
        val vals = mutableListOf(Tuple(41, "Vasya", 5))
        vals += Tuple(-3, "Masha", 2)
        vals += Tuple(5, "Kostya", 2)
        vals += Tuple(41, "Petya", 5)
        vals += Tuple(5, "Kostya", 3)

        vals.sortWith(Tuple3.comparator())

        assertEquals(
                listOf(
                        Tuple(-3, "Masha", 2),
                        Tuple(5, "Kostya", 2),
                        Tuple(5, "Kostya", 3),
                        Tuple(41, "Petya", 5),
                        Tuple(41, "Vasya", 5)
                ),
                vals
        )
    }
}