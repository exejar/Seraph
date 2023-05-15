package club.maxstats.seraph

import club.maxstats.seraph.render.AlertRender
import club.maxstats.seraph.render.FontManager
import club.maxstats.seraph.render.Position
import club.maxstats.seraph.render.none
import club.maxstats.seraph.util.*
import club.maxstats.weave.loader.api.ModInitializer
import club.maxstats.weave.loader.api.event.ChatReceivedEvent
import club.maxstats.weave.loader.api.event.EventBus
import club.maxstats.weave.loader.api.event.PlayerListEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import java.io.File

lateinit var fontManager: FontManager
class Main : ModInitializer {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    override fun init() {
        fontManager = FontManager()
        EventBus.subscribe(this)
        EventBus.subscribe(ApiKey())
        EventBus.subscribe(AlertRender())
    }

    @SubscribeEvent
    fun onListAdd(event: PlayerListEvent.Add) {
        println("List Add")
        if (locrawInfo.inGame()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${event.playerData.profile.name} grabbing stats"))

            coroutineScope.launch {
                val playerData = event.playerData.profile.id.getPlayerData() ?: return@launch
                none("${playerData.displayName}'s Stats", "${playerData.stats.bedwars.overall.finalKills} Final Kills", 4000L, Position.BOTTOM_RIGHT)
            }
        }
    }
}