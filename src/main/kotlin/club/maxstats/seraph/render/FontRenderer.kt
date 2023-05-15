package club.maxstats.seraph.render

import club.maxstats.seraph.util.asResource
import club.maxstats.seraph.util.wrap
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.ceil
import kotlin.math.roundToInt

class FontManager {
    private val defaultFont: GLFont
    val defaultFontRenderer: FontRenderer
    private val fonts = mutableMapOf<String, GLFont>()
    init {
        val executor = Executors.newFixedThreadPool(1) as ThreadPoolExecutor
        val textureQueue = ConcurrentLinkedQueue<TextureData>()

        defaultFont = GLFont(executor, textureQueue, Font("Verdana", Font.PLAIN, 18))

        loadFont("Bold", "/assets/fonts/Quicksand-Bold.ttf", intArrayOf(8, 10, 12, 14, 16, 18, 19, 20, 22, 24, 36, 48, 72, 96), executor, textureQueue);
        loadFont("Medium", "/assets/fonts/Quicksand-Medium.ttf", intArrayOf(8, 10, 12, 14, 16, 18, 19, 20, 22, 24, 36, 48, 72, 96), executor, textureQueue);
        loadFont("Light", "/assets/fonts/Quicksand-Light.ttf", intArrayOf(8, 10, 12, 14, 16, 18, 19, 20, 22, 24, 36, 48, 72, 96), executor, textureQueue);

        defaultFontRenderer = FontRenderer(
            this.getFont("Light 18"),
            this.getFont("Medium 18"),
            this.getFont("Bold 18"),
            this.getFont("Medium 18"),
            this.getFont("Bold 18")
        )

        executor.shutdown()

        while (!executor.isTerminated) {
            try {
                Thread.sleep(10L)
            } catch (ignored: Exception) {}

            while (!textureQueue.isEmpty()) {
                val textureData = textureQueue.poll()

                GlStateManager.bindTexture(textureData.textureId)
                GL11.glTexParameteri(3553, 10241, 9728)
                GL11.glTexParameteri(3553, 10240, 9728)
                GL11.glTexImage2D(3553, 0, 6408, textureData.width, textureData.height, 0, 6408, 5121, textureData.buffer)
            }
        }
    }

    fun getFont(key: String): GLFont { return fonts.getOrDefault(key, defaultFont) }
    fun getFontRenderer(size: Int): FontRenderer {
        return FontRenderer(
            getFont("Light $size"),
            getFont("Medium $size"),
            getFont("Bold $size"),
            getFont("Medium $size"),
            getFont("Bold $size")
        )
    }

    private fun loadFont(
        key: String,
        location: String,
        metrics: IntArray,
        executor: ThreadPoolExecutor,
        textureQueue: ConcurrentLinkedQueue<TextureData>
    ) {
        try {
            for (metric in metrics) {
                val stream = location.asResource()

                val loaded = Font.createFont(0, stream).deriveFont(Font.PLAIN, metric.toFloat())
                fonts["$key $metric"] = GLFont(executor, textureQueue, loaded)
            }
        } catch (ignored: Exception) {
        }
    }
}

