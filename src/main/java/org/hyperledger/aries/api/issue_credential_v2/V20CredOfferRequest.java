/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.credentials.CredentialPreview;

import java.util.UUID;

/**
 * V20CredOfferRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20CredOfferRequest {
    private Boolean autoIssue;
    private Boolean autoRemove;
    private String comment;
    private UUID connectionId;
    private CredentialPreview credentialPreview;
    private V2CredentialExchangeFree.V20CredFilter filter;
    private Boolean trace;
}
