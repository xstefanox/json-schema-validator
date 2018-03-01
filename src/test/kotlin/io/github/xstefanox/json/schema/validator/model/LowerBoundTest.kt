package io.github.xstefanox.json.schema.validator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class LowerBoundTest {

    @Test
    @DisplayName("exclusive lower bounds having the same value should be equal")
    internal fun test1() {

        val lowerBound1 = LowerBound(42, true)
        val lowerBound2 = LowerBound(42, true)

        assertThat(lowerBound1).isEqualTo(lowerBound2)
    }

    @Test
    @DisplayName("inclusive lower bounds having the same value should be equal")
    internal fun test2() {

        val lowerBound1 = LowerBound(42, false)
        val lowerBound2 = LowerBound(42, false)

        assertThat(lowerBound1).isEqualTo(lowerBound2)
    }

    @Test
    @DisplayName("lower bounds having the same value but differing by inclusiveness should not be equal")
    internal fun test3() {

        val lowerBound1 = LowerBound(42, true)
        val lowerBound2 = LowerBound(42, false)

        assertThat(lowerBound1).isNotEqualTo(lowerBound2)
    }

    @Test
    @DisplayName("a Number having the same value of an inclusive lower bound should be accepted")
    internal fun test4() {

        val lowerBound = LowerBound(42, false)

        assertThat(lowerBound.accepts(42)).isTrue()
    }

    @Test
    @DisplayName("a Number having the same value of an exclusive lower bound should not be accepted")
    internal fun test5() {

        val lowerBound = LowerBound(42, true)

        assertThat(lowerBound.accepts(42)).isFalse()
    }

    @Test
    @DisplayName("a Number lower than an inclusive lower bound should not be accepted")
    internal fun test6() {

        val lowerBound = LowerBound(42, false)

        assertThat(lowerBound.accepts(1)).isFalse()
    }

    @Test
    @DisplayName("a Number lower than an exclusive lower bound should not be accepted")
    internal fun test7() {

        val lowerBound = LowerBound(42, true)

        assertThat(lowerBound.accepts(1)).isFalse()
    }

    @Test
    @DisplayName("a Number greater than an inclusive lower bound should be accepted")
    internal fun test8() {

        val lowerBound = LowerBound(42, false)

        assertThat(lowerBound.accepts(100)).isTrue()
    }

    @Test
    @DisplayName("a Number greater than an exclusive lower bound should be accepted")
    internal fun test9() {

        val lowerBound = LowerBound(42, true)

        assertThat(lowerBound.accepts(100)).isTrue()
    }
}