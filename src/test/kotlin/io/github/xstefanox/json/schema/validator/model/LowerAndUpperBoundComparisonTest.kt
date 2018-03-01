package io.github.xstefanox.json.schema.validator.model

import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class LowerAndUpperBoundComparisonTest {

    @Test
    @DisplayName("lower bounds and upper bounds should be compared considering only their value")
    internal fun test1() {

        assertSoftly({

            val lowerBound = LowerBound(10, false)
            val upperBound = UpperBound(11, false)

            assertTrue(lowerBound < upperBound)
            assertTrue(upperBound > lowerBound)
        })

        assertSoftly({

            val lowerBound = LowerBound(10, true)
            val upperBound = UpperBound(11, false)

            assertTrue(lowerBound < upperBound)
            assertTrue(upperBound > lowerBound)
        })

        assertSoftly({

            val lowerBound = LowerBound(10, false)
            val upperBound = UpperBound(11, true)

            assertTrue(lowerBound < upperBound)
            assertTrue(upperBound > lowerBound)
        })
    }
}