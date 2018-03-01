package io.github.xstefanox.json.schema.validator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntervalTest {

    @Test
    @DisplayName("a number lesser than the lower bound should be considered out of the interval")
    internal fun test1() {

        val interval = Interval(LowerBound(100))

        assertThat(interval.contains(42)).isFalse()
    }

    @Test
    @DisplayName("a number greater than the upper bound should be considered out of the interval")
    internal fun test2() {

        val interval = Interval(UpperBound(100))

        assertThat(interval.contains(101)).isFalse()
    }

    @Test
    @DisplayName("a number lesser than the upper bound and greater than the lower bound should be considered in the interval")
    internal fun test3() {

        val interval = Interval(LowerBound(-100), UpperBound(100))

        assertThat(interval.contains(42)).isTrue()
    }

    @Test
    @DisplayName("lower bound can be equal to upper bound")
    internal fun test4() {
        Interval(LowerBound(100), UpperBound(100))
    }

    @Test
    @DisplayName("lower bound can not be greater than upper bound")
    internal fun test5() {
        assertThrows(IllegalArgumentException::class.java, {
            Interval(LowerBound(100), UpperBound(-100))
        })
    }
}