package club.maxstats.seraph.render.animation

import club.maxstats.seraph.util.isCloseTo
import club.maxstats.seraph.util.now
import kotlin.math.pow

class Spring(
    val tension: Float = 0.0f,
    val friction: Float = 0.0f,
    val timeScale: Float = 0.0f,

    var current: Float = 0.0f,
    var target: Float = 0.0f,
    var motion: Float = 0.0f,

    private var lastTime: Long = now()
) {
    fun update() {
        // check for non-moving
        if (isStopped()) {
            current = target
            return
        }

        val currentTime = now()
        val difference = currentTime - lastTime
        // check for no difference in time
        if (difference == 0L) return

        val delta = difference / timeScale
        lastTime = currentTime

        // spring physics
        motion += tension * (target - current) * delta
        motion *= friction.pow(delta)
        current += motion * delta
    }

    fun isStopped() = current isCloseTo target && motion isCloseTo 0.0f

    fun reset() {
        current = 0.0f
        motion = 0.0f
        target = 0.0f
    }

    fun dampen() {
        current = target
        motion = 0.0f
    }
}

class Spring2D(
    val x: Spring,
    val y: Spring
) {
    constructor(tension: Float, friction: Float, timeScale: Float) : this(
        Spring(tension, friction, timeScale),
        Spring(tension, friction, timeScale)
    )

    fun update() {
        x.update()
        y.update()
    }

    fun isStopped() = x.isStopped() && y.isStopped()

    fun dampen() {
        x.dampen()
        y.dampen()
    }

    fun reset() {
        x.reset()
        y.reset()
    }

    fun target(x: Float, y: Float) {
        this.x.target = x
        this.y.target = y
    }
}
