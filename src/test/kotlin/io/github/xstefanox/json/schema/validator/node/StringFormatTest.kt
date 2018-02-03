package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class StringFormatTest {

    @Test
    @DisplayName("StringFormat.DATE_TIME should be serialized following JSON Schema specification")
    internal fun test1() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.DATE_TIME)).isEqualTo("\"date-time\"")
    }

    @Test
    @DisplayName("StringFormat.EMAIL should be serialized following JSON Schema specification")
    internal fun test2() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.EMAIL)).isEqualTo("\"email\"")
    }

    @Test
    @DisplayName("StringFormat.EMAIL should be serialized following JSON Schema specification")
    internal fun test3() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.HOSTNAME)).isEqualTo("\"hostname\"")
    }

    @Test
    @DisplayName("StringFormat.IPV4 should be serialized following JSON Schema specification")
    internal fun test4() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.IPV4)).isEqualTo("\"ipv4\"")
    }

    @Test
    @DisplayName("StringFormat.IPV6 should be serialized following JSON Schema specification")
    internal fun test5() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.IPV6)).isEqualTo("\"ipv6\"")
    }

    @Test
    @DisplayName("StringFormat.URI should be serialized following JSON Schema specification")
    internal fun test6() {
        assertThat(OBJECT_MAPPER.writeValueAsString(StringFormat.URI)).isEqualTo("\"uri\"")
    }

    @Test
    @DisplayName("\"date-time\" should be deserialized in the corresponding enum value")
    internal fun test7() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"date-time\"")).isEqualTo(StringFormat.DATE_TIME)
    }

    @Test
    @DisplayName("\"email\" should be deserialized in the corresponding enum value")
    internal fun test8() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"email\"")).isEqualTo(StringFormat.EMAIL)
    }

    @Test
    @DisplayName("\"hostname\" should be deserialized in the corresponding enum value")
    internal fun test9() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"hostname\"")).isEqualTo(StringFormat.HOSTNAME)
    }

    @Test
    @DisplayName("\"ipv4\" should be deserialized in the corresponding enum value")
    internal fun test10() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"ipv4\"")).isEqualTo(StringFormat.IPV4)
    }

    @Test
    @DisplayName("\"ipv6\" should be deserialized in the corresponding enum value")
    internal fun test11() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"ipv6\"")).isEqualTo(StringFormat.IPV6)
    }

    @Test
    @DisplayName("\"uri\" should be deserialized in the corresponding enum value")
    internal fun test12() {
        assertThat(OBJECT_MAPPER.readValue<StringFormat>("\"uri\"")).isEqualTo(StringFormat.URI)
    }
}