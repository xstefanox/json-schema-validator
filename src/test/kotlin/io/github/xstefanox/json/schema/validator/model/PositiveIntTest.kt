package io.github.xstefanox.json.schema.validator.model

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

internal class PositiveIntTest {

    @Test
    @DisplayName("negative values should be rejected")
    internal fun test1() {
        assertThrows(IllegalArgumentException::class.java, { PositiveInt(-1) })
    }

    @Test
    @DisplayName("comparation should behave the same way of native int")
    internal fun test2() {
        assertThat(PositiveInt(1)).isEqualTo(PositiveInt(1))
        assertThat(PositiveInt(1)).isGreaterThan(PositiveInt(0))
        assertThat(PositiveInt(0)).isLessThan(PositiveInt(1))
    }

    @Test
    @DisplayName("string representation should be the value itself")
    internal fun test3() {
        assertThat(PositiveInt(1).toString()).isEqualTo("1")
    }

    @Test
    @DisplayName("a PositiveInt should be serialized as integer")
    internal fun test4() {
        assertThat(OBJECT_MAPPER.writeValueAsString(PositiveInt(1))).isEqualTo("1")
    }

    @Test
    @DisplayName("a PositiveInt should be deserialized from integer")
    internal fun test5() {
        assertThat(OBJECT_MAPPER.readValue<PositiveInt>("1")).isEqualTo(PositiveInt(1))
    }
}