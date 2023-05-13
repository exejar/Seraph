package club.maxstats.seraph.util

import club.maxstats.seraph.Main
import getPlayerFromUUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import player.Player
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

private val safeJson = Json { ignoreUnknownKeys = true }
private val playerCache = HashMap<UUID, Player>()
fun String.asResource() : InputStream = Main::class.java.classLoader.getResourceAsStream(this)
    ?: throw IllegalStateException("Failed to fetch resource $this")
fun now() = System.currentTimeMillis()
fun InputStream.readToString() = this.readBytes().toString(Charsets.UTF_8)
fun String.deserializeLocraw() {
    locrawInfo = safeJson.decodeFromString<LocrawInfo>(this)
}
fun calculateScaleFactor(mc: Minecraft): Int {
    val displayWidth = mc.displayWidth
    val displayHeight = mc.displayHeight

    var scaleFactor = 1
    val isUnicode = mc.isUnicode

    val guiScale = mc.gameSettings.guiScale
    if (guiScale == 0) scaleFactor = 1000

    while (scaleFactor < guiScale && displayWidth / (scaleFactor + 1) >= 320 && displayHeight / (scaleFactor + 1) >= 240) ++scaleFactor
    if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) --scaleFactor

    return scaleFactor
}
suspend fun UUID.getPlayerData() : Player {
    return playerCache.getOrPut(this) {
        getPlayerFromUUID(this.toString(), "")
    }
}