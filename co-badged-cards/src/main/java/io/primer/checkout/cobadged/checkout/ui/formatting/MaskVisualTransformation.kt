package io.primer.checkout.cobadged.checkout.ui.formatting

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MaskVisualTransformation(private val mask: String, private val symbol: Char = '#') :
    VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != symbol }

    override fun filter(text: AnnotatedString): TransformedText {
        val source = text.iterator()
        val masked = StringBuilder()

        var char = source.nextOrNull()

        loop@ for (maskChar in mask) {
            when {
                maskChar == char && maskChar != symbol -> {
                    masked.append(maskChar)
                    char = source.nextOrNull()
                }

                maskChar == symbol -> {
                    char = source.nextMaskChar(char)
                    if (char == null) { break@loop } else {
                        masked.append(char)
                        char = source.nextOrNull()
                    }
                }

                else -> {
                    if (char == null) break@loop
                    masked.append(maskChar)
                }
            }
        }

        return TransformedText(
            AnnotatedString(masked.toString()),
            offsetTranslator(masked.toString())
        )
    }

    private fun offsetTranslator(formatted: String) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val transformedOffsets = formatted
                .mapIndexedNotNull { index, _ ->
                    index
                        .takeIf { specialSymbolsIndices.contains(index).not() }
                        ?.plus(1)
                }
                .let { offsetList ->
                    listOf(0) + offsetList
                }

            return transformedOffsets[offset.coerceAtMost(transformedOffsets.size - 1)]
        }

        override fun transformedToOriginal(offset: Int): Int {
            return formatted
                .mapIndexedNotNull { index, _ ->
                    index.takeIf { specialSymbolsIndices.contains(index).not() }
                }
                .count { separatorIndex ->
                    separatorIndex < offset
                }
                .let { separatorCount ->
                    offset - separatorCount
                }
        }
    }

    private fun CharIterator.nextOrNull(): Char? = if (hasNext()) nextChar() else null

    private fun CharIterator.nextMaskChar(char: Char?): Char? {
        var nextMaskChar = char ?: return null
        while (hasNext()) {
            if (nextMaskChar.isLetterOrDigit()) break
            nextMaskChar = nextChar()
        }
        return if (nextMaskChar.isLetterOrDigit()) nextMaskChar else null
    }
}
