package club.maxstats.seraph.stats.features

import club.maxstats.hyko.Player
import club.maxstats.seraph.render.Color
import club.maxstats.seraph.render.drawBlurredRect
import club.maxstats.seraph.render.drawRoundedRect
import club.maxstats.seraph.stats.BedwarsFormatting
import club.maxstats.seraph.stats.getPlayerTag
import club.maxstats.seraph.util.*
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.GuiPlayerTabOverlay
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.world.WorldSettings

class TabStatsGui(minecraft: Minecraft, guiIngame: GuiIngame) : GuiPlayerTabOverlay(minecraft, guiIngame) {
    override fun renderPlayerlist(width: Int, scoreboard: Scoreboard?, objective: ScoreObjective?) {
        if (locrawInfo.inGame() && statTitles.isNotEmpty()) renderStatTab(width, scoreboard, objective)
        else super.renderPlayerlist(width, scoreboard, objective)
    }

    private val playerOrdering = Ordering.from(PlayerComparator())
    private val entryHeight = 10f
    private val backgroundBorderSize = 10f
    private val headSize = 10
    private val maxNameLength = mc.fontRendererObj.getStringWidth("${ChatColor.BOLD}WWWWWWWWWWWWWWWW")
    private val playerTagLength = mc.fontRendererObj.getStringWidth("${ChatColor.BOLD}WWWWWWWW")
    private fun renderStatTab(screenWidth: Int, scoreboard: Scoreboard?, objective: ScoreObjective?) {
        val netHandler = mc.netHandler
        /* Grab downwards of 40 players, since we don't wrap players in the custom tab list */
        val playerList = playerOrdering.sortedCopy(netHandler.playerInfoMap).subList(0,
            netHandler.playerInfoMap.size.coerceAtMost(40)
        )

        val width = (headSize + 2) * 2 +
                    maxNameLength + 10 +
                    (playerTagLength + 10) +
                    statTitles.fold(0) { acc, title -> acc + mc.fontRendererObj.getStringWidth(title) + 10 }

        var nameWidth = 0
        var objectiveWidth = 0

        val startingX = screenWidth / 2f - width / 2f
        val startingY = 24f

        val outerColor = Color(0,0,0,100)
        val innerColor = Color(255,255,255, 100)

        GlStateManager.pushMatrix()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        /* background */
        drawBlurredRect(
            startingX - this.backgroundBorderSize,
            startingY - this.backgroundBorderSize,
            width + this.backgroundBorderSize * 2,
            (playerList.size + 1) * (this.entryHeight + 1) - 1 + this.backgroundBorderSize * 2,
            6f
        )
        drawRoundedRect(
            startingX - this.backgroundBorderSize,
            startingY - this.backgroundBorderSize,
            width + this.backgroundBorderSize * 2,
            (playerList.size + 1) * (this.entryHeight + 1) - 1 + this.backgroundBorderSize * 2,
            6f,
            outerColor
        )
        drawRoundedRect(
            startingX - this.backgroundBorderSize,
            startingY - this.backgroundBorderSize,
            width + this.backgroundBorderSize * 2,
            (playerList.size + 1) * (this.entryHeight + 1) - 1 + this.backgroundBorderSize * 2,
            6f,
            outerColor
        )
        /* title-bar for stat names */
        drawRoundedRect(
            startingX,
            startingY,
            width.toFloat(),
            entryHeight,
            6f, 6f, 0f, 0f,
            innerColor
        )

        var xSpacer = startingX + headSize + 2

        mc.fontRendererObj.drawStringWithShadow("${ChatColor.BOLD}NAME", xSpacer, startingY + (entryHeight / 2 - 4), Color.white.toRGBA())
        /* Longest possible name */
        xSpacer += maxNameLength + 10

        mc.fontRendererObj.drawStringWithShadow("${ChatColor.BOLD}TAG", xSpacer, startingY + (entryHeight / 2 - 4), Color.white.toRGBA())
        xSpacer += playerTagLength + 10

        val statTitles = statTitles
        statTitles.forEach {
            mc.fontRendererObj.drawStringWithShadow(
                "${ChatColor.BOLD}$it",
                xSpacer,
                startingY + (entryHeight / 2 - 4),
                Color.white.toRGBA()
            )
            xSpacer += mc.fontRendererObj.getStringWidth("${ChatColor.BOLD}$it") + 10
        }

        var ySpacer = startingY + entryHeight + 1
        playerList.forEach {
            xSpacer = startingX

            /* draw rounded bottom edges, otherwise draw no rounded edges */
            if (playerList.indexOf(it) == playerList.size - 1)
                drawRoundedRect(
                    xSpacer,
                    ySpacer,
                    width.toFloat(),
                    entryHeight,
                    0f, 0f, 6f, 6f,
                    innerColor
                )
            else
                drawRoundedRect(
                    xSpacer,
                    ySpacer,
                    width.toFloat(),
                    entryHeight,
                    0f, 0f, 0f, 0f,
                    innerColor
                )

            val playerName = getPlayerName(it)
            val gameProfile = it.gameProfile

            if (mc.isIntegratedServerRunning || mc.netHandler.networkManager.isencrypted) {
                /* Render Player Face */
                val entityPlayer = mc.theWorld.getPlayerEntityByUUID(gameProfile.id)
                val upsideDown = entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.name == "Dinnerbone" || gameProfile.name == "Grumm")

                mc.textureManager.bindTexture(it.locationSkin)

                val u = 8 + (if (upsideDown) 8 else 0)
                val v = 8 * (if (upsideDown) -1 else 1)

                Gui.drawScaledCustomSizeModalRect(xSpacer.toInt(), ySpacer.toInt(), 8f, u.toFloat(), 8, v, headSize, headSize, 64f, 64f)

                if (entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.HAT))
                    Gui.drawScaledCustomSizeModalRect(xSpacer.toInt(), ySpacer.toInt(), 40f, u.toFloat(), 8, v, headSize, headSize, 64f, 64f)

                xSpacer += headSize + 2
            }

