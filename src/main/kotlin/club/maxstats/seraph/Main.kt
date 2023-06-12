package club.maxstats.seraph

import club.maxstats.kolour.render.FontManager
import club.maxstats.kolour.render.blurProgram
import club.maxstats.seraph.config.ConfigGui
import club.maxstats.seraph.event.ResizeWindowEvent
import club.maxstats.seraph.render.AlertRender
import club.maxstats.seraph.stats.StatCacheProvider
import club.maxstats.seraph.util.*
import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.event.EventBus
import net.weavemc.loader.api.event.KeyboardEvent
import net.weavemc.loader.api.event.StartGameEvent
import net.weavemc.loader.api.event.SubscribeEvent
import org.lwjgl.input.Keyboard

lateinit var fontManager: FontManager
lateinit var configGui: ConfigGui

class Main : ModInitializer {

    override fun preInit() {
        EventBus.subscribe(this)
    }

    @SubscribeEvent
    fun init(event: StartGameEvent.Post) {
        fontManager = FontManager()
        configGui = ConfigGui()
        EventBus.subscribe(ApiKey())
        EventBus.subscribe(AlertRender())
        EventBus.subscribe(StatCacheProvider())
        EventBus.subscribe(ResizeWindowEvent::class.java) {
            blurProgram.blurBuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight)
        }
        EventBus.subscribe(KeyboardEvent::class.java) {
            e -> if (e.keyCode == Keyboard.KEY_LEFT) mc.displayGuiScreen(configGui)
        }
    }
}