package club.maxstats.seraph.mixins

import club.maxstats.seraph.util.deserializeLocraw
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.play.server.S02PacketChat
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(NetHandlerPlayClient::class)
class LocrawMixin {
    @Inject(method = ["handleChat"], at = [At("HEAD")])
    fun handleChatInject(chat: S02PacketChat, ci: CallbackInfo) {
        if (chat.chatComponent.formattedText.startsWith("{\"server\":"))
            chat.chatComponent.formattedText.deserializeLocraw()
    }
}