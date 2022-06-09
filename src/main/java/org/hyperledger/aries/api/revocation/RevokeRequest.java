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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RevokeRequest {

    private String comment;

    /**
     * Needed when --notify-revocation flag is set, or notify is true
     * @since 0.7.3
     */
    private String connectionId;

    /** Either credential exchange identifier */
    private String credExId;

    /** Or credential revocation identifier */
    private String credRevId;

    /**
     * Send a notification to the credential recipient
     * @since 0.7.3
     */
    private Boolean notify;

    /**
     * Specify which version of the revocation notification should be sent
     * @since 0.7.4
     */
    @Builder.Default
    private NotifyVersion notifyVersion = NotifyVersion.V1_0;

    /** If false, needs a call to /revocation/publish-revocations later */
    private Boolean publish;

    /** and revocation registry identifier */
    private String revRegId;

    /**
     * Thread ID of the credential exchange message thread resulting in the credential now being revoked;
     * required if notify is true
     * @since 0.7.3
     */
    private String threadId;

    /**
     * Specifies the revocation notification event type that should be sent, either:
     * revocation-notification {@link RevocationNotificationEvent}
     * or revocation-notification-v2 {@link RevocationNotificationEventV2}
     */
    public enum NotifyVersion {
        @JsonProperty("v1_0")
        @SerializedName("v1_0")
        V1_0,

        @JsonProperty("v2_0")
        @SerializedName("v2_0")
        V2_0
    }
}
