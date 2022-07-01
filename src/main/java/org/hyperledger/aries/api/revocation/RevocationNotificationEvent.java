/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import lombok.*;

/**
 * Webhook event: revocation-notification
 * aries-rfc: 0183-revocation-notification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RevocationNotificationEvent extends RevocationNotificationBase {
    private String threadId;
    private String comment;

    /**
     * Extracts revocation information form the threadId
     * @return {@link RevocationInfo}
     */
    public RevocationInfo toRevocationInfo() {
        return expressionToRevocationInfo(threadId, 3);
    }
}
