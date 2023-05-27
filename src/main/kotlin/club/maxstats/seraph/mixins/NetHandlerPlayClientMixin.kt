package club.maxstats.seraph.mixins

import club.maxstats.seraph.event.JoinGameEvent
import club.maxstats.seraph.util.deserializeLocraw
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.play.server.S02PacketChat
import net.weavemc.loader.api.event.EventBus
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(NetHandlerPlayClient::class)
class NetHandlerPlayClientMixin {
    @Inject(method = ["handleChat"], at = [At("HEAD")])
    fun handleChatInject(chat: S02PacketChat, ci: CallbackInfo) {
        if (chat.chatComponent.unformattedText.startsWith("{\"server\":"))
            chat.chatComponent.unformattedText.deserializeLocraw()
    }
    @Inject(method = ["handleJoinGame"], at = [At("HEAD")])
    fun joinGameEventPreInject(ci: CallbackInfo) {
        EventBus.callEvent(JoinGameEvent.Pre())
    }
    @Inject(method = ["handleJoinGame"], at = [At("TAIL")])
    fun joinGameEventPostInject(ci: CallbackInfo) {
        EventBus.callEvent(JoinGameEvent.Post())
    }
}