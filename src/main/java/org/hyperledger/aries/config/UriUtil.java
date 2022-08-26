/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.config;

import lombok.NonNull;

import java.net.URI;

public class UriUtil {

    public static String httpToWs(@NonNull String url) {
        URI uri = URI.create(url);
        String scheme = "https".equals(uri.getScheme()) ? "wss" : "ws";
        String host = uri.getHost();
        int port = uri.getPort();
        String ifPort = port > 0 ? ":" + port : "";
        return scheme + "://" + host + ifPort + "/ws";
    }
}
