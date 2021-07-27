package ru.spbstu.ktuples

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromJsonElement
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class TupleTest {


    @Test
    fun `sanity_check`() {
        val tup2 = Tuple(2, "Hello")
        assertTrue(tup2 is Tuple2)
        assertEquals("Tuple(2, Hello)", tup2.toString())
    }

    @Test
    fun `formatting_tuples_is_basically_same_as_other_collections`() {
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
    fun `toArray_should_work`() {
        val tup = Tuple(5, "foo", Tuple(4), listOf('A', 'B', 'C'))
        val lst = listOf(5, "foo", Tuple(4), listOf('A', 'B', 'C'))

        assertEquals(lst, tup.toArray().toList())
        assertEquals(lst, tup.toTypedArray().toList())
        assertEquals(lst, tup.toList())
    }

    @Test
    fun `tuple_equality_and_ordering_should_work`() {
        assertEquals(Tuple(2, "hello", 3), Tuple(2, "hello", 3))
        assertNotEquals(Tuple(2, "hello", 3), Tuple(2, "hello", 4))

        assertTrue(Tuple(2, "hello", 3) > Tuple(1, "hello", 3))
        assertTrue(Tuple(2, "hello", 4) > Tuple(2, "hello", 3))
        assertTrue(Tuple(2, 4) >= Tuple(2, 4))
        assertTrue(Tuple(2, 4) <= Tuple(2, 4))
    }

    @Test
    fun `letAll_uncurries_the_functions`() {
        val app = { i: Int, s: String -> i + s.length }
        assertEquals(7, Tuple(2, "Hello").letAll(app))
        assertEquals(8, Tuple(2, "Hello", 4, 3.15).letAll { i, _, j, _ -> i * j })
    }

    @Test
    fun `logical_functions_should_work_as_in_collections`() {
        assertTrue(Tuple(2, "hello").all { it.toString().isNotEmpty() })
        assertTrue(Tuple(2, "hello").any { it.toString().length > 4 })
        assertTrue(Tuple(2, "hello").none { it.toString().length > 5 })
    }


    @Test
    fun `plus_should_catenate`() {
        assertEquals(Tuple(2, "Hello", 4.1, 8), Tuple(2, "Hello") + Tuple(4.1, 8))
        assertEquals(Tuple(2, "Hello", 8), Tuple(2, "Hello") + 8)
        assertEquals(Tuple(2, "Hello", 8), Tuple() + 2 + "Hello" + 8)
    }

    @Test
    fun `compareTo_should_compare`() {
        val v0 = Tuple(1, "Hello")
        val v1 = Tuple(0, "Alloha")
        val v2 = Tuple(1, "Alloha")

        assertTrue(v0 > v1)
        assertTrue(v2 > v1)
        assertTrue(v2 < v0)
    }

    @Test
    fun `comparator_should_compare`() {
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

    @Test
    fun `sorted_should_sort`() {
        assertEquals(Tuple(1,2,3), Tuple(1,2,3).sorted())
        assertEquals(Tuple(1,2,3), Tuple(1,3,2).sorted())
        assertEquals(Tuple(1,2,3), Tuple(2,1,3).sorted())
        assertEquals(Tuple(1,2,3), Tuple(2,3,1).sorted())
        assertEquals(Tuple(1,2,3), Tuple(3,2,1).sorted())
        assertEquals(Tuple(1,2,3), Tuple(3,1,2).sorted())

        assertEquals(Tuple(1,2,3,4,5,6), Tuple(6,5,4,3,2,1).sorted())
        assertEquals(Tuple(1,2,3,4,5,6), Tuple(6,1,2,3,5,4).sorted())

    }

    @Test
    fun `sortedWith_should_sort`() {
        assertEquals(Tuple(Tuple(1, 2), Tuple(1, 3), Tuple(2, 4), Tuple(7, 8)),
                    Tuple(Tuple(2, 4), Tuple(1, 3), Tuple(7, 8), Tuple(1, 2)).sortedWith(Tuple2.comparator<Int, Int>()))

        assertEquals(Tuple(Tuple(1, 2), Tuple(1, 3), Tuple(2, 4), Tuple(7, 8)),
                Tuple(Tuple(2, 4), Tuple(1, 3), Tuple(7, 8), Tuple(1, 2)).sortedWith(Comparator{ a, b -> a.compareTo(b) }))

        assertEquals(Tuple(6,5,4,3,2,1), Tuple(6,1,2,3,5,4).sortedWith(reverseOrder()))
    }

    @Test
    fun `fromCollection_works`() {
        assertEquals(Tuple(1, 2, 3), Tuple3.fromCollection((1..3).toList()))
        assertFailsWith<IllegalArgumentException> {
            Tuple3.fromCollection((1..4).toList())
        }
    }

}