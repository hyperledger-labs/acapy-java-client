/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class RevocationCredentialRecordFilter implements AcaPyRequestFilter {
    /** Credential exchange identifier */
    private String credExId;
    /** Credential revocation identifier */
    private String credRevId;
    /** Revocation registry identifier */
    private String revRegId;
}
