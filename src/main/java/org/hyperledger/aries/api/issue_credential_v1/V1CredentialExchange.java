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
import org.hyperledger.acy_py.generated.model.AttachDecorator;
import org.hyperledger.acy_py.generated.model.IndyCredAbstract;
import org.hyperledger.acy_py.generated.model.IndyCredRequest;
import org.hyperledger.acy_py.generated.model.IndyCredential;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private IndyCredAbstract credentialOffer;
    private CredentialOfferDict credentialOfferDict;
    private CredentialProposalDict credentialProposalDict;
    private IndyCredRequest credentialRequest;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequestMetadata;
    private String errorMsg;
    private CredentialExchangeInitiator initiator;
    private String parentThreadId;
    private IndyCredential rawCredential;
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

    public boolean isAutoIssueEnabled() {
        return autoIssue != null && autoIssue;
    }

    public boolean isAutoOfferEnabled() {
        return autoOffer != null && autoOffer;
    }

    public boolean isAutoRemoveEnabled() {
        return autoRemove != null && autoRemove;
    }

    public boolean stateIsProposalSent() {
        return CredentialExchangeState.PROPOSAL_SENT.equals(state);
    }

    public boolean stateIsProposalReceived() {
        return CredentialExchangeState.PROPOSAL_RECEIVED.equals(state);
    }

    public boolean stateIsOfferSent() {
        return CredentialExchangeState.OFFER_SENT.equals(state);
    }

    public boolean stateIsOfferReceived() {
        return CredentialExchangeState.OFFER_RECEIVED.equals(state);
    }

    public boolean stateIsRequestSent() {
        return CredentialExchangeState.REQUEST_SENT.equals(state);
    }

    public boolean stateIsRequestReceived() {
        return CredentialExchangeState.REQUEST_RECEIVED.equals(state);
    }

    public boolean stateIsCredentialReceived() {
        return CredentialExchangeState.CREDENTIAL_RECEIVED.equals(state);
    }

    public boolean stateIsCredentialAcked() {
        return CredentialExchangeState.CREDENTIAL_ACKED.equals(state);
    }

    public boolean stateIsDone() {
        return CredentialExchangeState.DONE.equals(state);
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

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static final class CredentialOfferDict {
        @SerializedName("@type")
        private String type;

        @SerializedName("@id")
        private String id;

        @SerializedName("~thread")
        private ThreadId threadId;

        private CredentialProposalDict.CredentialProposal credentialPreview;

        @SerializedName("offers~attach")
        private List<AttachDecorator> offersAttach = new ArrayList<>();
    }

    public Optional<Map<String, String>> findAttributesInCredentialOfferDict() {
        Optional<Map<String, String>> result = Optional.empty();
        if (credentialOfferDict != null && credentialOfferDict.credentialPreview != null) {
            List<CredentialAttributes> attributes = credentialOfferDict.getCredentialPreview().getAttributes();
            if (attributes != null) {
                return Optional.of(attributes.stream()
                        .collect(Collectors.toMap(CredentialAttributes::getName, CredentialAttributes::getValue)));
            }
        }
        return result;
    }

}
