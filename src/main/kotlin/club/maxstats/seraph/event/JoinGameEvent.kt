package club.maxstats.seraph.event

import club.maxstats.weave.loader.api.event.Event

sealed class JoinGameEvent: Event() {
    class Pre: JoinGameEvent()
    class Post: JoinGameEvent()
}