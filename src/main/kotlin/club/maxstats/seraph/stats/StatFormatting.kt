package club.maxstats.seraph.stats

import club.maxstats.hyko.Bedwars
import club.maxstats.hyko.Player
import club.maxstats.seraph.util.ChatColor
import club.maxstats.seraph.util.getStatRatio

class BedwarsFormatting(player: Player) {
    val formattedFKDR: String
    val formattedStar: String
    val formattedWinstreak: String
    val formattedWLR: String
    val formattedBBLR: String
    init {
        val bedwars = player.stats.bedwars

        formattedStar = formatStars(player.achievements.bedwarsLevel)
        formattedWinstreak = formatWinstreak(bedwars.overall.winstreak)
        formattedFKDR = formatFKDR(
            getStatRatio(
                bedwars.overall.finalKills,
                bedwars.overall.finalDeaths
            )
        )
        formattedWLR = formatWLR(
            getStatRatio(
                bedwars.overall.wins,
                bedwars.overall.losses
            )
        )
        formattedBBLR = formatBBLR(
            getStatRatio(
                bedwars.overall.bedsBroken,
                bedwars.overall.bedsLost
            )
        )
    }

    private val colors = arrayOf(
        ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.AQUA, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE
    )
    private val prestigeColors = arrayOf(
        ChatColor.WHITE, ChatColor.YELLOW, ChatColor.AQUA, ChatColor.GREEN, ChatColor.DARK_AQUA, ChatColor.RED,
        ChatColor.LIGHT_PURPLE, ChatColor.BLUE, ChatColor.DARK_PURPLE, ChatColor.DARK_GRAY
    )
    private fun getStarColor(star: Int): ChatColor {
        return when {
            star < 100 -> ChatColor.GRAY
            star < 200 -> ChatColor.WHITE
            star < 300 -> ChatColor.GOLD
            star < 400 -> ChatColor.AQUA
            star < 500 -> ChatColor.DARK_GREEN
            star < 600 -> ChatColor.DARK_AQUA
            star < 700 -> ChatColor.DARK_RED
            star < 800 -> ChatColor.LIGHT_PURPLE
            star < 900 -> ChatColor.BLUE
            else -> ChatColor.DARK_PURPLE
        }
    }
    fun formatStars(star: Int): String {
        val starString = star.toString()

        return if (star < 1000) {
            "${getStarColor(star)}$starString\u272B"
        } else {
            val colorAmount: Int
            val starUnicode: String

            when {
                star < 1100 -> {
                    colorAmount = 7
                    starUnicode = "\u272A"
                    colors.fill(ChatColor.WHITE, 0, 4)
                }
                star < 2000 -> {
                    colorAmount = 5
                    starUnicode = "\u272A"
                    when {
                        star < 1200 -> colors.fill(ChatColor.WHITE, 0, 4)
                        star < 1300 -> colors.fill(ChatColor.YELLOW, 0, 4)
                        star < 1400 -> colors.fill(ChatColor.AQUA, 0, 4)
                        star < 1500 -> colors.fill(ChatColor.GREEN, 0, 4)
                        star < 1600 -> colors.fill(ChatColor.DARK_AQUA, 0, 4)
                        star < 1700 -> colors.fill(ChatColor.RED, 0, 4)
                        star < 1800 -> colors.fill(ChatColor.LIGHT_PURPLE, 0, 4)
                        star < 1900 -> colors.fill(ChatColor.BLUE, 0, 4)
                        else -> colors.fill(ChatColor.DARK_PURPLE, 0, 4)
                    }
                } else -> {
                    colorAmount = 6
                    starUnicode = "\u269D"
                    when {
                        star < 2100 -> prestigeColors.copyInto(colors)
                        star < 2200 -> { colors.fill(ChatColor.WHITE, 0, 2); colors.fill(ChatColor.YELLOW, 2, 4) }
                        star < 2300 -> prestigeColors.copyInto(colors, 0, 0, 5)
                        star < 2400 -> prestigeColors.copyInto(colors, 0, 1, 6)
                        star < 2500 -> { colors.fill(ChatColor.AQUA, 0, 2); colors.fill(ChatColor.WHITE, 2, 4) }
                        star < 2600 -> { colors.fill(ChatColor.WHITE, 0, 2); colors.fill(ChatColor.GREEN, 2, 4) }
                        star < 2700 -> prestigeColors.copyInto(colors, 0, 2, 7)
                        star < 2800 -> { colors.fill(ChatColor.YELLOW, 0, 2); colors.fill(ChatColor.WHITE, 2, 4) }
                        star < 2900 -> prestigeColors.copyInto(colors, 0, 3, 8)
                        star < 3000 -> prestigeColors.copyInto(colors, 0, 4, 9)
                        else -> prestigeColors.copyInto(colors, 0, 5, 10)
                    }
                }
            }

            val starWave = (System.currentTimeMillis() % 850L / 850.0F * colorAmount).toLong()

            return buildString {
                append(colors[(starWave + 4).toInt() % colorAmount])
                append(starString[0])
                append(colors[(starWave + 3).toInt() % colorAmount])
                append(starString.substring(1, starString.length))
                append(starUnicode)
            }
        }
    }

