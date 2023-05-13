package club.maxstats.seraph.util

import club.maxstats.seraph.Main
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream

val safeJson = Json { ignoreUnknownKeys = true }
fun String.asResource() : InputStream = Main::class.java.classLoader.getResourceAsStream(this)
    ?: throw IllegalStateException("Failed to fetch resource $this")
fun now() = System.currentTimeMillis()
fun InputStream.readToString() = this.readBytes().toString(Charsets.UTF_8)
fun String.deserializeLocraw() {
    locrawInfo = safeJson.decodeFromString<LocrawInfo>(this)
}