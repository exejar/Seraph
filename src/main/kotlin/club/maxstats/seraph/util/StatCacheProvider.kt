package club.maxstats.seraph.util

import club.maxstats.seraph.event.JoinGameEvent
import club.maxstats.weave.loader.api.event.ChatReceivedEvent
import club.maxstats.weave.loader.api.event.EntityListEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer

class StatCacheProvider {
    var grabStats: Boolean = false
    var joinGameReset: Boolean = true
    @SubscribeEvent
    fun onJoin(event: JoinGameEvent.Pre) {
        if (joinGameReset)
            grabStats = false
    }
    @SubscribeEvent
    fun onChat(event: ChatReceivedEvent) {
        if (event.message.unformattedText.startsWith("Sending you to mini")) {
            grabStats = true
            joinGameReset = false
        } else if (!joinGameReset)
            joinGameReset = true
    }
    @SubscribeEvent
    fun onListAdd(event: EntityListEvent.Add) {
        if (event.entity is EntityPlayer) {
            if (event.entity.uniqueID.version() == 4) {
                if (grabStats)
                    getOrPutPlayerData(event.entity.uniqueID)
            } else {
                /* Nicked Player */

            }
        }
    }
}