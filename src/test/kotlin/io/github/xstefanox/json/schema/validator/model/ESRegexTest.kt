package io.github.xstefanox.json.schema.validator.model

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ESRegexTest {

    @Test
    @DisplayName("invalid regex should be rejected")
    internal fun test1() {
        assertThrows(IllegalArgumentException::class.java, {
            ESRegex("*")
        })
    }

    @Test
    @DisplayName("valid regex should be accepted")
    internal fun test2() {
        ESRegex("test")
    }

    @Test
    @DisplayName("non matching string should be correctly tested")
    internal fun test3() {
        assertThat(ESRegex("^test.*$").matches("something")).isFalse()
    }

    @Test
    @DisplayName("matching string should be correctly tested")
    internal fun test4() {
        assertThat(ESRegex("^test.*$").matches("test123")).isTrue()
    }

    @Test
    @DisplayName("regex should be serialized as EcmaScript regex")
    internal fun test5() {

        val actual = OBJECT_MAPPER.writeValueAsString(ESRegex("^test.*$"))
        val expected = """
            "/^test.*$/"
            """.trimIndent()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("regex should be deserialized from EcmaScript regex")
    internal fun test6() {

        val actual = OBJECT_MAPPER.readValue<ESRegex>("""
            "/^test.*$/"
            """.trimIndent())
        val expected = ESRegex("^test.*$")

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("ESRegex built on the same EcmaScript regex should be equal")
    internal fun test7() {

        val actual = ESRegex("test")
        val expected = ESRegex("test")

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("ESRegex built on different EcmaScript regex should be different")
    internal fun test8() {

        val actual = ESRegex("test1")
        val expected = ESRegex("test2")

        assertThat(actual).isNotEqualTo(expected)
    }

    @Test
    @DisplayName("regex should be normalized")
    internal fun test9() {

        val actual = ESRegex("/test/")
        val expected = ESRegex("test")

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("normalization should consider regex modifiers")
    internal fun test10() {

        val actual = ESRegex("/test/")
        val expected = ESRegex("/test/i")

        assertThat(actual).isNotEqualTo(expected)
    }

    @Test
    @DisplayName("string representation should be the normalized regular expression itself")
    internal fun test11() {
        assertThat(ESRegex("/test/gi").toString()).isEqualTo("/test/gi")
    }

    @Test
    @DisplayName("ESRegex should be equal to itself")
    internal fun test12() {

        val esRegex = ESRegex("test")

        assertThat(esRegex).isEqualTo(esRegex)
    }

    @Test
    @DisplayName("ESRegex cannot be equal to object of another class")
    internal fun test13() {

        val esRegex = ESRegex("test")

        assertThat(esRegex).isNotEqualTo(Any())
    }

    @Test
    @DisplayName("ESRegex objects built on the same regex should have the same hascode")
    internal fun test14() {

        val esRegex1 = ESRegex("test")
        val esRegex2 = ESRegex("test")

        assertThat(esRegex1).hasSameHashCodeAs(esRegex2)
    }

    @Test
    @DisplayName("ESRegex objects built on different regex should have different hascode")
    internal fun test15() {

        val esRegex1 = ESRegex("test")
        val esRegex2 = ESRegex("abc")

        assertThat(esRegex1.hashCode()).isNotEqualTo(esRegex2.hashCode())
    }
}