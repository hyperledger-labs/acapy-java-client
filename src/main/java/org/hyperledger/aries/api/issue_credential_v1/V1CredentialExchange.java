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
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import java.util.List;

/**
 * Result of a credential exchange. E.g. issueCredentialSend() or issueCredentialSendProposal()
 *
 */
@SuppressWarnings("unused")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
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
    private CredentialProposalDict credentialProposalDict;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequest;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequestMetadata;
    private String errorMsg;
    private CredentialExchangeInitiator initiator;
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

    public boolean isIssuer() {
        return CredentialExchangeRole.ISSUER.equals(role);
    }

    public boolean isHolder() {
        return CredentialExchangeRole.HOLDER.equals(role);
    }

    public boolean isProposalReceived() {
        return CredentialExchangeState.PROPOSAL_RECEIVED.equals(state);
    }

    public boolean isAutoIssueEnabled() {
        return autoIssue != null && autoIssue;
    }

    public boolean isAutoOfferEnabled() {
        return autoOffer != null && autoOffer;
    }

    public boolean isAutoRemoveEnabled() {
        return autoRemove != null && autoRemove;
    }

    public boolean isCredentialAcked() {
        return CredentialExchangeState.CREDENTIAL_ACKED.equals(state);
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static final class CredentialProposalDict {
        @SerializedName("@type")
        private String type;

        @SerializedName("@id")
        private String id;

        private String schemaId;

        private String credDefId;

        private CredentialProposal credentialProposal;

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        public static final class CredentialProposal {
            @SerializedName("@type")
            private String type;

            private List<CredentialAttributes> attributes;
        }
    }
}
