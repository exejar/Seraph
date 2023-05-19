package club.maxstats.seraph.render.shader

import club.maxstats.seraph.render.Color
import club.maxstats.seraph.render.drawQuad
import club.maxstats.seraph.util.getScaledResolution
import club.maxstats.seraph.util.mc
import net.minecraft.client.shader.Framebuffer
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

        val sr = getScaledResolution()
        val scale = sr.scaleFactor
        val trueScale = (mc.displayWidth.toFloat() / sr.scaledWidth_double.toFloat()) / 2f

        this.color.color = color
        location.x = x * scale
        location.y = mc.displayHeight - (y + height) * scale

        val scaledWidth = width * trueScale
        val scaledHeight = height * trueScale

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
    val pass = Uniform1f("u_pass")
    val blurRadius = Uniform1f("u_blurRadius")
    val rectRadius = Uniform4f("u_rectRadius")
    val location = Uniform4f("u_location")
    override fun register() {
        registerShader("assets/shaders/blur.fsh", GL20.GL_FRAGMENT_SHADER)
        registerShader("assets/shaders/blur.vsh", GL20.GL_VERTEX_SHADER)
    }

    private fun renderFrameBufferTexture(frameBuffer: Framebuffer) {
        val scaledRes = getScaledResolution()
        val texX = frameBuffer.framebufferWidth.toFloat() / frameBuffer.framebufferTextureWidth.toFloat()
        val texY = frameBuffer.framebufferHeight.toFloat() / frameBuffer.framebufferTextureHeight.toFloat()

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
    }

    var horizontalBuffer = Framebuffer(mc.displayWidth, mc.displayHeight, false)
    var verticalBuffer = Framebuffer(mc.displayWidth, mc.displayHeight, false)
    fun render(
        x: Float = 0f,
        y: Float = 0f,
        width: Float = getScaledResolution().scaledWidth_double.toFloat(),
        height: Float = getScaledResolution().scaledHeight_double.toFloat(),
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomLeftRadius: Float,
        bottomRightRadius: Float,
        blurRadius: Float = 18f
    ) {
        begin()

        horizontalBuffer.framebufferClear()
        verticalBuffer.framebufferClear()

        texelSize.x = 1f / mc.displayWidth
        texelSize.y = 1f / mc.displayHeight
        texture.textureId = 0
        this.blurRadius.x = ceil(2 * blurRadius)

        val sr = getScaledResolution()
        val scale = sr.scaleFactor
        val trueScale = (mc.displayWidth.toFloat() / sr.scaledWidth_double.toFloat()) / 2f

        location.x = x * scale
        location.y = mc.displayHeight - (y + height) * scale
        location.z = width * trueScale
        location.w = height * trueScale

        rectRadius.x = topRightRadius
        rectRadius.y = bottomRightRadius
        rectRadius.z = topLeftRadius
        rectRadius.w = bottomLeftRadius

        // 1st gaussian blur pass
        pass.x = 1f
        applyUniforms(mc.displayWidth.toFloat(), mc.displayHeight.toFloat())

        horizontalBuffer.bindFramebuffer(true)
        mc.framebuffer.bindFramebufferTexture()
        renderFrameBufferTexture(horizontalBuffer)

        // 2nd gaussian blur pass
        pass.x = 2f
        applyUniforms(mc.displayWidth.toFloat(), mc.displayHeight.toFloat())

        verticalBuffer.bindFramebuffer(true)
        horizontalBuffer.bindFramebufferTexture()
        renderFrameBufferTexture(verticalBuffer)

        // region cutting
        pass.x = 3f
        applyUniforms(mc.displayWidth.toFloat(), mc.displayHeight.toFloat())

        mc.framebuffer.bindFramebuffer(true)
        verticalBuffer.bindFramebufferTexture()
        renderFrameBufferTexture(mc.framebuffer)

        mc.framebuffer.unbindFramebuffer()
        verticalBuffer.unbindFramebufferTexture()

        end()
    }

}