/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.hyperledger.aries.api.multitenancy.WalletRecord;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ClientToTenant extends WalletRecord implements AutoCloseable {
    private final AriesClient restClient;
    private final AriesWebSocketClient webSocketClient;

    public ClientToTenant (@NonNull AriesClient restClient,
        @NonNull AriesWebSocketClient webSocketClient, @NonNull WalletRecord walletRecord) {
        this.restClient = restClient;
        this.webSocketClient = webSocketClient;
        this.setCreatedAt(walletRecord.getCreatedAt());
        this.setKeyManagementMode(walletRecord.getKeyManagementMode());
        this.setSettings(walletRecord.getSettings());
        this.setState(walletRecord.getState());
        this.setToken(walletRecord.getToken());
        this.setUpdatedAt(walletRecord.getUpdatedAt());
        this.setWalletId(walletRecord.getWalletId());
    }

    public AriesClient send() {
        return restClient;
    }

    public AriesWebSocketClient receive() {
        return webSocketClient;
    }

    @Override
    public void close() {
        this.webSocketClient.close();
    }
}
