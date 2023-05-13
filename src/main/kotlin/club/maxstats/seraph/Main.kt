package club.maxstats.seraph

import club.maxstats.seraph.util.getPlayerData
import club.maxstats.seraph.util.locrawInfo
import club.maxstats.weave.loader.api.ModInitializer
import club.maxstats.weave.loader.api.event.EventBus
import club.maxstats.weave.loader.api.event.PlayerListEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent

class Main : ModInitializer {
    override fun init() {
        EventBus.subscribe(this)
    }

    @SubscribeEvent
    suspend fun onListAdd(event: PlayerListEvent.Add) {
        if (locrawInfo.inGame()) {
            event.playerData.profile.id.getPlayerData()
        }
    }
}