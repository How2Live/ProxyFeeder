// app/src/main/java/com/nononsenseapps/feeder/di/NetworkModule.kt
package com.nononsenseapps.feeder.di

import java.net.InetSocketAddress
import java.net.Proxy
import okhttp3.OkHttpClient
import org.kodein.di.*

val networkModule = DI.Module(
    name = "network",
    // erlaubt, vorhandene Bindungen mit den gleichen Keys zu ersetzen
    allowSilentOverride = true
) {
    /* ───── 1. GLOBALER OKHTTP-CLIENT MIT TOR ───── */
    bind<OkHttpClient>(overrides = true) with singleton {
        OkHttpClient.Builder()
            .proxy(
                Proxy(
                    Proxy.Type.SOCKS,
                    InetSocketAddress("127.0.0.1", 9050)   // Orbot-/Tor-Port
                )
            )
            .build()
    }

    /* ───── 2. REST DER ORIGINALEN BINDUNGEN ───── */
    bind<JsonAdapter<Feed>>() with provider { feedAdapter() }
    bind<JsonFeedParser>()    with provider { JsonFeedParser(instance(), instance()) }
    bind<FeedParser>()        with provider { FeedParser(di) }

    bind<SyncRestClient>()    with singleton { SyncRestClient(di) }
    bind<RssLocalSync>()      with singleton { RssLocalSync(di) }
    bind<FullTextParser>()    with singleton { FullTextParser(di) }
}
