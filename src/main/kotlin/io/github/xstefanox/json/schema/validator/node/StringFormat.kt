package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonValue

enum class StringFormat(private val format: String) {

    DATE_TIME("date-time"),
    EMAIL("email"),
    HOSTNAME("hostname"),
    IPV4("ipv4"),
    IPV6("ipv6"),
    URI("uri");

    @JsonValue
    fun toJson(): String {
        return this.format
    }
}