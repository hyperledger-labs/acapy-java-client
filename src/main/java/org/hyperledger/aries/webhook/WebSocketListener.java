/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import org.hyperledger.aries.config.GsonConfig;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;

@Slf4j
public class WebSocketListener extends okhttp3.WebSocketListener {

    private Gson gson = GsonConfig.defaultConfig();
    
    private final String label;
    private final EventHandler handler;
    
    public WebSocketListener(String label, EventHandler handler) {
        this.label = label;
        this.handler = handler;
    }
    
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.debug("{} Open: {}", label, response);
    }
    
    @Override
    public void onMessage(WebSocket webSocket, String message) {
        log.debug("{} Event: {}", label, message);
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String topic = json.get("topic").getAsString();
            String payload = json.has("payload") ? json.get("payload").toString() : null;
            handler.handleEvent(topic, payload);
        } catch (JsonSyntaxException ex) {
            log.error("JsonSyntaxException", ex);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable th, Response response) {
        String message = response != null ? response.message() : th.getMessage();
        if (!"Socket closed".equals(message))
            log.error(String.format("%s Failure: %s", label, message), th);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        log.trace("{} Closing: {} {}", label, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        log.debug("{} Closed: {} {}", label, code, reason);
    }
}
