/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.webhook.EventParser;

import java.util.List;
import java.util.Optional;

/**
 * V20PresExRecordByFormat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresExRecordByFormat {
    private JsonObject pres;
    private JsonObject presProposal;
    private JsonObject presRequest;

    public Optional<PresentProofRequest.ProofRequest> resolveIndyPresentationRequest() {
        JsonElement indy = getIndy(presRequest);
        if (indy != null) {
            return Optional.ofNullable(GsonConfig.defaultConfig().fromJson(indy, PresentProofRequest.ProofRequest.class));
        }
        return Optional.empty();
    }

    public JsonObject resolveIndyPresentation() {
        JsonElement indy = getIndy(pres);
        return indy != null ? indy.getAsJsonObject() : null;
    }

    public List<PresentationExchangeRecord.Identifier> resolveIndyIdentifiers() {
        JsonElement indy = getIndy(pres);
        return EventParser.resolveIdentifiers(indy != null ? indy.getAsJsonObject() : null);
    }

    private JsonElement getIndy(JsonObject o) {
        if (o != null) {
            return o.get("indy");
        }
        return null;
    }
}
