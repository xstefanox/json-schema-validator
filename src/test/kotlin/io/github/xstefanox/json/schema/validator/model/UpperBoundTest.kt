package io.github.xstefanox.json.schema.validator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class UpperBoundTest {

    @Test
    @DisplayName("exclusive upper bounds having the same value should be equal")
    internal fun test1() {

        val upperBound1 = UpperBound(42, true)
        val upperBound2 = UpperBound(42, true)

        assertThat(upperBound1).isEqualTo(upperBound2)
    }

    @Test
    @DisplayName("inclusive upper bounds having the same value should be equal")
    internal fun test2() {

        val upperBound1 = UpperBound(42, false)
        val upperBound2 = UpperBound(42, false)

        assertThat(upperBound1).isEqualTo(upperBound2)
    }

    @Test
    @DisplayName("upper bounds having the same value but differing by inclusiveness should not be equal")
    internal fun test3() {

        val upperBound1 = UpperBound(42, true)
        val upperBound2 = UpperBound(42, false)

        assertThat(upperBound1).isNotEqualTo(upperBound2)
    }

    @Test
    @DisplayName("a Number having the same value of an inclusive upper bound should be accepted")
    internal fun test4() {

        val upperBound = UpperBound(42, false)

        assertThat(upperBound.accepts(42)).isTrue()
    }

    @Test
    @DisplayName("a Number having the same value of an exclusive upper bound should not be accepted")
    internal fun test5() {

        val upperBound = UpperBound(42, true)

        assertThat(upperBound.accepts(42)).isFalse()
    }

    @Test
    @DisplayName("a Number lower than an inclusive upper bound should be accepted")
    internal fun test6() {

        val upperBound = UpperBound(42, false)

        assertThat(upperBound.accepts(1)).isTrue()
    }

    @Test
    @DisplayName("a Number lower than an exclusive upper bound should be accepted")
    internal fun test7() {

        val upperBound = UpperBound(42, true)

        assertThat(upperBound.accepts(1)).isTrue()
    }

    @Test
    @DisplayName("a Number greater than an inclusive upper bound should be not accepted")
    internal fun test8() {

        val upperBound = UpperBound(42, false)

        assertThat(upperBound.accepts(100)).isFalse()
    }

    @Test
    @DisplayName("a Number greater than an exclusive upper bound should not be accepted")
    internal fun test9() {

        val upperBound = UpperBound(42, true)

        assertThat(upperBound.accepts(100)).isFalse()
    }
}