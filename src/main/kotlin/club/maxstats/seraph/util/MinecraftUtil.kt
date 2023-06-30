package club.maxstats.seraph.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.client.network.NetworkPlayerInfo
import java.lang.reflect.Field
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap
import kotlin.experimental.or

/**
 * Creates a Minecraft GameProfile using a player's username
 */
fun String.createGameProfile(): GameProfile? {
    val userJson = URL("https://api.mojang.com/users/profiles/minecraft/$this").fetchResponse()
        ?: return null
    val mcUser = Json.decodeFromString<MinecraftUser>(userJson)

    val profileJson = URL("https://sessionserver.mojang.com/session/minecraft/profile/${mcUser.id}?unsigned=false").fetchResponse()
        ?: return null
    val mcProfile = Json.decodeFromString<MinecraftProfile>(profileJson)

    val gameProfile = GameProfile(
        UUID.nameUUIDFromBytes(
            mcProfile.id.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        ),
        mcProfile.name
    )

    mcProfile.properties?.let {
        it.forEach { property ->
            gameProfile.properties.put(
                property.name,
                Property(
                    property.name,
                    property.value,
                    property.signature
                )
            )
        }
    }

    return gameProfile
}

fun GameProfile.createEntity(): EntityOtherPlayerMP {
    var playerInfoMap: Field? = null
    for (field in NetHandlerPlayClient::class.java.declaredFields) {
        if (field.name === "playerInfoMap")
            playerInfoMap = field
    }

    if (playerInfoMap != null) {
        playerInfoMap.isAccessible = true
        (playerInfoMap.get(mc.netHandler) as HashMap<UUID, NetworkPlayerInfo>)[this.id] = NetworkPlayerInfo(this)
    }

    val entityPlayer = EntityOtherPlayerMP(
        mc.theWorld,
        this
    )

    // Set the all skin layers to true, to force render all layers
    val dataWatcher = entityPlayer.dataWatcher
    val currentValue = dataWatcher.getWatchableObjectByte(10)
    val newValue = currentValue or 0xFF.toByte()
    dataWatcher.updateObject(10, newValue)

    return entityPlayer
}

@Serializable
private data class MinecraftUser(
    val id: String,
    val name: String
)

@Serializable
private data class MinecraftProfile(
    val id: String,
    val name: String,
    val properties: List<ProfileProperties>?
)

@Serializable
private data class ProfileProperties(
    val name: String,
    val value: String,
    val signature: String
)

private fun URL.fetchResponse() : String? {
    with(this.openConnection() as HttpURLConnection) {
        connect()

        if (responseCode != HttpURLConnection.HTTP_OK)
            return null

        return inputStream.bufferedReader().use { it.readText() }.also { disconnect() }
    }
}