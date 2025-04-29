package com.nononsenseapps.feeder.di

import java.net.InetSocketAddress
import java.net.Proxy
import okhttp3.OkHttpClient

/* ── Feeder-Klassen ─────────────────────────────────────────────── */
import com.nononsenseapps.feeder.model.FeedParser
import com.nononsenseapps.feeder.model.FullTextParser
import com.nononsenseapps.feeder.model.RssLocalSync
import com.nononsenseapps.feeder.sync.SyncRestClient
/* ── JSON-Feed / Moshi ──────────────────────────────────────────── */
import com.nononsenseapps.jsonfeed.Feed
import com.nononsenseapps.jsonfeed.JsonFeedParser
import com.nononsenseapps.jsonfeed.feedAdapter
import com.squareup.moshi.JsonAdapter
/* ── Kodein DI ──────────────────────────────────────────────────── */
import org.kodein.di.*

val networkModule = DI.Module(
    name = "network",
    allowSilentOverride = true
) {
    /* 1. Globaler OkHttp-Client (Tor/Orbot) */
    bind<OkHttpClient>(overrides = true) with singleton {
        OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 9050)))
            .build()
    }

    /* 2. Parser & Sync-Klassen */
    bind<JsonAdapter<Feed>>() with provider { feedAdapter() }

    bind<JsonFeedParser>() with provider {
        JsonFeedParser(
            instance<OkHttpClient>(),
            instance<JsonAdapter<Feed>>()
        )
    }

    bind<FeedParser>()     with provider  { FeedParser(di) }
    bind<FullTextParser>() with singleton { FullTextParser(di) }
    bind<SyncRestClient>() with singleton { SyncRestClient(di) }
    bind<RssLocalSync>()   with singleton { RssLocalSync(di) }
}
