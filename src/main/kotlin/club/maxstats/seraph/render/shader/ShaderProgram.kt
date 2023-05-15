package club.maxstats.seraph.render.shader

import club.maxstats.seraph.render.Color
import club.maxstats.seraph.util.asResource
import club.maxstats.seraph.util.now
import club.maxstats.seraph.util.readToString
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20.*

abstract class ShaderProgram(
    val program: Int = glCreateProgram(),
    val uniforms: MutableList<Uniform> = mutableListOf(),
    var active: Boolean = false
) {
    abstract fun register()
    init {
        register()
        glLinkProgram(program)
        glValidateProgram(program)
    }

    protected val genesis = now()
    protected val resolution = Uniform2f("u_resolution")
    protected val time = Uniform1f("u_time")
    open fun begin() {
        active = true
        glUseProgram(program)
    }
    open fun applyUniforms(width: Float, height: Float) {
        resolution.x = width
        resolution.y = height

        time.x = (now() - genesis) / 1000f

        uniforms.forEach { it.apply() }
    }
    open fun end() {
        if (!active) return
        active = false
        glUseProgram(0)
    }
    open fun render(width: Float, height: Float) {
        begin()
        applyUniforms(width, height)
        end()
    }

    fun registerShader(
        location: String,
        type: Int
    ) {
        val source = location.asResource()
        val shader = initShader(
            location.substringAfterLast("/").substringBeforeLast("."),
            source.readToString(),
            type
        )

        glAttachShader(program, shader)
    }
    private fun initShader(name: String, shaderSource: String, type: Int) : Int {
        var shader = 0

        try {
            shader = glCreateShader(type)
            glShaderSource(shader, shaderSource)
            glCompileShader(shader)

            if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL11.GL_FALSE)
                throw Exception(
                    "Error in creating shader $name :${
                        glGetShaderInfoLog(shader, Short.MAX_VALUE.toInt())
                    }"
                )

            return shader
        } catch (ex: Exception) {
            glDeleteShader(shader)
            throw ex
        }
    }

    fun getUniform(name: String) = glGetUniformLocation(program, name)
    abstract inner class Uniform(
        name: String,
        var location: Int = 0
    ) {
        init {
            location = getUniform(name)
            registerUniform()
        }
        private fun registerUniform() = uniforms.add(this)
        internal abstract fun apply()
    }

    open inner class Uniform1f(
        name: String,
        var x: Float = 0f
    ) : Uniform(name) {
        override fun apply() {
            glUniform1f(location, x)
        }
    }

    open inner class Uniform2f(
        name: String,
        var x: Float = 0f,
        var y: Float = 0f
    ) : Uniform(name) {
        override fun apply() {
            glUniform2f(location, x, y)
        }
    }

    open inner class Uniform3f(
        name: String,
        var x: Float = 0f,
        var y: Float = 0f,
        var z: Float = 0f
    ) : Uniform(name) {
        override fun apply() {
            glUniform3f(location, x, y, z)
        }
    }

    open inner class Uniform4f(
        name: String,
        var x: Float = 0f,
        var y: Float = 0f,
        var z: Float = 0f,
        var w: Float = 0f
    ) : Uniform(name) {
        override fun apply() {
            glUniform4f(location, x, y, z, w)
        }
    }

    open inner class UniformColor(
        name: String,
        var color: Color = Color(0, 0, 0, 0)
    ) : Uniform(name) {
        override fun apply() {
            glUniform4f(
                location,
                color.red / 255f,
                color.green / 255f,
                color.blue / 255f,
                color.alpha / 255f
            )
        }
    }
}