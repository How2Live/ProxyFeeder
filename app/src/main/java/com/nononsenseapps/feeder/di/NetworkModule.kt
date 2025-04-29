package com.nononsenseapps.feeder.di

import com.nononsenseapps.feeder.model.*          // FeedParser, … 
import com.nononsenseapps.feeder.sync.SyncRestClient
import com.nononsenseapps.jsonfeed.*              // Feed, JsonFeedParser, feedAdapter
import com.squareup.moshi.JsonAdapter
import okhttp3.OkHttpClient
import org.kodein.di.*                            // DI, bind, provider, singleton
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Alles, was übers Netz geht, kommt durch *diesen* Client – mit Tor-Proxy.
 */
val networkModule = DI.Module(name = "network", allowSilentOverride = true) {

    // ─── EINZIGER OkHttpClient ────────────────────────────────────────────────
    bind<OkHttpClient>(overrides = true) with singleton {
        OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 9050)))
            .build()
    }

    // ─── Parser können Zustand halten → provider ─────────────────────────────
    bind<JsonAdapter<Feed>>()      with provider { feedAdapter() }
    bind<JsonFeedParser>()         with provider { JsonFeedParser(instance(), instance()) }
    bind<FeedParser>()             with provider { FeedParser(di) }

    // ─── Stateless → singleton ───────────────────────────────────────────────
    bind<SyncRestClient>() with singleton { SyncRestClient(di) }
    bind<RssLocalSync>()   with singleton { RssLocalSync(di) }
    bind<FullTextParser>() with singleton { FullTextParser(di) }
}
