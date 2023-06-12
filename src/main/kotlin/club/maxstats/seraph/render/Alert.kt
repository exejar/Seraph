package club.maxstats.seraph.render

import club.maxstats.kolour.render.FontRenderer
import club.maxstats.kolour.render.FontStyle
import club.maxstats.kolour.render.drawRectangle
import club.maxstats.kolour.util.Color
import club.maxstats.seraph.event.RenderLastEvent
import club.maxstats.seraph.fontManager
import club.maxstats.seraph.render.animation.Fade
import club.maxstats.seraph.render.animation.Spring
import club.maxstats.seraph.render.animation.Spring2D
import club.maxstats.seraph.util.getScaledResolution
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.weavemc.loader.api.event.SubscribeEvent
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.ceil

private val mc = Minecraft.getMinecraft()
private val height = 60f
private val width = 120f
private val alerts = CopyOnWriteArrayList<Alert>()
private fun alert(risk: Risk, title: String, text: String, displayTime: Long, position: Position) {
    alerts.filter { it.position == position }. forEach { it.move() }
    alerts += Alert(risk, title, text, position, displayTime)
}
fun general(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.GENERAL, title, text, displayTime, position)
}
fun none(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.NONE, title, text, displayTime, position)
}
fun low(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.LOW, title, text, displayTime, position)
}
fun medium(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.MEDIUM, title, text, displayTime, position)
}
fun high(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.HIGH, title, text, displayTime, position)
}
fun fatal(title: String, text: String, displayTime: Long, position: Position) {
    alert(Risk.FATAL, title, text, displayTime, position)
}

class AlertRender {
    @SubscribeEvent
    fun onRenderOverlay(event: RenderLastEvent) {
        alerts.forEach { it.render() }
    }
}

private class Alert(
    risk: Risk,
    val title: String,
    val text: String,
    val position: Position,
    displayTime: Long
) {
    val riskColor = risk.color
    val spring = position.inSpring()
    var fade = Fade(Color(35, 35, 35, 230), Color(35, 35, 35, 0), 500, displayTime - 500)
    val titleRenderer: FontRenderer = fontManager.getFontRenderer(20)
    val defaultRenderer: FontRenderer = fontManager.defaultFontRenderer
    val headerHeight = 15f

    fun render() {
        if (fade.isStopped()) {
            alerts -= this
            return
        }

        fade.update()
        spring.update()
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()

        drawRectangle(spring.x.current, spring.y.current, width, headerHeight, 6f, 6f, 0f, 0f, riskColor.copy(alpha = fade.curColor.alpha))
        drawRectangle(spring.x.current, spring.y.current + headerHeight, width, height - headerHeight, 0f, 0f, 6f, 6f, fade.curColor)

        titleRenderer.drawString(
            title,
            ceil(spring.x.current + width / 2 - titleRenderer.getWidth(title) / 2),
            ceil(spring.y.current + headerHeight / 2 - titleRenderer.getHeight(title) / 2),
            Color.white.copy(alpha = fade.curColor.alpha).toRGBA()
        )
        defaultRenderer.drawCenteredWrappedString(
            text,
            (spring.x.current + 5),
            (spring.y.current + defaultRenderer.getHeight(text) + 10),
            width - 10f,
            3f,
            Color.white.copy(alpha = fade.curColor.alpha).toRGBA(),
            FontStyle.PLAIN
        )

        GlStateManager.disableAlpha()
        GlStateManager.disableBlend()
    }
}

fun Color.alpha(alpha: Int) : Color {
    return Color(this.red, this.green, this.blue, alpha)
}

private fun Alert.move() {
    if (this.position == Position.TOP_LEFT || this.position == Position.TOP_RIGHT)
        this.spring.y.target += (height + 5)
    else
        this.spring.y.target -= (height + 5)
}
private fun Position.inSpring() : Spring2D {
    val scaledRes = getScaledResolution()
    return when (this) {
        Position.TOP_LEFT -> Spring2D(
            Spring(0.6f, 0.32f, 60f, -width, 10f),
            Spring(0.6f, 0.32f, 60f, 10f, 10f)
        )
        Position.TOP_RIGHT -> Spring2D(
            Spring(0.6f, 0.32f, 60f, scaledRes.scaledWidth_double.toFloat(), (scaledRes.scaledWidth_double - width - 10).toFloat()),
            Spring(0.6f, 0.32f, 60f, 0f, 0f)
        )
        Position.BOTTOM_LEFT -> Spring2D(
            Spring(0.6f, 0.32f, 60f, -width, 10f),
            Spring(0.6f, 0.32f, 60f, (scaledRes.scaledHeight_double - height - 10).toFloat(), (scaledRes.scaledHeight_double - height - 10).toFloat())
        )
        Position.BOTTOM_RIGHT -> Spring2D(
            Spring(0.6f, 0.32f, 60f, scaledRes.scaledWidth_double.toFloat(), (scaledRes.scaledWidth_double - width - 10).toFloat()),
            Spring(0.6f, 0.32f, 60f, (scaledRes.scaledHeight_double - height - 10).toFloat(), (scaledRes.scaledHeight_double - height - 10).toFloat())
        )
    }
}

enum class Risk(val color: Color) {
    GENERAL(Color(35, 35, 35, 230)),
    NONE(Color(35, 150, 35, 230)),
    LOW(Color(150, 150, 35, 230)),
    MEDIUM(Color(150, 100, 35, 230)),
    HIGH(Color(150, 35, 35, 230)),
    FATAL(Color(75, 0, 0, 230))
}

enum class Position {
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
}