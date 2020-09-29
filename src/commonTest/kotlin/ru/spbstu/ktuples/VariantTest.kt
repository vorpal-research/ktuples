package ru.spbstu.ktuples

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VariantTest {
    @Test
    fun `sanity_check`() {
        val tp = if(true) Variant0(2) else Variant1("Hello")
        assertEquals(tp.value, 2)
    }

    @Test
    fun `when_should_be_exhaustive`() {
        val v0: EitherOf3<Int, Double, String> = Variant0(value = 4)

        val result: String = // force exhaustiveness check
                when(v0) {
                    is Variant0 -> (v0.value - 3).toString(8)
                    is Variant1 -> (v0.value.isNaN().toString())
                    is Variant2 -> v0.value
                }

        // this is more of a compile-time test, not a runtime one
        assertEquals(result, "1")
    }

    @Test
    fun `map_should_not_change_other_values`() {
        val v0 = Variant0(2) ?: Variant1("Hello")

        assertTrue(v0 is EitherOf2)

        val v1 = v0.map1 { it.length }.converge()

        assertEquals(v1, 2)
    }

    @Test
    fun `map_should_change_values_accordingly`() {
        val v0 = Variant1("Hello") ?: Variant0(2) ?: Variant2(3.15)

        assertTrue(v0 is EitherOf3)

        val v1 = v0.map1 { it + " world" }

        assertEquals(v1.value, "Hello world")
    }

    @Test
    fun `converge_should_converge`() {
        val v0 = Variant0(2) ?: Variant1("Hello")

        val c = v0.converge { it.toString() }
        assertEquals("2", c)
    }

    @Test
    fun `dynamic_constructor_should_work`() {
        assertTrue(Variant(0, "Hello") is Variant0<*>)
        assertEquals(Variant(0, 2), Variant0(2))
    }

    @Test
    fun `compareTo_should_compare`() {
        val v0: EitherOf3<Int, String, Double> = Variant0(2)
        val v1: EitherOf3<Int, String, Double> = Variant1("Hi")
        val v2: EitherOf3<Int, String, Double> = Variant1("Alloha")

        assertTrue(v0 < v2)
        assertTrue(v0 < v1)
        assertTrue(v1 > v2)
    }

    @Test
    fun `comparator_should_compare`() {
        val vals: MutableList<EitherOf3<Boolean, String, Int>> = mutableListOf()
        vals += Variant0(false)
        vals += Variant2(5)
        vals += Variant1("Hello")
        vals += Variant2(0)
        vals += Variant1("Alloha")

        vals.sortWith(EitherOf3.comparator())

        assertEquals(
                listOf(
                        Variant0(false),
                        Variant1("Alloha"),
                        Variant1("Hello"),
                        Variant2(0),
                        Variant2(5)
                        ),
                vals
        )
    }
}