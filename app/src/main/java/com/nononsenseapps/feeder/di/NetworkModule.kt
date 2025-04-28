package com.nononsenseapps.feeder.di

import java.net.Proxy
import java.net.InetSocketAddress
import com.nononsenseapps.feeder.model.*
import com.nononsenseapps.feeder.sync.SyncRestClient
import com.nononsenseapps.jsonfeed.*
import com.squareup.moshi.JsonAdapter
import okhttp3.OkHttpClient
import org.kodein.di.*

val networkModule = DI.Module(name = "network") {

    // SOCKS-Proxy-Client
    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 9050)))
            .build()
    }

    // Parser-Abhängigkeiten
    bind<JsonAdapter<Feed>>() with provider { feedAdapter() }

    bind<JsonFeedParser>() with provider {
        JsonFeedParser(
            instance<OkHttpClient>(),           // ← Typ angegeben
            instance<JsonAdapter<Feed>>()       // ← Typ angegeben
        )
    }

    bind<FeedParser>()     with provider  { FeedParser(di) }
    bind<SyncRestClient>() with singleton { SyncRestClient(di) }
    bind<RssLocalSync>()   with singleton { RssLocalSync(di) }
    bind<FullTextParser>() with singleton { FullTextParser(di) }
}
