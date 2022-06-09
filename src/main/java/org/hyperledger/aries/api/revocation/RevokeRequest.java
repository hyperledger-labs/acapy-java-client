/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

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
    private org.hyperledger.acy_py.generated.model.RevokeRequest.NotifyVersionEnum notifyVersion;

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
}
