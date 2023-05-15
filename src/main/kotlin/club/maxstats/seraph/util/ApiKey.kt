package club.maxstats.seraph.util

import club.maxstats.weave.loader.api.event.ChatReceivedEvent
import club.maxstats.weave.loader.api.event.SubscribeEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

class ApiKey {
    private val file = File("seraph_cache")
    init {
        try {
            hypixelApiKey = Json.decodeFromString(Config.serializer(), file.readText()).apiKey
        } catch (ex: Exception) {
            println("Cache file \"seraph_cache\" does not exist")
        }
    }
    @SubscribeEvent
    fun onChat(event: ChatReceivedEvent) {
        if (event.message.unformattedText.startsWith("Your new API key is ") && isOnHypixel) {
            val key = event.message.unformattedText.replace("Your new API key is ", "")
            val json = Json.encodeToString(Config.serializer(), Config(key))
            file.writeText(json)
            hypixelApiKey = key
        }
    }
    @Serializable
    private data class Config(
        @SerialName("api_key") val apiKey: String = ""
    )
}