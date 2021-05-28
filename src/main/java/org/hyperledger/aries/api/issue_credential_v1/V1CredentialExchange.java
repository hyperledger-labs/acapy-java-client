/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of a credential exchange. E.g. issueCredentialSend() or issueCredentialSendProposal()
 *
 */
@Data @NoArgsConstructor
public class V1CredentialExchange {
    private Boolean autoIssue;
    private Boolean autoOffer;
    private Boolean autoRemove;
    private String connectionId;
    private String createdAt;
    private Credential credential;
    @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
    private String credentialDefinitionId;
    private String credentialExchangeId;
    private String credentialId; // only set when the state is credential_acked
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialOffer;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialOfferDict;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialProposalDict;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequest;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequestMetadata;
    private String errorMsg;
    private String initiator;
    private String parentThreadId;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject rawCredential;
    private String revocRegId;
    private String revocationId;
    private CredentialExchangeRole role;
    private String schemaId;
    private CredentialExchangeState state;
    private String threadId;
    private Boolean trace;
    private String updatedAt;
}
