/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;
import org.hyperledger.aries.api.jsonld.VerifiablePresentation;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.webhook.EventParser;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * V20PresExRecordByFormat
 *
 * aca-py specific way of presenting the types as either dif or indy.
 * The second way would be to go through the attachments and get the corresponding format by the attachment id.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresExRecordByFormat {
    private JsonObject pres;
    private JsonObject presProposal;
    private JsonObject presRequest;

    public boolean isIndy() {
        return getByFormat(Format.INDY, presRequest) != null;
    }

    public boolean isDif() {
        return getByFormat(Format.DIF, presRequest) != null;
    }

    public Optional<PresentProofRequest.ProofRequest> resolveIndyPresentationRequest() {
        JsonElement indy = getByFormat(Format.INDY, presRequest);
        return parseValue(indy, PresentProofRequest.ProofRequest.class);
    }

    public <T> Optional<T> resolveDifPresentationRequest(Type type) {
        JsonElement dif = getByFormat(Format.DIF, presRequest);
        return parseValue(dif, type);
    }

    private <T> Optional<T> parseValue(JsonElement json, @NonNull Class<T> valueType) {
        if (json == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(GsonConfig.defaultConfig().fromJson(json, valueType));
    }

    private <T> Optional<T> parseValue(JsonElement json, @NonNull Type valueType) {
        if (json == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(GsonConfig.defaultConfig().fromJson(json, valueType));
    }

    public JsonObject resolveIndyPresentation() {
        JsonElement indy = getByFormat(Format.INDY, pres);
        return indy != null ? indy.getAsJsonObject() : null;
    }

    public VerifiablePresentation<VerifiableCredential> resolveDifPresentation() {
        JsonElement dif = getByFormat(Format.DIF, pres);
        return GsonConfig.defaultConfig().fromJson(dif, VerifiablePresentation.VERIFIABLE_CREDENTIAL_TYPE);
    }

    public List<PresentationExchangeRecord.Identifier> resolveIndyIdentifiers() {
        JsonElement indy = getByFormat(Format.INDY, pres);
        return EventParser.resolveIdentifiers(indy != null ? indy.getAsJsonObject() : null);
    }

    private JsonElement getByFormat(Format f, JsonObject o) {
        if (o != null) {
            return o.get(f.getValue());
        }
        return null;
    }

    @AllArgsConstructor
    private enum Format {
        INDY("indy"),
        DIF("dif");

        @Getter
        private final String value;
    }
}
