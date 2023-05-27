package club.maxstats.seraph

import club.maxstats.seraph.render.AlertRender
import club.maxstats.seraph.render.FontManager
import club.maxstats.seraph.render.ShapeRenderer
import club.maxstats.seraph.stats.StatCacheProvider
import club.maxstats.seraph.util.*
import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.event.EventBus
import net.weavemc.loader.api.event.StartGameEvent
import net.weavemc.loader.api.event.SubscribeEvent

lateinit var fontManager: FontManager

class Main : ModInitializer {

    override fun preInit() {
        EventBus.subscribe(this)
    }

    @SubscribeEvent
    fun init(event: StartGameEvent.Post) {
        fontManager = FontManager()
        EventBus.subscribe(ApiKey())
        EventBus.subscribe(AlertRender())
        EventBus.subscribe(StatCacheProvider())
        EventBus.subscribe(ShapeRenderer())
    }
}