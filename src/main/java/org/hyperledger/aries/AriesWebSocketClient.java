/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import lombok.Builder;
import lombok.Singular;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.webhook.AriesWebSocketListener;
import org.hyperledger.aries.webhook.EventHandler;
import org.hyperledger.aries.webhook.IEventHandler;
import org.hyperledger.aries.webhook.ReactiveEventHandler;

import javax.annotation.Nullable;
import java.util.*;

/**
 * ACA-PY Websocket Client: Receives events from aca-py
 */
public class AriesWebSocketClient extends ReactiveEventHandler {

    private final OkHttpClient client;
    private final String url;
    private final String apiKey;
    private final String bearerToken;
    private final List<IEventHandler> handler;
    private final List<String> walletIdFilter;

    private WebSocket webSocket;

    /**
     * Create a new websocket client, supports builder methods like: {@code AriesWebSocketClient.builder().build()}
     * @param url Optional: The aca-py ws URL e.g. ws(s)://host:[port]/ws, defaults to localhost
     * @param apiKey Optional: The admin api key if security is enabled
     * @param bearerToken Optional: The Bearer token used in the Authorization header when running in multi tenant mode
     * @param client Optional: {@link OkHttpClient} if null or not set a default client is created
     * @param handler Optional: None, one or many custom event handler implementations, defaults to {@link EventHandler.DefaultEventHandler}
     * @param walletIdFilter Optional: Filter events by provided walletId
     */
    @Builder
    public AriesWebSocketClient(@Nullable String url,
                                @Nullable String apiKey,
                                @Nullable String bearerToken,
                                @Nullable OkHttpClient client,
                                @Singular("handler") List<IEventHandler> handler,
                                @Nullable @Singular("walletId") List<String> walletIdFilter) {
        this.client = Objects.requireNonNullElseGet(client, OkHttpClient::new);
        this.url = StringUtils.isEmpty(url) ? "ws://localhost:8031/ws" : StringUtils.trim(url);
        this.apiKey = StringUtils.trimToEmpty(apiKey);
        this.bearerToken = StringUtils.trimToEmpty(bearerToken);
        this.walletIdFilter = walletIdFilter != null ? Collections.unmodifiableList(walletIdFilter) : null;

        List<IEventHandler> tempHandler = new ArrayList<>();
        tempHandler.add(this);
        tempHandler.addAll(handler.isEmpty() ? List.of(new EventHandler.DefaultEventHandler()) : handler);
        this.handler = Collections.unmodifiableList(tempHandler);

        openWebSocket();
    }

    private void openWebSocket() {
        Request.Builder b = new Request.Builder();
        b.url(url);
        if (apiKey != null) {
            b.header(BaseClient.X_API_KEY, apiKey);
        }
        if (bearerToken != null) {
            b.header(BaseClient.AUTHORIZATION, BaseClient.BEARER + bearerToken);
        }
        webSocket = client.newWebSocket(
                b.build(),
                AriesWebSocketListener.builder()
                        .handler(handler)
                        .walletIdFilter(walletIdFilter)
                        .build());
    }

    public void closeWebsocket() {
        if (webSocket != null) {
            webSocket.close(1001, null);
        }
    }
}
