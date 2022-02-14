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

/**
 * V20CredBoundOfferRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20CredBoundOfferRequest {
    private V2CredentialExchangeFree.V2CredentialPreview counterPreview;
    private V2CredentialExchangeFree.V20CredFilter filter;
}
