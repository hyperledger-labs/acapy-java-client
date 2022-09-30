/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.config.CredDefId;
import org.hyperledger.aries.pojo.PojoProcessor;

import java.util.Map;

@Slf4j
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Credential {

    private Map<String, String> attrs;

    @SerializedName(value = CredDefId.CREDENTIAL_DEFINITION_ID,
            alternate = {CredDefId.CRED_DEF_ID, CredDefId.CREDENTIALDEFINITIONID})
    private String credentialDefinitionId;

    private String credRevId;

    private String referent;

    private String revRegId;

    private String schemaId;

    /**
     * Maps the credential attributes into an instance of the provided class type.
     * @param <T> The class type
     * @param type The class type
     * @return Instantiated type with all matching properties set
     */
    public <T> T to(@NonNull Class<T> type) {
        return PojoProcessor.setValues(type, attrs);
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static final class CredentialRevokedResult {
        private Boolean revoked;
    }
}
