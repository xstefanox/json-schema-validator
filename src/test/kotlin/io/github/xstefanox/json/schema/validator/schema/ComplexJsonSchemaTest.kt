package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ComplexJsonSchemaTest {

    @Test
    @DisplayName("array index should be reported in property error message")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "products": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "id": {
                                    "type": "integer"
                                },
                                "name": {
                                    "type": "string"
                                },
                                "price": {
                                    "type": "number"
                                },
                                "returned": {
                                    "type": "boolean"
                                }
                            }
                        }
                    },
                    "date": {
                        "type": "null"
                    }
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "products": [
                    {
                        "id": null,
                        "name": "product1",
                        "price": 1.5,
                        "returned": false
                    },
                    {
                        "id": 1,
                        "name": null,
                        "price": 1.5,
                        "returned": false
                    },
                    {
                        "id": 1,
                        "name": "product1",
                        "price": null,
                        "returned": false
                    },
                    {
                        "id": 1,
                        "name": "product1",
                        "price": 1.5,
                        "returned": null
                    }
                ],
                "date": "this is not null"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        // @formatter:off

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(5)
                .at(0)
                    .hasMessage("element should be an integer")
                    .pointsTo("/products/0/id")
                .at(1)
                    .hasMessage("element should be a string")
                    .pointsTo("/products/1/name")
                .at(2)
                    .hasMessage("element should be a number")
                    .pointsTo("/products/2/price")
                .at(3)
                    .hasMessage("element should be a boolean")
                    .pointsTo("/products/3/returned")
                .at(4)
                    .hasMessage("element should be null")
                    .pointsTo("/date")

        // @formatter:on
    }

    @Test
    @DisplayName("errors should be reported from any level of depth")
    internal fun name() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "products": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "id": {
                                    "type": "integer"
                                },
                                "categories": {
                                    "type": "array",
                                    "items": {
                                        "type": "object",
                                        "properties": {
                                            "name": {
                                                "type": "string"
                                            },
                                            "tags": {
                                                "type": "array",
                                                "items": {
                                                    "type": "string"
                                                },
                                                "minItems": 1
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "products": [
                    {
                        "id": null,
                        "categories": []
                    },
                    {
                        "id": 1,
                        "categories": [
                            {
                                "name": null,
                                "tags": [
                                    "red"
                                ]
                            }
                        ]
                    },
                    {
                        "id": 1,
                        "categories": [
                            {
                                "name": "something",
                                "tags": [
                                    "red"
                                ]
                            },
                            {
                                "name": "another",
                                "tags": [
                                    true
                                ]
                            }
                        ]
                    },
                    {
                        "id": 1,
                        "categories": [
                            {
                                "name": "something",
                                "tags": []
                            }
                        ]
                    }
                ]
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        // @formatter:off

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(4)
                .at(0)
                    .hasMessage("element should be an integer")
                    .pointsTo("/products/0/id")
                .at(1)
                    .hasMessage("element should be a string")
                    .pointsTo("/products/1/categories/0/name")
                .at(2)
                    .hasMessage("element should be a string")
                    .pointsTo("/products/2/categories/1/tags/0")
                .at(3)
                    .hasMessage("expected at least 1 items")
                    .pointsTo("/products/3/categories/0/tags")

        // @formatter:on
    }
}