/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.config.GsonConfig;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts a V2 credential exchange record into a {@link Credential} representation.
 */
public class V2ToV1IndyCredentialConverter {

    private final Gson gson = GsonConfig.defaultConfig();

    public static V2ToV1IndyCredentialConverter INSTANCE() {
        return new V2ToV1IndyCredentialConverter();
    }

    public V1CredentialExchange toV1Offer(@NonNull V20CredExRecord v2Record) {
        IdWrapper ids = getIdsFromOffer(v2Record);
        return V1CredentialExchange
                .builder()
                .state(v2Record.getState())
                .credentialExchangeId(v2Record.getCredExId())
                .initiator(v2Record.getInitiator())
                .role(v2Record.getRole())
                .updatedAt(v2Record.getUpdatedAt())
                .autoRemove(v2Record.getAutoRemove())
                .autoOffer(v2Record.getAutoOffer())
                .autoIssue(v2Record.getAutoIssue())
                .trace(v2Record.getTrace())
                .threadId(v2Record.getThreadId())
                .createdAt(v2Record.getCreatedAt())
                .connectionId(v2Record.getConnectionId())
                .credentialDefinitionId(ids.getCredentialDefinitionId())
                .schemaId(ids.getSchemaId())
                .credentialProposalDict(V1CredentialExchange.CredentialProposalDict
                        .builder()
                        .schemaId(ids.getSchemaId())
                        .credDefId(ids.getCredentialDefinitionId())
                        .credentialProposal(V1CredentialExchange.CredentialProposalDict.CredentialProposal
                                .builder()
                                .attributes(v2Record.getCredOffer() != null
                                        && v2Record.getCredOffer().getCredentialPreview() != null
                                        ? v2Record.getCredOffer().getCredentialPreview().getAttributes()
                                        : null)
                                .build())
                        .build())
                .build();
    }

    /**
     * Converts v2 record into a {@link Credential}
     * Only works when the exchange state is 'done'
     * @param v2Record {@link V20CredExRecord}
     * @return optional {@link Credential}
     */
    public Optional<Credential> toV1Credential(@NonNull V20CredExRecord v2Record) {
        Optional<JsonObject> credential = getIndyCredential(v2Record);
        if (credential.isPresent()) {
            JsonElement typeIndy = credential.get().get("indy");
            if (typeIndy != null) {
                JsonElement values = typeIndy.getAsJsonObject().get("values");
                if (values != null) {
                    Set<Map.Entry<String, JsonElement>> entries = values.getAsJsonObject().entrySet();
                    Map<String, String> raw = entries
                            .stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    v -> getRawValue(v.getValue())));
                    Credential v1Credential = gson.fromJson(typeIndy, Credential.class);
                    v1Credential.setAttrs(raw);
                    return Optional.of(v1Credential);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<JsonObject> getIndyCredential(@NonNull V20CredExRecord indy) {
        if (indy.getByFormat() != null && indy.getByFormat().getCredIssue() != null) {
            return Optional.of(indy.getByFormat().getCredIssue());
        }
        else return Optional.empty();
    }

    private IdWrapper getIdsFromOffer(@NonNull V20CredExRecord indy) {
        IdWrapper.IdWrapperBuilder b = IdWrapper.builder();
        if (indy.getByFormat() != null && indy.getByFormat().getCredOffer() != null) {
            JsonObject offer = indy.getByFormat().getCredOffer();
            if (offer != null) {
                JsonObject typeIndy = offer.getAsJsonObject("indy");
                if (typeIndy != null) {
                    b.schemaId(typeIndy.get("schema_id").getAsString());
                    b.credentialDefinitionId(typeIndy.get("cred_def_id").getAsString());
                }
            }
        }
        return b.build();
    }

    private String getRawValue(@NonNull JsonElement el) {
        JsonElement raw = el.getAsJsonObject().get("raw");
        if (raw != null) {
            return raw.getAsString();
        }
        return "";
    }

    @Data @Builder
    private static final class IdWrapper {
        private String schemaId;
        private String credentialDefinitionId;
    }
}
