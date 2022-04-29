/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.BaseClient;
import org.hyperledger.aries.config.GsonConfig;

import java.util.List;

@Slf4j
public class AriesWebSocketListener extends okhttp3.WebSocketListener {

    private final Gson gson = GsonConfig.defaultConfig();

    private final String label;
    private final List<IEventHandler> handler;
    private final List<String> walletIdFilter;
    private final boolean withLabel;

    @Builder
    public AriesWebSocketListener(
            String label,
            @NonNull @Singular("handler") List<IEventHandler> handler,
            @Singular("walletId") List<String> walletIdFilter) {
        this.label = label;
        this.handler = handler;
        this.withLabel = StringUtils.isNotEmpty(label);
        this.walletIdFilter = walletIdFilter;
    }

    private String appendLabel(String msg) {
        return withLabel ? label + " " + msg : msg;
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        log.debug(appendLabel("Open: {}"), response);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String message) {
        log.trace(appendLabel("Event: {}"), message);
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String walletId = json.has("wallet_id") ? json.get("wallet_id").getAsString() : null;
            String topic = json.get("topic").getAsString();
            String payload = json.has("payload") ? json.get("payload").toString() : BaseClient.EMPTY_JSON;

            // drop ws ping messages, not to be confused with aca-py ping message
            // https://datatracker.ietf.org/doc/html/rfc6455#section-5.5.2
            if (notWsPing(topic, payload) && isForWalletId(walletId)) {
                handler.forEach(h -> h.handleEvent(walletId, topic, payload));
            }
        } catch (JsonSyntaxException ex) {
            log.error("JsonSyntaxException", ex);
        }
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable th, Response response) {
        String message = response != null ? response.message() : th.getMessage();
        if (!"Socket closed".equals(message))
            log.error(String.format("%s Failure: %s", label, message), th);
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        log.trace(appendLabel("Closing: {} {}"), code, reason);
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        log.debug(appendLabel("Closed: {} {}"), code, reason);
    }

    private boolean notWsPing(String topic, String payload) {
        return !(EventType.PING.topicEquals(topic) && BaseClient.EMPTY_JSON.equals(payload));
    }

    private boolean isForWalletId(String walletId) {
        return walletId == null
                || walletIdFilter == null
                || ArrayUtils.contains(walletIdFilter.toArray(), walletId);
    }
}