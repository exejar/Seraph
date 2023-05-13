package club.maxstats.seraph

import club.maxstats.seraph.util.apiKey
import club.maxstats.seraph.util.getPlayerData
import club.maxstats.seraph.util.isOnHypixel
import club.maxstats.seraph.util.locrawInfo
import club.maxstats.weave.loader.api.ModInitializer
import club.maxstats.weave.loader.api.event.ChatReceivedEvent
import club.maxstats.weave.loader.api.event.EventBus
import club.maxstats.weave.loader.api.event.PlayerListEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

class Main : ModInitializer {
    private val file = File("seraph_cache")

    override fun init() {
        EventBus.subscribe(this)
        apiKey = Json.decodeFromString(ApiKey.serializer(), file.readText()).apiKey
    }

    @SubscribeEvent
    suspend fun onListAdd(event: PlayerListEvent.Add) {
        if (locrawInfo.inGame()) {
            event.playerData.profile.id.getPlayerData()
        }
    }

    @SubscribeEvent
    fun onChat(event: ChatReceivedEvent) {
        if (event.message.unformattedText.startsWith("Your new API key is ") && isOnHypixel) {
            val key = event.message.unformattedText.replace("Your new API key is ", "")
            val json = Json.encodeToString(ApiKey.serializer(), ApiKey(key))
            file.writeText(json)
            apiKey = key
        }
    }

    @Serializable
    data class ApiKey(
        @SerialName("api_key") val apiKey: String
    )
}