package ru.spbstu.ktuples

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VariantTest {
    @Test
    fun `sanity check`() {
        val tp = if(true) Variant0(2) else Variant1("Hello")
        assertEquals(tp.value, 2)
    }

    @Test
    fun `when should be exhaustive`() {
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
    fun `map should not change other values`() {
        val v0 = Variant0(2) ?: Variant1("Hello")

        assertTrue(v0 is EitherOf2)

        val v1 = v0.map1 { it.length }.converge()

        assertEquals(v1, 2)
    }

    @Test
    fun `map should change values accordingly`() {
        val v0 = Variant1("Hello") ?: Variant0(2) ?: Variant2(3.15)

        assertTrue(v0 is EitherOf3)

        val v1 = v0.map1 { it + " world" }

        assertEquals(v1.value, "Hello world")
    }

    @Test
    fun `converge should converge`() {
        val v0 = Variant0(2) ?: Variant1("Hello")

        val c = v0.converge { it.toString() }
        assertEquals("2", c)
    }
}