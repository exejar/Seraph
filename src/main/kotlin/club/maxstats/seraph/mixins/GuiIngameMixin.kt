package club.maxstats.seraph.mixins

import club.maxstats.seraph.stats.features.TabStatsGui
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.GuiPlayerTabOverlay
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GuiIngame::class)
class GuiIngameMixin {
    @JvmField @Shadow var overlayPlayerList : GuiPlayerTabOverlay = GuiPlayerTabOverlay(Minecraft.getMinecraft(), this as GuiIngame)
    @Inject(method = ["<init>"], at = [At("TAIL")])
    private fun statOverlay(mc: Minecraft, ci: CallbackInfo) {
        overlayPlayerList = TabStatsGui(mc, this as GuiIngame)
    }
}