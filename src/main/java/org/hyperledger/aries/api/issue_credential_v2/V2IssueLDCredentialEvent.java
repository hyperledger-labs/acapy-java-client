/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
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
 * issue_credential_v2_0_ld_proof
 * contains wallet id's
 */
@Data
@NoArgsConstructor
public class V2IssueLDCredentialEvent {
    private String credExId;
    private String updatedAt;
    private String createdAt;
    private String credExLdProofId;
    /** w3c credential record_id */
    private String credIdStored; // only set when holder
}
