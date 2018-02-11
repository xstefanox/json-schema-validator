package io.github.xstefanox.json.schema.validator.model

import com.fasterxml.jackson.annotation.JsonValue
import jdk.nashorn.api.scripting.ScriptObjectMirror
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import javax.script.SimpleBindings
import kotlin.text.RegexOption.IGNORE_CASE

class ESRegex(regex: String) {

    private val regex: ScriptObjectMirror

    private companion object {

        private val REGEX_COMPILATION_SCRIPT: CompiledScript

        private val REGEX_MATCHER = Regex("/(.*)/([a-z]*)", IGNORE_CASE)

        init {
            val js = ScriptEngineManager().getEngineByName("js")

            js as Compilable

            REGEX_COMPILATION_SCRIPT = js.compile("new RegExp(regex, modifiers)")
        }
    }

    init {

        val simpleBindings = SimpleBindings()

        val matchResult = REGEX_MATCHER.find(regex)

        if (matchResult != null) {
            simpleBindings["regex"] = matchResult.groups[1]!!.value
            simpleBindings["modifiers"] = matchResult.groups[2]!!.value
        } else {
            simpleBindings["regex"] = regex
            simpleBindings["modifiers"] = ""
        }

        try {
            this.regex = REGEX_COMPILATION_SCRIPT.eval(simpleBindings) as ScriptObjectMirror
        } catch (e: ScriptException) {
            throw IllegalArgumentException("$regex is not a valid EcmaScript regular expression")
        }
    }

    fun matches(s: String): Boolean {
        return regex.callMember("test", s) as Boolean
    }

    @JsonValue
    override fun toString(): String {
        return regex.callMember("toString") as String
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) {
            return true
        }

        if (javaClass != other?.javaClass) {
            return false
        }

        other as ESRegex

        if (regex.toString() != other.regex.toString()) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        return regex.toString().hashCode()
    }
}
