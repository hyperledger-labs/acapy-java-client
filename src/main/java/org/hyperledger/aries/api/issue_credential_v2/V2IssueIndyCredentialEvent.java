/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Webhook/Websocket Event:
 * issue_credential_v2_0_indy event
 * contains credential revocation information for issued v2 credentials
 */
@Data @NoArgsConstructor
public class V2IssueIndyCredentialEvent {
    private String revRegId;
    private String credExIndyId;
    private String credExId;
    private String updatedAt;
    private String createdAt;
    private String credRevId;
    private String credIdStored; // only set when holder
    private Object credRequestMetadata; // only set when holder
}
