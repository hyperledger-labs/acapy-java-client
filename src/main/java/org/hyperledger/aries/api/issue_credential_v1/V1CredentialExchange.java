/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
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
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hyperledger.acy_py.generated.model.AttachDecorator;
import org.hyperledger.acy_py.generated.model.IndyCredAbstract;
import org.hyperledger.acy_py.generated.model.IndyCredRequest;
import org.hyperledger.acy_py.generated.model.IndyCredential;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Result of a credential exchange. E.g. issueCredentialSend() or issueCredentialSendProposal()
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class V1CredentialExchange extends BaseCredExRecord {

    private Credential credential;
    private IndyCredAbstract credentialOffer;
    private CredentialOfferDict credentialOfferDict;
    private CredentialProposalDict credentialProposalDict;
    private IndyCredRequest credentialRequest;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialRequestMetadata;
    private IndyCredential rawCredential;

    private String schemaId;
    @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
    private String credentialDefinitionId;
    private String credentialId; // only set when the state is credential_acked
    private String revocRegId;
    private String revocationId;

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

            private static final Comparator<CredentialAttributes> attributesComparator = Comparator
                    .comparing(CredentialAttributes::getName);

            @SerializedName("@type")
            private String type;

            private List<CredentialAttributes> attributes;

            public List<CredentialAttributes> getAttributes() {
                attributes.sort(attributesComparator);
                return attributes;
            }
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
        Map<String, String> attributesMap = findAttributesInCredentialOfferDictList().orElse(List.of())
                .stream()
                .collect(Collectors.toMap(CredentialAttributes::getName, CredentialAttributes::getValue));

        return Optional.of(attributesMap);
    }

    public Optional<List<CredentialAttributes>> findAttributesInCredentialOfferDictList() {
        Optional<List<CredentialAttributes>> result = Optional.empty();
        if (credentialOfferDict != null && credentialOfferDict.credentialPreview != null) {
            List<CredentialAttributes> attributes = credentialOfferDict.getCredentialPreview().getAttributes();
            if (attributes != null) {
                return Optional.of(attributes);
            }
        }
        return result;
    }
}
