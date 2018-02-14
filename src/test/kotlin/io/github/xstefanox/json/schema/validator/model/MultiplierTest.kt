package io.github.xstefanox.json.schema.validator.model

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class MultiplierTest {

    @Test
    @DisplayName("multiplier must be greater than 0")
    internal fun test1() {
        assertThrows(IllegalArgumentException::class.java, { Multiplier(0) })
        assertThrows(IllegalArgumentException::class.java, { Multiplier(-1) })
    }

    @Test
    @DisplayName("multiplier must be serialized as double if its native value is a double")
    internal fun test2() {
        assertThat(OBJECT_MAPPER.writeValueAsString(Multiplier(1.0))).isEqualTo("1.0")
    }

    @Test
    @DisplayName("multiplier must be serialized as integer if its native value is an integer")
    internal fun test3() {
        assertThat(OBJECT_MAPPER.writeValueAsString(Multiplier(1))).isEqualTo("1")
    }

    @Test
    @DisplayName("multiplier must be deserialized from double")
    internal fun test4() {
        assertThat(OBJECT_MAPPER.readValue<Multiplier>("1.0")).isEqualTo(Multiplier(1.0))
    }

    @Test
    @DisplayName("multiplier must be deserialized from integer")
    internal fun test5() {
        assertThat(OBJECT_MAPPER.readValue<Multiplier>("1")).isEqualTo(Multiplier(1))
    }

    @Test
    @DisplayName("comparation should behave the same way of native value")
    internal fun test6() {
        assertThat(Multiplier(1)).isEqualTo(Multiplier(1))
        assertThat(Multiplier(2)).isGreaterThan(Multiplier(1))
        assertThat(Multiplier(1)).isLessThan(Multiplier(2))
    }

    @Test
    @DisplayName("comparation should not be affected by the type of the native value")
    internal fun test7() {
        assertThat(Multiplier(1)).isEqualTo(Multiplier(1.0))
        assertThat(Multiplier(2)).isGreaterThan(Multiplier(1.0))
        assertThat(Multiplier(1)).isLessThan(Multiplier(2.0))

        assertThat(Multiplier(1.0)).isEqualTo(Multiplier(1))
        assertThat(Multiplier(2.0)).isGreaterThan(Multiplier(1))
        assertThat(Multiplier(1.0)).isLessThan(Multiplier(2))
    }

    @Test
    @DisplayName("string representation should be the value itself")
    internal fun test8() {
        assertThat(Multiplier(1).toString()).isEqualTo("1")
    }

    @Test
    @DisplayName("a multiplier should divide its multiples")
    internal fun test9() {
        assertThat(Multiplier(2).divides(4)).isTrue()
    }

    @Test
    @DisplayName("a multiplier should not divide a number which is not one of its multiplesmultiples")
    internal fun test10() {
        assertThat(Multiplier(2).divides(5)).isFalse()
    }
}