    fun formatWinstreak(winstreak: Int): String {
        return when {
            winstreak < 5 -> "${ChatColor.GRAY}$winstreak"
            winstreak < 10 -> "${ChatColor.WHITE}$winstreak"
            winstreak < 20 -> "${ChatColor.GOLD}$winstreak"
            winstreak < 35 -> "${ChatColor.DARK_GREEN}$winstreak"
            winstreak < 50 -> "${ChatColor.RED}$winstreak"
            winstreak < 75 -> "${ChatColor.DARK_RED}$winstreak"
            winstreak < 100 -> "${ChatColor.LIGHT_PURPLE}$winstreak"
            else -> "${ChatColor.DARK_PURPLE}$winstreak"
        }
    }
    fun formatWLR(wlr: Double): String {
        return when {
            wlr < 1 -> "${ChatColor.GRAY}$wlr"
            wlr < 2 -> "${ChatColor.WHITE}$wlr"
            wlr < 3 -> "${ChatColor.GOLD}$wlr"
            wlr < 5 -> "${ChatColor.DARK_GREEN}$wlr"
            wlr < 10 -> "${ChatColor.RED}$wlr"
            wlr < 15 -> "${ChatColor.DARK_RED}$wlr"
            wlr < 50 -> "${ChatColor.LIGHT_PURPLE}$wlr"
            else -> "${ChatColor.DARK_PURPLE}$wlr"
        }
    }
    fun formatFKDR(fkdr: Double): String {
        return when {
            fkdr < 1.5 -> "${ChatColor.GRAY}$fkdr"
            fkdr < 3.5 -> "${ChatColor.WHITE}$fkdr"
            fkdr < 5 -> "${ChatColor.GOLD}$fkdr"
            fkdr < 10 -> "${ChatColor.DARK_GREEN}$fkdr"
            fkdr < 20 -> "${ChatColor.RED}$fkdr"
            fkdr < 50 -> "${ChatColor.DARK_RED}$fkdr"
            fkdr < 100 -> "${ChatColor.LIGHT_PURPLE}$fkdr"
            else -> "${ChatColor.DARK_PURPLE}$fkdr"
        }
    }
    fun formatBBLR(bblr: Double): String {
        return when {
            bblr < 1.5 -> "${ChatColor.GRAY}$bblr"
            bblr < 2.5 -> "${ChatColor.WHITE}$bblr"
            bblr < 5 -> "${ChatColor.GOLD}$bblr"
            bblr < 7.5 -> "${ChatColor.DARK_GREEN}$bblr"
            bblr < 10 -> "${ChatColor.RED}$bblr"
            bblr < 15 -> "${ChatColor.DARK_RED}$bblr"
            bblr < 50 -> "${ChatColor.LIGHT_PURPLE}$bblr"
            else -> "${ChatColor.DARK_PURPLE}$bblr"
        }
    }
}

class DuelsFormatting {

}