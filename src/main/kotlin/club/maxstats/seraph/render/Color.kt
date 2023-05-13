package club.maxstats.seraph.render

data class Color(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
    val alpha: Int = 255
) {
    fun toHex() = "#" +
            red.toString(16).padStart(2, '0') +
            green.toString(16).padStart(2, '0') +
            blue.toString(16).padStart(2, '0') +
            alpha.toString(16).padStart(2, '0')

    fun toRGBA() = (alpha shl 24) or (red shl 16) or (green shl 8) or blue
}

val white = Color(255, 255, 255, 255)