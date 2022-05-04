/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.V20Pres;
import org.hyperledger.acy_py.generated.model.V20PresProposal;
import org.hyperledger.acy_py.generated.model.V20PresRequest;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;
import org.hyperledger.aries.api.jsonld.VerifiablePresentation;
import org.hyperledger.aries.api.present_proof.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * V20PresExRecord
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresExRecord implements PresExStateTranslator {
    private Boolean autoPresent;
    private String createdAt;
    private String updatedAt;
    private Boolean trace;
    private String errorMsg;

    private Boolean verified;

    private String connectionId;
    private String presExId;
    private String threadId;

    private PresentationExchangeInitiator initiator;
    private PresentationExchangeRole role;
    private PresentationExchangeState state;

    private V20Pres pres;
    private V20PresExRecordByFormat byFormat;
    private V20PresProposal presProposal;
    private V20PresRequest presRequest;

    public boolean isIndy() {
        return byFormat != null && byFormat.isIndy();
    }

    public boolean isDif() {
        return byFormat != null && byFormat.isDif();
    }

    public Optional<PresentProofRequest.ProofRequest> resolveIndyPresentationRequest() {
        if (byFormat != null) {
            return byFormat.resolveIndyPresentationRequest();
        }
        return Optional.empty();
    }

    public <T> Optional<T> resolveDifPresentationRequest(Type type) {
        if (byFormat != null) {
            return byFormat.resolveDifPresentationRequest(type);
        }
        return Optional.empty();
    }

    public JsonObject resolveIndyPresentation() {
        if (byFormat != null) {
            return byFormat.resolveIndyPresentation();
        }
        return null;
    }

    public VerifiablePresentation<VerifiableCredential> resolveDifPresentation() {
        if (byFormat != null) {
            return byFormat.resolveDifPresentation();
        }
        return null;
    }

    public List<PresentationExchangeRecord.Identifier> resolveIndyIdentifiers() {
        if (byFormat != null) {
            return byFormat.resolveIndyIdentifiers();
        }
        return List.of();
    }
}
