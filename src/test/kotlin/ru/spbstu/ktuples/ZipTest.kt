package ru.spbstu.ktuples

import org.junit.Test
import kotlin.test.assertEquals

class ZipTest {

    @Test
    fun `simple cases`() {
        val l0 = listOf(1, 2, 3, 4)
        val l1 = listOf('a', 'b', 'c')

        assertEquals(
                listOf(Tuple(1, 'a'), Tuple(2, 'b'), Tuple(3, 'c')),
                zip(l0, l1)
        )

        assertEquals(
                listOf('b', 'd', 'f'),
                zip(l0, l1) { a, b -> b + a }
        )
    }

    @Test
    fun `simple cases for sequences`() {
        val s0 = "Hello".asSequence()
        val s1 = generateSequence(0) { it + 1 }

        assertEquals(
                listOf(Tuple('H', 0), Tuple('e', 1), Tuple('l', 2),
                        Tuple('l', 3), Tuple('o', 4)),
                zip(s0, s1).toList()
        )

        assertEquals(
                listOf('H', 'f', 'n', 'o', 's'),
                zip(s0, s1) { a, b -> a + b }.toList()
        )
    }

    @Test
    fun `infinite sequences`() {
        val s0 = generateSequence(0) { it + 1 }
        val s1 = generateSequence(0) { it + 1 }.drop(1)
        val s2 = generateSequence(0) { it + 1 }.drop(2)

        assertEquals(
                Tuple(40,41,42),
                zip(s0, s1, s2).drop(40).first()
        )

        assertEquals(
                20 + 21 * 22,
                zip(s0, s1, s2) { a,b,c -> a + b * c }.drop(20).first()
        )
    }

}
