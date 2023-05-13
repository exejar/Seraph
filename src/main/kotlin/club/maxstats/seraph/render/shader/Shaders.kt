package club.maxstats.seraph.render.shader

import org.lwjgl.opengl.GL20

class RoundedRectProgram : ShaderProgram() {
    val location = Uniform2f("u_location")
    val radius = Uniform4f("u_radius")
    val color = UniformColor("u_color")

    override fun register() {
        registerShader("assets/shaders/roundedrect.fsh", GL20.GL_FRAGMENT_SHADER)
    }
}