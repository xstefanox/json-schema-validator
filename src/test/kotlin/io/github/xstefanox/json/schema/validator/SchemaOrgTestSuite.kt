package io.github.xstefanox.json.schema.validator

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Paths
import java.util.stream.Stream
import kotlin.streams.asStream


private const val SCHEMA_ORG_TEST_SUITE_DIR = "/submodules/json-schema-org-test-suite/tests/draft7"


internal class SchemaOrgTestSuite {

    @ParameterizedTest(name = "{0}")
    @MethodSource("getTests")
    internal fun testAll(schemaOrgTestAssertion: SchemaOrgTest) {

        val schema = JsonSchemaFactory().from(schemaOrgTestAssertion.schema.toString())

        assertSoftly { softly ->

            schemaOrgTestAssertion.tests.forEach { testAssertion ->

                val validationResult = schema.validate(testAssertion.data)

                softly.assertThat(validationResult.isSuccessful)
                        .describedAs(testAssertion.description)
                        .isEqualTo(testAssertion.valid)

            }
        }
    }

    companion object {

        @JvmStatic
        @Suppress("unused")
        private fun getTests(): Stream<SchemaOrgTest> {
            return Paths.get(System.getProperty("user.dir"), SCHEMA_ORG_TEST_SUITE_DIR)
                    .toFile()
                    .walk()
                    .filterNot(File::isDirectory)
                    .flatMap { OBJECT_MAPPER.readValue<List<SchemaOrgTest>>(it).asSequence() }
                    .asStream()
        }
    }

    internal data class SchemaOrgTest(val description: String, val schema: JsonNode, val tests: List<TestAssertion>) {

        override fun toString(): String {
            return description
        }
    }

    internal data class TestAssertion(val description: String, val data: JsonNode, val valid: Boolean)
}
