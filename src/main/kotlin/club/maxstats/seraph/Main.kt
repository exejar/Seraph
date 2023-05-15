package club.maxstats.seraph

import club.maxstats.seraph.render.AlertRender
import club.maxstats.seraph.render.FontManager
import club.maxstats.seraph.util.*
import club.maxstats.weave.loader.api.ModInitializer
import club.maxstats.weave.loader.api.event.EventBus
import club.maxstats.weave.loader.api.event.PlayerListEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent
import kotlinx.coroutines.*

lateinit var fontManager: FontManager
val statScope = CoroutineScope(Dispatchers.Default)
class Main : ModInitializer {

    override fun init() {
        fontManager = FontManager()
        EventBus.subscribe(this)
        EventBus.subscribe(ApiKey())
        EventBus.subscribe(AlertRender())
    }

    @SubscribeEvent
    fun onListAdd(event: PlayerListEvent.Add) {
        if (locrawInfo.inGame()) {
            statScope.launch {
                getPlayerData(event.playerData.profile.id)
            }
        }
    }
}