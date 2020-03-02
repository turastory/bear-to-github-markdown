package com.turastory.markdown

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ClassifierTest : FunSpec({
    val classifier = Classifier()

    test("Classify headers") {
        classifier("# SICP Note 1.1") shouldBe Type.Header
        classifier("### 1.1 The Elements of Programming") shouldBe Type.Header
        classifier("###### Summary#") shouldBe Type.Header
        classifier("#SICP") shouldNotBe Type.Header
    }

    test("Classify quotes") {
        classifier("> Hello World") shouldBe Type.Quote
        classifier(">The predicate expression is evaluated first, ") shouldNotBe Type.Quote
    }

    test("Classify questions") {
        classifier("\tHow's the day?") shouldBe Type.Question
        classifier("    What are the adventages when I use block structure?") shouldBe Type.Question
        classifier("  What are the adventages when I use block structure?") shouldNotBe Type.Question
    }

    test("Classify codes") {
        classifier("```scheme") shouldBe Type.Code
        classifier("```") shouldBe Type.Code
        classifier("``scheme") shouldNotBe Type.Code
    }

    test("Classify none types") {
        classifier("Android Studio") shouldBe Type.None
        classifier("* Just a regular unordered list") shouldBe Type.None
        classifier("1. Just a regular ordered list") shouldBe Type.None
        classifier("") shouldBe Type.None
        classifier(" ") shouldBe Type.None
    }
})
