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
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.webhook.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ACA-PY Websocket Client: Receives events from aca-py
 */
@Slf4j
public class AriesWebSocketClient extends ReactiveEventHandler implements AutoCloseable, IFailureHandler {

    private final OkHttpClient client;
    private final String url;
    private final String apiKey;
    private final String bearerToken;
    private final List<IEventHandler> handler;
    private final List<String> walletIdFilter;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private WebSocket webSocket;

    /**
     * Create a new websocket client, supports builder methods like: {@code AriesWebSocketClient.builder().build()}
     * @param url Optional: The aca-py ws URL e.g. ws(s)://host:[port]/ws, defaults to localhost
     * @param apiKey Optional: The admin api key if security is enabled
     * @param bearerToken Optional: The Bearer token used in the Authorization header when running in multi tenant mode
     * @param client Optional: {@link OkHttpClient} if null or not set a default client is created
     * @param handler Optional: None, one or many custom event handler implementations, defaults to {@link EventHandler.DefaultEventHandler}
     * @param walletIdFilter Optional: Filter events by provided walletId
     * @param reactiveBufferSize How many events the reactive event handler should buffer, defaults to {@link ReactiveEventHandler#DEFAULT_BUFFER_SIZE}
     */
    @Builder
    public AriesWebSocketClient(@Nullable String url,
                                @Nullable String apiKey,
                                @Nullable String bearerToken,
                                @Nullable OkHttpClient client,
                                @Singular("handler") List<IEventHandler> handler,
                                @Nullable @Singular("walletId") List<String> walletIdFilter,
                                @Nullable Integer reactiveBufferSize) {
        super(reactiveBufferSize != null ? reactiveBufferSize : ReactiveEventHandler.DEFAULT_BUFFER_SIZE);
        this.client = Objects.requireNonNullElseGet(client, OkHttpClient::new);
        this.url = StringUtils.isEmpty(url) ? "ws://localhost:8031/ws" : StringUtils.trim(url);
        this.apiKey = StringUtils.trimToEmpty(apiKey);
        this.bearerToken = StringUtils.trimToEmpty(bearerToken);
        this.walletIdFilter = walletIdFilter != null ? Collections.unmodifiableList(walletIdFilter) : null;
        this.handler = mergeHandler(handler);
        openWebSocket();
    }

    private void openWebSocket() {
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (apiKey != null) {
            request.header(BaseClient.X_API_KEY, apiKey);
        }
        if (bearerToken != null) {
            request.header(BaseClient.AUTHORIZATION, BaseClient.BEARER + bearerToken);
        }
        webSocket = client.newWebSocket(
                request.build(),
                AriesWebSocketListener.builder()
                        .walletIdFilter(walletIdFilter)
                        .handler(handler)
                        .failureHandler(self())
                        .build());
    }

    @Override
    public void close() {
        if (webSocket != null) {
            webSocket.close(1001, null);
        }
        webSocket = null;
    }

    @Override
    public void onFailure() {
        close();
        executor.schedule(() -> {
            log.debug("Websocket disconnected - reconnecting");
            try {
                openWebSocket();
            } catch (Exception e) {
                onFailure();
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executor.shutdownNow();
        close();
    }

    @NotNull
    private List<IEventHandler> mergeHandler(List<IEventHandler> handler) {
        List<IEventHandler> tempHandler = new ArrayList<>();
        tempHandler.add(this);
        tempHandler.addAll(handler.isEmpty() ? List.of(new EventHandler.DefaultEventHandler()) : handler);
        return Collections.unmodifiableList(tempHandler);
    }

    private AriesWebSocketClient self() {
        return this;
    }
}
