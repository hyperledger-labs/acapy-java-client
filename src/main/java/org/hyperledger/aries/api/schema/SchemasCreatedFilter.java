/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.schema;

import org.hyperledger.aries.api.AcaPyRequestFilter;

import lombok.Builder;
import lombok.Data;

/**
 * A filter which can be used to let aca-py look for special criteria of schemas.
 */
@Data
@Builder
public class SchemasCreatedFilter implements AcaPyRequestFilter {
    private String schemaId;
    private String schemaIssuerDid;
    private String schemaName;
    private String schemaVersion;
}