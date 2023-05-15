package club.maxstats.seraph.render.shader

import club.maxstats.seraph.render.Color
import club.maxstats.seraph.render.drawQuad
import club.maxstats.seraph.util.calculateScaleFactor
import club.maxstats.seraph.util.mc
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL20

class RoundedRectProgram : ShaderProgram() {
    val location = Uniform2f("u_location")
    val radius = Uniform4f("u_radius")
    val color = UniformColor("u_color")

    override fun register() {
        registerShader("assets/shaders/roundedrect.fsh", GL20.GL_FRAGMENT_SHADER)
    }

    fun render(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomLeftRadius: Float,
        bottomRightRadius: Float,
        color: Color
    ) {
        begin()

        val scale = calculateScaleFactor(mc)
        this.color.color = color
        location.x = x * scale
        location.y = Display.getHeight() - (y + height) * scale

        var scaledWidth = width
        var scaledHeight = height
        if (scale == 1) {
            scaledWidth /= 2
            scaledHeight /= 2
        }

        radius.x = topRightRadius
        radius.y = bottomRightRadius
        radius.z = topLeftRadius
        radius.w = bottomLeftRadius

        applyUniforms(scaledWidth, scaledHeight)
        drawQuad(x - 5, y - 5, x + width + 10, y + height + 10)
        end()
    }
}