package club.maxstats.seraph.render.shader

import club.maxstats.seraph.render.Color
import club.maxstats.seraph.render.drawQuad
import club.maxstats.seraph.util.calculateScaleFactor
import club.maxstats.seraph.util.getScaledResolution
import club.maxstats.seraph.util.mc
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import kotlin.math.ceil

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

class BlurProgram: ShaderProgram() {
    val texture = UniformSampler("u_texture")
    val texelSize = Uniform2f("u_texelSize")
    val direction = Uniform2f("u_direction")
    val radius = Uniform1f("u_radius")
    override fun register() {
        registerShader("assets/shaders/blur.fsh", GL20.GL_FRAGMENT_SHADER)
        registerShader("assets/shaders/blur.vsh", GL20.GL_VERTEX_SHADER)
    }

    // TODO don't use stencils
    private fun renderFrameBufferTexture(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        frameBuffer: Framebuffer
    ) {
        val scaledRes = getScaledResolution()
        val texX = frameBuffer.framebufferWidth.toFloat() / frameBuffer.framebufferTextureWidth.toFloat()
        val texY = frameBuffer.framebufferHeight.toFloat() / frameBuffer.framebufferTextureHeight.toFloat()


        glClear(GL_STENCIL_BUFFER_BIT)
        glEnable(GL_STENCIL_TEST)
        glStencilFunc(GL_ALWAYS, 1, 0xFF)
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE)
        glStencilMask(0xFF)

        drawQuad(x, y, x + width, y + height)

        glStencilFunc(GL_EQUAL, 1, 0xFF)

        glBegin(GL_QUADS)
        glTexCoord2f(0f, 0f)
        glVertex3f(0f, scaledRes.scaledHeight.toFloat(), 0f)
        glTexCoord2f(texX, 0f)
        glVertex3f(scaledRes.scaledWidth.toFloat(), scaledRes.scaledHeight.toFloat(), 0f)
        glTexCoord2f(texX, texY)
        glVertex3f(scaledRes.scaledWidth.toFloat(), 0f, 0f)
        glTexCoord2f(0f, texY)
        glVertex3f(0f, 0f, 0f)
        glEnd()

        glDisable(GL_STENCIL_TEST)
    }

    var blurredBuffer = Framebuffer(mc.displayWidth, mc.displayHeight, false)

    fun render(
        x: Float = 0f,
        y: Float = 0f,
        width: Float = getScaledResolution().scaledWidth_double.toFloat(),
        height: Float = getScaledResolution().scaledHeight_double.toFloat(),
        blurRadius: Float = 18f
    ) {
        begin()

        blurredBuffer.framebufferClear()
        blurredBuffer.bindFramebuffer(true)

        GL20.glUseProgram(this.program)

        texelSize.x = 1f / mc.displayWidth
        texelSize.y = 1f / mc.displayHeight
        texture.textureId = 0
        radius.x = ceil(2 * blurRadius)
        direction.x = 1f
        direction.y = 0f

        applyUniforms(Display.getWidth().toFloat(), Display.getHeight().toFloat())

        mc.framebuffer.bindFramebufferTexture()
        renderFrameBufferTexture(x, y, width, height, blurredBuffer)

        blurredBuffer.unbindFramebuffer()

        direction.x = 0f
        direction.y = 1f

        applyUniforms(Display.getWidth().toFloat(), Display.getHeight().toFloat())

        mc.framebuffer.bindFramebuffer(true)
        blurredBuffer.bindFramebufferTexture()
        renderFrameBufferTexture(x, y, width, height, mc.framebuffer)

        end()
    }

}