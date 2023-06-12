package club.maxstats.seraph.util

import club.maxstats.kolour.render.FontRenderer
import club.maxstats.kolour.render.GLFont

private val splitChars = charArrayOf(' ', '-', '\t')
fun String.wrap(
    width: Int,
    fontRenderer: FontRenderer,
    font: GLFont
): String {
    val words = this.explode(splitChars)

    var lineLength = 0f
    val builder = StringBuilder()
    for (word in words) {
        var wordLength = fontRenderer.getWidth(word, font)
        var formattedWord = word

        if (lineLength + wordLength > width) {
            if (lineLength > 0) {
                builder.append(System.lineSeparator())
                lineLength = 0f
            }

            while (wordLength > width) {
                builder.append(word, 0, width - 1).append('-')
                wordLength = fontRenderer.getWidth(word.substring(width - 1), font)

                builder.append(System.lineSeparator())
            }

            formattedWord = word.trim()
        }

        builder.append(formattedWord)
        lineLength += wordLength
    }

    return builder.toString()
}

private fun String.explode(
    splitChars: CharArray
): Array<String> {
    val parts = mutableListOf<String>()
    var startIndex = 0

    while (true) {
        val index = this.indexOfAny(splitChars, startIndex)

        if (index == -1) {
            parts.add(this.substring(startIndex))
            return parts.toTypedArray()
        }

        val word = this.substring(startIndex, index)
        val nextChar = this.substring(index, index + 1)[0]

        if (Character.isWhitespace(nextChar)) {
            parts.add(word)
            parts.add(nextChar.toString())
        } else
            parts.add(word + nextChar)

        startIndex = index + 1
    }
}

private fun String.indexOfAny(
    searchChars: CharArray,
    startingIndex: Int
): Int {
    val sub = this.substring(startingIndex)
    for ((index, char) in sub.withIndex()) {
        if (searchChars.contains(char))
            return index + startingIndex
    }

    return -1
}