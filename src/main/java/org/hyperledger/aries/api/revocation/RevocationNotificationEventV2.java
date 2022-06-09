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
import lombok.*;

/**
 * webhook event: revocation-notification-v2
 * aries-rfc: 0721-revocation-notification-v2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RevocationNotificationEventV2 extends RevocationNotificationBase {

    private RevocationFormat revocationFormat;
    private String credentialId;
    private String comment;

    /**
     * Extracts revocation information form the credentialId
     * @return {@link RevocationInfo}
     */
    public RevocationInfo toRevocationInfo() {
        return expressionToRevocationInfo(credentialId, 2);
    }

    public enum RevocationFormat {
        @JsonProperty("indy-anoncreds")
        @SerializedName("indy-anoncreds")
        INDY_ANONCREDS
    }
}
