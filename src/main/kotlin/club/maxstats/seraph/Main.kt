package club.maxstats.seraph

import club.maxstats.seraph.render.AlertRender
import club.maxstats.seraph.render.FontManager
import club.maxstats.seraph.stats.StatCacheProvider
import club.maxstats.seraph.util.*
import club.maxstats.weave.loader.api.ModInitializer
import club.maxstats.weave.loader.api.event.EventBus

lateinit var fontManager: FontManager

class Main : ModInitializer {

    override fun init() {
        fontManager = FontManager()
        EventBus.subscribe(ApiKey())
        EventBus.subscribe(AlertRender())
        EventBus.subscribe(StatCacheProvider())
    }
}