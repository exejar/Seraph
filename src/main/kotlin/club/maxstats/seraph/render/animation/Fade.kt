package club.maxstats.seraph.render.animation

import club.maxstats.kolour.util.Color
import club.maxstats.seraph.util.now
import kotlin.math.floor

class Fade(
    val startColor: Color,
    val desColor: Color,
    val duration: Long,
    val delay: Long = 0
) {
    var curColor: Color = startColor
    var genesis = now()
    var lastUpdate = genesis

    fun update() {
        if (now() - genesis > delay) {
            if (curColor.toHex() != desColor.toHex()) {
                val timeElapsed = lastUpdate - genesis - delay

                val t = step(timeElapsed.toFloat(), duration.toFloat())

                val r = mix(startColor.red, desColor.red, t)
                val g = mix(startColor.green, desColor.green, t)
                val b = mix(startColor.blue, desColor.blue, t)
                val a = mix(startColor.alpha, desColor.alpha, t)

                curColor = curColor.copy(red = r, green = g, blue = b, alpha = a)
                lastUpdate = now()
            }
        }
    }

    private fun mix(start: Int, end: Int, t: Float): Int {
        return floor((end - start) * t + start).toInt()
    }

    private fun step(time: Float, duration: Float): Float {
        val t = time / duration
        return t * t * (3f - 2f * t)
    }

    fun isStopped() : Boolean {
        return curColor.toHex() == desColor.toHex()
    }
}