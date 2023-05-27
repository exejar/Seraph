package club.maxstats.seraph.stats

import club.maxstats.seraph.event.JoinGameEvent
import club.maxstats.seraph.util.getOrPutPlayerData
import net.minecraft.entity.player.EntityPlayer
import net.weavemc.loader.api.event.ChatReceivedEvent
import net.weavemc.loader.api.event.EntityListEvent
import net.weavemc.loader.api.event.SubscribeEvent

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
            try {
                if (event.entity.uniqueID.version() == 4) {
                    if (grabStats)
                        getOrPutPlayerData(event.entity.uniqueID)
                }
            } catch (ignored: Exception) {}
        }
    }
}