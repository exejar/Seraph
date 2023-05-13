package club.maxstats.seraph.util

import club.maxstats.seraph.Main
import java.io.InputStream

fun String.asResource() : InputStream = Main::class.java.classLoader.getResourceAsStream(this)
    ?: throw IllegalStateException("Failed to fetch resource $this")
fun now() = System.currentTimeMillis()
fun InputStream.readToString() = this.readBytes().toString(Charsets.UTF_8)