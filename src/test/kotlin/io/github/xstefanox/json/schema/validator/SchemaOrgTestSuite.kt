package io.github.xstefanox.json.schema.validator

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Paths
import java.util.stream.Stream
import kotlin.streams.asStream


private val SCHEMA_ORG_TEST_SUITE_DIR = Paths.get(
        System.getProperty("user.dir"),
        "/submodules/json-schema-org-test-suite/tests/draft7"
)


internal class SchemaOrgTestSuite {

    @ParameterizedTest(name = "{0}")
    @MethodSource("getTests")
    @DisplayName("draft 7 official tests")
    internal fun testAll(schemaOrgTestAssertion: JsonSchemaOrgTestFile) {

        val schema = JsonSchemaFactory().from(schemaOrgTestAssertion.test.schema.toString())

        assertSoftly { softly ->

            schemaOrgTestAssertion.test.tests.forEach { testAssertion ->

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
        private fun getTests(): Stream<JsonSchemaOrgTestFile> {
            return SCHEMA_ORG_TEST_SUITE_DIR
                    .toFile()
                    .walk()
                    .filterNot(File::isDirectory)
                    .flatMap { file ->
                        OBJECT_MAPPER.readValue<List<JsonSchemaOrgTest>>(file)
                                .asSequence()
                                .map { test ->
                                    JsonSchemaOrgTestFile(test, file)
                                }
                    }
                    .asStream()
        }
    }

    internal data class JsonSchemaOrgTest(val description: String, val schema: JsonNode, val tests: List<TestAssertion>)

    internal data class TestAssertion(val description: String, val data: JsonNode, val valid: Boolean)

    internal data class JsonSchemaOrgTestFile(
            val test: JsonSchemaOrgTest,
            private val file: File
    ) {

        override fun toString(): String {
            return "${file.path.removePrefix(SCHEMA_ORG_TEST_SUITE_DIR.toString()).removePrefix("/")} : ${test.description}"
        }
    }
}
