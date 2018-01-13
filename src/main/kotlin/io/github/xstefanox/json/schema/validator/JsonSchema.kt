package io.github.xstefanox.json.schema.validator

abstract class JsonSchema {

    companion object {

        /**
         * The Media Type for a JSON Schema as defined in [rfc6838](https://tools.ietf.org/html/rfc6838).
         */
        @JvmField
        val MEDIA_TYPE = "application/schema+json"
    }
}