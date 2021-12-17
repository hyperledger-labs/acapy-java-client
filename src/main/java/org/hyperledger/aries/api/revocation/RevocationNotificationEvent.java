/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Webhook event: revocation-notification
 */
@Data @NoArgsConstructor
public class RevocationNotificationEvent {
    private String threadId;
    private String comment;
}
