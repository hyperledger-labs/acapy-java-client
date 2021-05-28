/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credential_definition;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

import javax.annotation.Nullable;

@Data @Builder
public class CredentialDefinitionFilter implements AcaPyRequestFilter {
    @Nullable private String credentialDefinitionId;
    @Nullable private String issuerDid;
    @Nullable private String schemaId;
    @Nullable private String schemaIssuerDid;
    @Nullable private String schemaName;
    @Nullable private String schemaVersion;
}
