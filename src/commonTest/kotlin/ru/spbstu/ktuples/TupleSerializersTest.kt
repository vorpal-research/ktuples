package ru.spbstu.ktuples

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals

class TupleSerializersTest {
    @Serializable
    data class Moo(val t: Tuple2<String, Double>)

    @Test
    fun baseTest() {
        val zz = Json.encodeToJsonElement(Tuple(1, "Hello", listOf(1, 2, 3)))
        assertEquals(Json.parseToJsonElement("""[1, "Hello", [1, 2, 3]]"""), zz)
    }

    @Test
    fun wrappedTest () {
        val zz = Json.encodeToJsonElement(Moo(Tuple("h", 3.15)))
        assertEquals(Json.parseToJsonElement("""{ "t" : ["h", 3.15] }"""), zz)
        assertEquals(Moo(Tuple("h", 3.15)),
            Json.decodeFromString("""{ "t" : ["h", 3.15] }"""))
    }
}