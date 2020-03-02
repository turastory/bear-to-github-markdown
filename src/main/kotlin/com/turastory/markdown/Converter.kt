package com.turastory.markdown

typealias LineConverter = (String) -> List<String>

/**
 * Converter reads lines of texts and generates another texts,
 * depending on the rules provided.
 */
interface Converter {
    operator fun invoke(lines: List<String>): List<String>
}

fun Converter(classifier: Classifier, rules: Map<Type, LineConverter>): Converter = object : Converter {
    override fun invoke(lines: List<String>): List<String> {
        val convertWithType: LineConverter = { line ->
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
