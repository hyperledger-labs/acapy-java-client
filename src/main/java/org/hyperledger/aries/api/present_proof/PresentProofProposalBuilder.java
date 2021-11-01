/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.NonNull;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.present_proof_v2.V20PresProposalByFormat;
import org.hyperledger.aries.api.present_proof_v2.V20PresProposalRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper class to build a {@link PresentProofProposal}
 *
 */
public class PresentProofProposalBuilder {

    /**
     * Usecase: send a single wallet credential to a connection. Auto present is set to true, as the flow
     * is initiated with a specific credential anyway.
     * @param connectionId the connection id
     * @param cred {@link Credential}
     * @return simple {@link PresentProofProposal} without any predicates set
     */
    public static PresentProofProposal fromCredential(@NonNull String connectionId, @NonNull Credential cred) {
        final Map<String, String> attrs = cred.getAttrs();
        List<PresentProofProposal.PresentationPreview.PresAttrSpec> presAttr = new ArrayList<>();
        attrs.forEach( (k, v) -> presAttr.add(PresentProofProposal.PresentationPreview.PresAttrSpec
                .builder()
                .name(k)
                .value(v)
                .credentialDefinitionId(cred.getCredentialDefinitionId())
                .referent(cred.getReferent())
                .build()));
        return PresentProofProposal
                .builder()
                .autoPresent(Boolean.TRUE)
                .presentationProposal(PresentProofProposal.PresentationPreview.builder().attributes(presAttr).build())
                .connectionId(connectionId)
                .build();
    }

    public static V20PresProposalRequest v2IndyFromCredential(@NonNull String connectionId, @NonNull Credential cred) {
        PresentProofProposal v1 = fromCredential(connectionId, cred);
        return V20PresProposalRequest
                .builder()
                .autoPresent(Boolean.TRUE)
                .connectionId(connectionId)
                .presentationProposal(V20PresProposalByFormat
                        .builder()
                        .indy(v1.getPresentationProposal())
                        .build())
                .build();
    }
}
