package club.maxstats.seraph.mixins

import club.maxstats.seraph.event.ResizeWindowEvent
import net.minecraft.client.Minecraft
import net.weavemc.loader.api.event.EventBus
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
class MinecraftMixin {
    @Inject(method = ["updateFramebufferSize"], at = [At("HEAD")])
    fun updateFramebufferSizeInject(ci: CallbackInfo) {
        EventBus.callEvent(ResizeWindowEvent())
    }
}