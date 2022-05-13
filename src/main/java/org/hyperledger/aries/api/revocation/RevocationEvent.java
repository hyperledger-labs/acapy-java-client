/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Issuer credential revocation information.
 * Webhook event: issuer_cred_rev
 * IssuerCredRevRecord in aca-py
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class RevocationEvent {
    private RevocationEventState state;
    private String createdAt;
    private String revRegId;
    private String recordId;
    private String credDefId;
    private String credExId;
    private String uploadedAt;
    private String credRevId;

    public enum RevocationEventState {
        @JsonProperty("issued")
        @SerializedName("issued")
        ISSUED,

        @JsonProperty("revoked")
        @SerializedName("revoked")
        REVOKED
    }
}
