package club.maxstats.seraph.util

import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class SimpleCache<K : Any, V>(
    private val maxSize: Int,
    private val timeToLive: Duration
) {
    private val cache = ConcurrentHashMap<K, CacheEntry<V>>()
    private class CacheEntry<V>(val value: V, val expiryTime: Instant)

    fun put(key: K, value: V) {
        val expiryTime = Instant.now().plus(timeToLive)
        cache.putIfAbsent(key, CacheEntry(value, expiryTime))
        if (cache.size > maxSize)
            cache.remove(cache.entries.iterator().next().key)
    }
    fun get(key: K): V? {
        val entry = cache[key] ?: return null
        if (entry.expiryTime.isBefore(Instant.now())) {
            cache.remove(key)
            return null
        }
        return entry.value
    }
    fun clear() {
        cache.clear()
    }
    fun size(): Int {
        return cache.size
    }
}