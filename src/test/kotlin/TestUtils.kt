import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule

class TestUtils {

    companion object {

        val OBJECT_MAPPER: ObjectMapper by lazy {

            val objectMapper = ObjectMapper()
            objectMapper.configure(INDENT_OUTPUT, true)
            objectMapper.registerModule(KotlinModule())
        }
    }
}
