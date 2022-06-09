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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * webhook event: revocation-notification-v2
 */
@Data @NoArgsConstructor
public class RevocationNotificationEventV2 {

    private RevocationFormat revocationFormat;
    private String credentialId;
    private String comment;

    public enum RevocationFormat {
        @JsonProperty("indy-anoncreds")
        @SerializedName("indy-anoncreds")
        INDY_ANONCREDS
    }
}
