package club.maxstats.seraph.event

import net.weavemc.loader.api.event.Event

sealed class JoinGameEvent: Event() {
    class Pre: JoinGameEvent()
    class Post: JoinGameEvent()
}