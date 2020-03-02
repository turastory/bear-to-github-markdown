package com.turastory.markdown

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LineConverterTest : FunSpec({
    val simpleClassifier = object : Classifier {
        override fun invoke(line: String): Type {
            return when {
                line.startsWith("q") -> Type.Question
                line.startsWith("a") -> Type.Answer
                line.startsWith("c") -> Type.Code
                else -> Type.None
            }
        }
    }
    val rules: Map<Type, LineConversionRule> = mapOf(
        Type.Question to { line: String -> listOf("qq") },
        Type.Answer to { line: String -> listOf("aa") },
        Type.Code to { line: String -> listOf("```") },
        Type.None to { line: String -> listOf("nn") }
    )
    val converter = LineConverter(simpleClassifier, rules)

    test("converter should skip converting the codes") {
        val inputs = listOf(
            "Hello World!",
            "q: This is a question.",
            "a: This is an answer.",
            "c: Code start",
            "q Android",
            "a ABTest",
            "c: Code end"
        )
        val outputs = listOf(
            "nn",
            "qq",
            "aa",
            "```",
            "q Android",
            "a ABTest",
            "```"
        )

        converter(inputs) shouldBe outputs
    }

    test("converter with a single line of text") {
        val inputs = listOf("a: This is an answer")
        val outputs = listOf("aa")
        converter(inputs) shouldBe outputs
    }
})