class FontRenderer(
    val lightFont: GLFont,
    val plainFont: GLFont,
    val boldFont: GLFont,
    val italicFont: GLFont,
    val boldItalicFont: GLFont
) {
    private val colors = IntArray(32)
    private val colorCodes = "0123456789abcdefklmnor"
    init {
        initColors()
    }

    /**
     * Draws a string to the screen using the provided FontStyle.
     * @param text the string to draw.
     * @param x the X position.
     * @param y the Y position.
     * @param color the color.
     * @param style the font style to render with
     */
    fun drawString(
        text: String,
        x: Double,
        y: Double,
        color: Int,
        style: FontStyle = FontStyle.PLAIN
    ) {
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()

        renderString(getFontFromStyle(style), text, x.toFloat(), y.toFloat(), color)
    }

    /**
     * Draws and wraps a string to the screen according to the width and lineSpacing
     * @param width distance the text can be rendered at before wrapping down to another line
     * @param lineSpacing spacing between each line
     * @return Pair<width, height> width: longest line, height: combined height of all lines and spacing
     */
    fun drawWrappedString(
        text: String,
        x: Double,
        y: Double,
        width: Double,
        lineSpacing: Double,
        color: Int,
        style: FontStyle = FontStyle.PLAIN
    ): Pair<Double, Double> {
        val font = getFontFromStyle(style)
        val lines = text.wrap(
            ceil(width).toInt(),
            this,
            font
        ).split(System.lineSeparator())

        var height = 0.0
        var longest = 0.0

        for (i in lines.indices) {
            val lineWidth = getWidth(lines[i], font).toDouble()
            val lineHeight = getHeight(lines[i], font).toDouble()

            drawString(lines[i], x, y + (i * (lineHeight + lineSpacing)).toFloat(), color, style)

            if (lineWidth > longest)
                longest = lineWidth

            height += lineHeight + lineSpacing
        }

        return longest to height
    }

    fun drawCenteredWrappedString(
        text: String,
        x: Double,
        y: Double,
        width: Double,
        lineSpacing: Double,
        color: Int,
        style: FontStyle = FontStyle.PLAIN
    ): Pair<Double, Double> {
        val font = getFontFromStyle(style)
        val lines = text.wrap(
            ceil(width).toInt(),
            this,
            font
        ).split(System.lineSeparator())

        var height = 0.0
        var longest = 0.0

        for (i in lines.indices) {
            val lineWidth = getWidth(lines[i], font).toDouble()
            val lineHeight = getHeight(lines[i], font).toDouble()

            drawString(lines[i], x + width / 2 - lineWidth / 2, y + (i * (lineHeight + lineSpacing)).toFloat(), color, style)

            if (lineWidth > longest)
                longest = lineWidth

            height += lineHeight + lineSpacing
        }

        return longest to height
    }

    private fun renderString(
        font: GLFont,
        text: String,
        x: Float,
        y: Float,
        color: Int
    ) {
        if (text.isEmpty())
            return

        var xCoord = (x * 10f).roundToInt() / 10f
        var yCoord = (y * 10f).roundToInt() / 10f

        GL11.glPushMatrix()
        GlStateManager.scale(0.5, 0.5, 1.0)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        xCoord -= 2f
        yCoord -= 2f
        xCoord += 0.5f
        yCoord += 0.5f
        xCoord *= 2f
        yCoord *= 2f

        val multiplier = 255.0
        val currentColor = Color(color)
        GL11.glColor4d(
            currentColor.red / multiplier,
            currentColor.green / multiplier,
            currentColor.blue / multiplier,
            (color ushr 24 and 0xff) / multiplier
        )

        var currentFont = font
        var bold = false
        var italic = false

        try {
            for ((i, char) in text.withIndex()) {
                if (char.code > 256)
                    continue

                /* Found Minecraft Chat Format */
                if (char == '\u00A7' && i + 1 < text.length) {
                    val colorCodeIndex = colorCodes.indexOf(text[i + 1].lowercase())

                    when (colorCodes.indexOf(text[i + 1].lowercase())) {
                        in 0..15, 21 -> {
                            bold = false
                            italic = false
                            currentFont = font

                            if (colorCodeIndex <= 15) {
                                val colorCode = colors[colorCodeIndex]
                                val red = (colorCode shr 16 and 0xFF) / 255.0f
                                val green = (colorCode shr 8 and 0xFF) / 255.0f
                                val blue = (colorCode and 0xFF) / 255.0f

                                GL11.glColor4f(red, green, blue, 1.0f)
                            } else {
                                GL11.glColor4d(
                                    currentColor.red / multiplier,
                                    currentColor.green / multiplier,
                                    currentColor.blue / multiplier,
                                    (color shr 24 and 0xFF) / 255.0
                                )
                            }
                        }
                        17 -> {
                            bold = true
                            currentFont = if (italic) boldItalicFont else boldFont
                        }
                        20 -> {
                            italic = true
                            currentFont = if (bold) boldItalicFont else italicFont
                        }
                    }
                    continue
                }

                drawChar(char, currentFont.characters, xCoord, yCoord)

                if (char.code >= currentFont.characters.size) continue

                val charData = currentFont.characters[char.code]
                xCoord += charData.width - 8f
            }
        } catch (ignored: StringIndexOutOfBoundsException) {
        }

        GL11.glPopMatrix()
        GlStateManager.disableBlend()
        GlStateManager.bindTexture(0)
        GlStateManager.resetColor()
    }

    private fun drawChar(
        character: Char,
        charDataArray: Array<CharacterData>,
        x: Float,
        y: Float
    ) {
        if (character.code >= charDataArray.size) return;

        val charData = charDataArray[character.code]
        charData.bind()

        GL11.glBegin(6);
        GL11.glTexCoord2f(0f, 0f);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0f, 1f);
        GL11.glVertex2f(x, y + charData.height);
        GL11.glTexCoord2f(1f, 1f);
        GL11.glVertex2f(x + charData.width, y + charData.height);
        GL11.glTexCoord2f(1f, 0f);
        GL11.glVertex2f(x + charData.width, y);
        GL11.glEnd();
    }
    fun getWidth(text: String, font: GLFont = plainFont) = this.getBounds(text, font).first + 2f
    fun getHeight(text: String, font: GLFont = plainFont) = this.getBounds(text, font).second / 2f - 2f

    /**
     * Gets the bounds of the given text. Takes account of Minecraft's chat formatting
     * @return Pair<width, height>
     */
    private fun getBounds(text: String, font: GLFont): Pair<Float, Float> {
        var height = 0f
        var width = 0f
        var bold = false
        var italic = false
        var currentFont = font

        for ((i, char) in text.withIndex()) {
            if (char.code > 256) continue

            /* Found Minecraft Chat Format, Check if it's Bold or Italicized */
            if (char == '\u00A7' && i + 1 < text.length) {
                when (colorCodes.indexOf(text[i + 1].lowercase())) {
                    in 0..15, 21 -> {
                        bold = false
                        italic = false
                        currentFont = font
                    }
                    17 -> {
                        bold = true
                        currentFont = if (italic) boldItalicFont else boldFont
                    }
                    20 -> {
                        italic = true
                        currentFont = if (bold) boldItalicFont else italicFont
                    }
                }
                continue
            }

            val charData = currentFont.characters[char.code]
            height = height.coerceAtLeast(charData.height)
            width += (charData.width - 8f) / 2f
        }

        return width to height
    }

    private fun initColors() {
        for (i in 0 until 32) {
            val t = (i shr 3 and 1) * 85
            var r = (i shr 2 and 1) * 170 + t
            var g = (i shr 1 and 1) * 170 + t
            var b = (i and 1) * 170 + t

            if (i == 6) {
                r += 85
            }

            if (i >= 16) {
                r /= 4
                g /= 4
                b /= 4
            }

            colors[i] = ((r and 255) shl 16 or (g and 255) shl 8 or (b and 255))
        }
    }

    fun getFontFromStyle(style: FontStyle) : GLFont {
        return when (style) {
            FontStyle.LIGHT -> this.lightFont
            FontStyle.BOLD -> this.boldFont
            FontStyle.ITALIC -> this.italicFont
            FontStyle.BOLD_ITALIC -> this.boldItalicFont
            FontStyle.PLAIN -> this.plainFont
        }
    }
}
class GLFont(
    executor: ExecutorService,
    textureQueue: ConcurrentLinkedQueue<TextureData>,
    javaFont: Font
) {
    val characters = Array(256) { CharacterData(0, 0.toChar(), 0f, 0f) }
    init {
        val textureIds = IntArray(256) { GL11.glGenTextures() }
        executor.execute { setup(javaFont, characters, textureIds, textureQueue) }
    }

    private fun setup(
        font: Font,
        charData: Array<CharacterData>,
        textureIds: IntArray,
        textureQueue: ConcurrentLinkedQueue<TextureData>
    ): Array<CharacterData> {
        val plainFont = font.deriveFont(Font.PLAIN)
        val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()

        graphics.font = plainFont

        val metrics = graphics.fontMetrics

        for (i in charData.indices) {
            val character = i.toChar()
            val bounds = metrics.getStringBounds(character.toString(), graphics)

            val width = bounds.width.toFloat() + 8.0f
            val height = bounds.height.toFloat()

            val image = BufferedImage(ceil(width.toDouble()).toInt(), ceil(height.toDouble()).toInt(), BufferedImage.TYPE_INT_ARGB)
            val g2d = image.createGraphics()

            g2d.font = plainFont
            g2d.color = Color.WHITE
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            g2d.drawString(character.toString(), 4, metrics.ascent)

            val textureId = textureIds[i]

            createTexture(textureId, image, textureQueue)

            charData[i] = CharacterData(textureId, character, image.width.toFloat(), image.height.toFloat())
        }

        graphics.dispose()

        return charData
    }

    private fun createTexture(
        textureId: Int,
        image: BufferedImage,
        textureQueue: ConcurrentLinkedQueue<TextureData>
    ) {
        val pixels = IntArray(image.width * image.height)
        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)
        val buffer = BufferUtils.createByteBuffer(image.width * image.height * 4)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            buffer.put((pixel shr 16 and 0xFF).toByte())
            buffer.put((pixel shr 8 and 0xFF).toByte())
            buffer.put((pixel and 0xFF).toByte())
            buffer.put((pixel shr 24 and 0xFF).toByte())
        }

        buffer.flip()
        textureQueue.add(TextureData(textureId, image.width, image.height, buffer))
    }
}

data class CharacterData(
    val textureId: Int,
    val character: Char,
    val width: Float,
    val height: Float
) {
    fun bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId)
    }
}

data class TextureData(
    val textureId: Int,
    val width: Int,
    val height: Int,
    val buffer: ByteBuffer
)

enum class FontStyle {
    LIGHT, PLAIN, BOLD, ITALIC, BOLD_ITALIC
}