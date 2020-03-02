package com.turastory.markdown

typealias LineConversionRule = (String) -> List<String>

/**
 * LineConverter reads lines of texts and generates another texts,
 * depending on the rules provided.
 */
interface LineConverter {
    operator fun invoke(lines: List<String>): List<String>
}

fun LineConverter(classifier: Classifier, rules: Map<Type, LineConversionRule>): LineConverter = object : LineConverter {
    override fun invoke(lines: List<String>): List<String> {
        val convertWithType: LineConversionRule = { line ->
            val type = classifier(line)
            rules[type]?.invoke(line) ?: listOf(line)
        }

        var skipConversion = false

        // TODO: Refactor this - not to use state variable
        return lines.flatMap { line ->
            val type = classifier(line)
            if (skipConversion) {
                if (type == Type.Code) {
                    skipConversion = false
                    convertWithType(line)
                } else {
                    listOf(line)
                }
            } else {
                if (type == Type.Code)
                    skipConversion = true
                convertWithType(line)
            }
        }
    }
}
