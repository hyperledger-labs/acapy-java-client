/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;

import org.hyperledger.aries.util.ledger.LedgerDIDCreate;
import org.hyperledger.aries.util.ledger.LedgerDIDResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ACA-PY client
 */
@Slf4j
@SuppressWarnings("unused")
public class LedgerClient extends BaseClient {

    public static final String LEDGER_GENESIS_TXN_URL = "/genesis";
    public static final String LEDGER_DID_REGISTRATION_URL = "/register";
    public static final String LEDGER_DID_ENDORSER_ROLE = "ENDORSER";

    private final String url;

    /**
     * @param url The ledger URL without a path e.g. protocol://host:[port]
     * @param client {@link OkHttpClient} if null a default client is created
     */
    @Builder
    public LedgerClient(@NonNull String url,
                        @Nullable OkHttpClient client) {
        super(client);
        this.url = StringUtils.trim(url);
    }

    // ----------------------------------------------------
    // Register a new public DID on the ledger
    // ----------------------------------------------------

    /**
     * Create a public DID
     * @param didCreate {@link DIDCreate}
     * @return {@link DID}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<LedgerDIDResponse> ledgerDidCreate(@NonNull LedgerDIDCreate didCreate) throws IOException {
        Request req = buildPost(url + LEDGER_DID_REGISTRATION_URL, didCreate);
        return call(req, LedgerDIDResponse.class);
    }


    // ----------------------------------------------------
    // Internal
    // ----------------------------------------------------

    private Request buildPost(String u, Object body) {
        return request(u)
                .post(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildPut(String u, Object body) {
        return request(u)
                .put(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildPatch(String u, Object body) {
        return request(u)
                .patch(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildGet(String u) {
        return request(u)
                .get()
                .build();
    }

    private Request buildDelete(String u) {
        return request(u)
                .delete()
                .build();
    }

    private Request.Builder request(String u) {
        Request.Builder b = new Request.Builder()
                .url(u);
        return b;
    }
}
