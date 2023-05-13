package club.maxstats.seraph.tabstats

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.GuiPlayerTabOverlay
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.Scoreboard

class TabStatsGui(minecraft: Minecraft, guiIngame: GuiIngame) : GuiPlayerTabOverlay(minecraft, guiIngame) {

    override fun renderPlayerlist(width: Int, scoreboard: Scoreboard?, objective: ScoreObjective?) {
        super.renderPlayerlist(width, scoreboard, objective)
    }
}