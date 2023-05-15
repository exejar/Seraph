package club.maxstats.seraph.mixins

import club.maxstats.seraph.event.RenderLastEvent
import club.maxstats.weave.loader.api.event.EventBus
import net.minecraft.client.renderer.EntityRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(EntityRenderer::class)
class EntityRendererMixin {
    @Inject(method = ["updateCameraAndRender"], at = [At("TAIL")])
    fun renderLast(ci: CallbackInfo) {
        EventBus.callEvent(RenderLastEvent())
    }
}