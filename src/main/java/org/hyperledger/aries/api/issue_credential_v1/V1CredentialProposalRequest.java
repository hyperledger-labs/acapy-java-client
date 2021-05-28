/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.config.CredDefId;

import javax.annotation.Nullable;

/**
 * Credential that is requested from a connection (holder to issuer),
 * or issued to a connection (issuer to holder)
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class V1CredentialProposalRequest {

    /**
     *  Whether to remove the credential exchange record on completion
     *  (overrides --preserve-exchange-records configuration setting)
     */
    @Nullable
    private Boolean autoRemove;

    @Nullable
    private String comment;

    @NonNull
    private String connectionId;

    @Nullable
    @SerializedName(value = CredDefId.CREDENTIAL_DEFINITION_ID, alternate = CredDefId.CRED_DEF_ID)
    private String credentialDefinitionId;

    /**
     * Optional when sending the issuer a proposal, mandatory when sending the holder a credential
     */
    @Nullable
    private CredentialPreview credentialProposal;

    @Nullable
    private String issuerDid;

    @Nullable
    private String schemaId;

    @Nullable
    private String schemaIssuerDid;

    @Nullable
    private String schemaName;

    @Nullable
    private String schemaVersion;

    @Nullable
    private Boolean trace;

    /**
     * Constructor used when issuing a credential via issueCredentialSend()
     * @param <T> type
     * @param connectionId the connection id
     * @param credentialDefinitionId the credential definition id
     * @param instance instance template
     */
    public <T> V1CredentialProposalRequest(
            @NonNull String connectionId, @NonNull String credentialDefinitionId, @NonNull T instance) {
        super();
        this.connectionId = connectionId;
        this.credentialDefinitionId = credentialDefinitionId;
        this.credentialProposal = new CredentialPreview(CredentialAttributes.from(instance));
    }
}
