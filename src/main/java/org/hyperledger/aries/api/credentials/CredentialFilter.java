/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import lombok.NonNull;

import java.util.function.Predicate;

public class CredentialFilter {

    public static Predicate<Credential> credentialDefinitionId(
            @NonNull String credentialDefinitionId) {
        return c -> credentialDefinitionId.equals(c.getCredentialDefinitionId());
    }

    public static Predicate<Credential> schemaId(
            @NonNull String schemaId) {
        return c -> schemaId.equals(c.getSchemaId());
    }
}
