package club.maxstats.seraph.util

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

@Serializable
data class LocrawInfo(
    @SerializedName("server") val server: String = "",

    /* Present when you're in a lobby */
    @SerializedName("gametype") val gameType: GameType = GameType.UNKNOWN,
    @SerializedName("lobbyname") val lobbyName: String = "",

    /* Present when you're in a game */
    @SerializedName("mode") val mode: String = "",
    @SerializedName("map") val map: String = ""
)
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = GameType::class)
object GameTypeSerializer : KSerializer<GameType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GameType", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): GameType {
        val gameTypeStr = decoder.decodeString()
        return GameType.values().firstOrNull { it.gameType == gameTypeStr }
            ?: GameType.UNKNOWN
    }
    override fun serialize(encoder: Encoder, value: GameType) {
        encoder.encodeString(value.gameType)
    }
}

val locrawJson = Json { ignoreUnknownKeys = true }
fun deserializeLocraw(json: String) {
    locrawInfo = locrawJson.decodeFromString<LocrawInfo>(json)
}
var locrawInfo: LocrawInfo = LocrawInfo("", GameType.UNKNOWN, "", "", "")

/**
 * @author Scherso ([...](https://github.com/scherso/))
 */
@Serializable(with = GameTypeSerializer::class)
enum class GameType(val gameType: String) {
    ARCADE_GAMES   ("ARCADE"),
    BEDWARS        ("BEDWARS"),
    BLITZ_SG       ("SURVIVAL_GAMES"),
    BUILD_BATTLE   ("BUILD_BATTLE"),
    CLASSIC_GAMES  ("LEGACY"),
    COPS_AND_CRIMS ("MCGO"),
    DUELS          ("DUELS"),
    HOUSING        ("HOUSING"),
    LIMBO          ("LIMBO"),
    MAIN           ("MAIN"),
    MEGA_WALLS     ("WALLS3"),
    MURDER_MYSTERY ("MURDER_MYSTERY"),
    PIT            ("PIT"),
    PROTOTYPE      ("PROTOTYPE"),
    SKYBLOCK       ("SKYBLOCK"),
    SKYWARS        ("SKYWARS"),
    SMASH_HEROES   ("SUPER_SMASH"),
    SPEED_UHC      ("SPEED_UHC"),
    TNT_GAMES      ("TNTGAMES"),
    UHC_CHAMPIONS  ("UHC"),
    UNKNOWN        (""),
    WARLORDS       ("BATTLEGROUND")
}