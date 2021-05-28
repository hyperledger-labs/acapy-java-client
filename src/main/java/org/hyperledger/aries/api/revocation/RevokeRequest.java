/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
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
    /** Either credential exchange identifier */
    private String credExId;

    /** Or credential revocation identifier */
    private String credRevId;
    /** and revocation registry identifier */
    private String revRegId;

    /** If false, needs a call to /revocation/publish-revocations later */
    private Boolean publish;
}