            if (it.gameType == WorldSettings.GameType.SPECTATOR) {
                // idk
            } else {
                xSpacer = startingX + headSize + 2

                /* Render Player Name */
                mc.fontRendererObj.drawStringWithShadow(playerName, xSpacer, ySpacer + (entryHeight / 2 - 4), -1)
                xSpacer += maxNameLength + 10

                /* Render Player Tag */
                val playerTag = getPlayerTag(gameProfile.id)
                mc.fontRendererObj.drawStringWithShadow(playerTag.formattedName, xSpacer, ySpacer + (entryHeight / 2 - 4), playerTag.color.toRGBA())
                xSpacer += playerTagLength + 10

                val playerData = getPlayerData(gameProfile.id)
                if (playerData != null) {
                    val statList = playerData.statList
                    statList.forEachIndexed { index, stat ->
                        mc.fontRendererObj.drawStringWithShadow(stat, xSpacer, ySpacer + (entryHeight / 2 - 4), Color.white.toRGBA())
                        xSpacer += mc.fontRendererObj.getStringWidth("${ChatColor.BOLD}${statTitles[index]}") + 10
                    }
                }
            }

            ySpacer += entryHeight + 1
        }

        GlStateManager.popMatrix()
    }

    private val statTitles: List<String>
        get() = when (locrawInfo.gameType) {
            GameType.BEDWARS -> mutableListOf(
                "STAR",
                "WS",
                "WLR",
                "FKDR",
                "BBLR",
            )
            GameType.DUELS -> mutableListOf(
                "TITLE",
                "WS",
                "BWS",
                "WLR",
                "KDR",
                "WINS",
                "KILLS",
            )
            else -> emptyList()
        }

    private val Player.statList: List<String>
        get() = when (locrawInfo.gameType) {
            GameType.BEDWARS -> {
                /* Maybe update this to include formatting in the cache, so we don't constantly format over and over again
                (although this might mess up the star wave animation */
                val formatted = BedwarsFormatting(this)
                mutableListOf(
                    formatted.formattedStar,
                    formatted.formattedWinstreak,
                    formatted.formattedWLR,
                    formatted.formattedFKDR,
                    formatted.formattedBBLR
                )
            }
            GameType.DUELS -> mutableListOf(
                "TITLE",
                "WS",
                "BWS",
                "WLR",
                "KDR",
                "WINS",
                "KILLS",
            )
            else -> emptyList()
        }
}

class PlayerComparator : Comparator<NetworkPlayerInfo> {
    override fun compare(o1: NetworkPlayerInfo, o2: NetworkPlayerInfo): Int {
        val team1 = o1.playerTeam
        val team2 = o2.playerTeam
        return ComparisonChain.start().compareTrueFirst(
            o1.gameType != WorldSettings.GameType.SPECTATOR,
            o2.gameType != WorldSettings.GameType.SPECTATOR
        ).compare(
            team1?.registeredName ?: "",
            team2?.registeredName ?: ""
        ).compare(
            o1.gameProfile.name,
            o2.gameProfile.name
        ).result()
    }
}