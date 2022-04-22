/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import lombok.Builder;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.webhook.AriesWebSocketListener;
import org.hyperledger.aries.webhook.EventHandler;
import org.hyperledger.aries.webhook.IEventHandler;

import javax.annotation.Nullable;

/**
 * ACA-PY Websocket Client: Receives events from aca-py
 */
public class AriesWebSocketClient extends BaseClient {

    private final String url;
    private final String apiKey;
    private final String bearerToken;
    private final IEventHandler handler;

    @Getter
    private WebSocket webSocket;

    /**
     * Create a new websocket client, supports builder methods like: {@code AriesWebSocketClient.builder().build()}
     * @param url Optional: The aca-py ws URL e.g. ws(s)://host:[port]/ws, defaults to localhost
     * @param apiKey Optional: The admin api key if security is enabled
     * @param bearerToken Optional: The Bearer token used in the Authorization header when running in multi tenant mode
     * @param client Optional: {@link OkHttpClient} if null or not set a default client is created
     * @param handler Optional: Custom event handler implementation, defaults to {@link EventHandler.DefaultEventHandler}
     */
    @Builder
    public AriesWebSocketClient(@Nullable String url, @Nullable String apiKey,
        @Nullable String bearerToken, @Nullable OkHttpClient client, @Nullable IEventHandler handler) {
        super(client);
        this.url = StringUtils.isEmpty(url) ? "ws://localhost:8031/ws" : StringUtils.trim(url);
        this.apiKey = StringUtils.trimToEmpty(apiKey);
        this.bearerToken = StringUtils.trimToEmpty(bearerToken);
        this.handler = handler != null ? handler : new EventHandler.DefaultEventHandler();
        openWebSocket();
    }

    private void openWebSocket() {
        Request.Builder b = new Request.Builder();
        b.url(url);
        if (apiKey != null) {
            b.header(X_API_KEY, apiKey);
        }
        if (bearerToken != null) {
            b.header(AUTHORIZATION, BEARER + bearerToken);
        }
        webSocket = client.newWebSocket(
                b.build(),
                AriesWebSocketListener.builder().handler(handler).build());
    }

    public void closeWebsocket() {
        if (webSocket != null) {
            webSocket.close(1001, "Going Down");
        }
    }
}
