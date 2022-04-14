/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.settings;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * "payload": {"authenticated": true, "label": "Aries Cloud Agent", "endpoint": "http://localhost:8030", "no_receive_invites": false, "help_link": null}
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class Settings {

    private boolean authenticated;
    @SerializedName("no_receive_invites")
    private boolean noInvites;
    private String label;
    private String endpoint;
    @SerializedName("help_link")
    private String helpLink;
}
