package club.maxstats.seraph.util

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LocrawInfo(
    @SerializedName("server") val server: String,

    /* Present when you're in a lobby */
    @SerializedName("gametype") val gameType: GameType,
    @SerializedName("lobbyname") val lobbyName: String,

    /* Present when you're in a game */
    @SerializedName("mode") val mode: String,
    @SerializedName("map") val map: String
)

var locrawInfo: LocrawInfo = LocrawInfo("", GameType.UNKNOWN, "", "", "")

/**
 * @author Scherso ([...](https://github.com/scherso/))
 */
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