/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.jsonld.ErrorResponse;
import org.hyperledger.aries.api.multitenancy.WalletRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof.PresentationRequestCredentials;
import org.hyperledger.aries.config.GsonConfig;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
abstract class BaseClient {

    static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    static final Type CONNECTION_TYPE = new TypeToken<Collection<ConnectionRecord>>(){}.getType();
    static final Type CREDENTIAL_TYPE = new TypeToken<Collection<Credential>>(){}.getType();
    static final Type ISSUE_CREDENTIAL_TYPE = new TypeToken<Collection<V1CredentialExchange>>(){}.getType();
    static final Type PRESENTATION_REQUEST_CREDENTIALS = new TypeToken<Collection<PresentationRequestCredentials>>(){}.getType();
    static final Type PROOF_TYPE = new TypeToken<Collection<PresentationExchangeRecord>>(){}.getType();
    static final Type WALLET_DID_TYPE = new TypeToken<Collection<DID>>(){}.getType();
    static final Type WALLET_RECORD_TYPE = new TypeToken<Collection<WalletRecord>>(){}.getType();
    static final Type MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();

    static final String X_API_KEY = "X-API-Key";
    static final String AUTHORIZATION = "Authorization";
    static final String BEARER = "Bearer ";

    static final String EMPTY_JSON = "{}";

    final Gson gson = GsonConfig.defaultConfig();

    final OkHttpClient client;

    BaseClient(@Nullable OkHttpClient client) {
        this.client = Objects.requireNonNullElseGet(client, () -> new OkHttpClient.Builder()
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .build());
    }

    static RequestBody jsonBody(String json) {
        return RequestBody.create(json, JSON_TYPE);
    }

    <T> Optional<T> call(Request req, Class<T> t) throws IOException {
        return call(req, (Type) t);
    }

    <T> Optional<T> call(Request req, Type t) throws IOException {
        Optional<T> result = Optional.empty();
        try (Response resp = client.newCall(req).execute()) {
            if (resp.isSuccessful() && resp.body() != null) {
                result = Optional.of(gson.fromJson(resp.body().string(), t));
            } else if (!resp.isSuccessful()) {
                handleError(resp);
            }
        }
        return result;
    }

    Optional<String> raw(Request req) throws IOException {
        Optional<String> result = Optional.empty();
        try (Response resp = client.newCall(req).execute()) {
            if (resp.isSuccessful() && resp.body() != null) {
                result = Optional.of(resp.body().string());
            } else if (!resp.isSuccessful()) {
                handleError(resp);
            }
        }
        return result;
    }

    void call(Request req) throws IOException {
        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                handleError(resp);
            }
        }
    }

    public <T> Optional<T> getWrapped(Optional<String> json, String field, Type t) {
        if (json.isPresent()) {
            JsonElement je = JsonParser
                    .parseString(json.get())
                    .getAsJsonObject()
                    .get(field);
            return Optional.ofNullable(gson.fromJson(je, t));
        }
        return Optional.empty();
    }

    public void checkForError(Optional<String> json) {
        if (json.isPresent()) {
            ErrorResponse error = gson.fromJson(json.get(), ErrorResponse.class);
            if (error != null && error.getError() != null) {
                throw new AriesException(0, error.getError());
            }
        }
    }

    private static void handleError(Response resp) throws IOException {
        String msg = StringUtils.isNotEmpty(resp.message()) ? resp.message() : "";
        String body = resp.body() != null ? resp.body().string() : "";
        log.error("code={} message={}\nbody={}", resp.code(), msg, body);
        throw new AriesException(resp.code(), msg + "\n" + body);
    }
}
