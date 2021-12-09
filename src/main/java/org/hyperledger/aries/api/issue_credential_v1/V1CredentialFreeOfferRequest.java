/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.credentials.CredentialPreview;

/**
 * V1CredentialConnFreeOfferRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V1CredentialFreeOfferRequest {
    private Boolean autoIssue;
    private Boolean autoRemove;
    private String comment;
    private String credDefId;
    private CredentialPreview credentialPreview;
    private Boolean trace;
}
