import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.xstefanox.json.schema.validator.JsonSchemaNodeDeserializer
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode

class TestUtils {

    companion object {

        val OBJECT_MAPPER: ObjectMapper by lazy {

            val simpleModule = SimpleModule()
                    .addDeserializer(JsonSchemaNode::class.java, JsonSchemaNodeDeserializer())

            ObjectMapper()
                    .configure(INDENT_OUTPUT, true)
                    .registerModule(KotlinModule())
                    .registerModule(simpleModule)
        }
    }
}
