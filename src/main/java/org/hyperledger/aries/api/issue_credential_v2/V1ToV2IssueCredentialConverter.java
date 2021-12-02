/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import lombok.NonNull;
import org.hyperledger.acy_py.generated.model.*;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialProposalRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * V1 to V2 issue credential model converters
 */
public class V1ToV2IssueCredentialConverter {

    /**
     * Converts V1 credential offer request into its V2 counterpart.
     * @param v1 {@link V10CredentialBoundOfferRequest}
     * @return {@link V20CredBoundOfferRequest}
     */
    public static V20CredBoundOfferRequest toV20CredBoundOfferRequest(@NonNull V10CredentialBoundOfferRequest v1) {
        return V20CredBoundOfferRequest
                .builder()
                .counterPreview(V20CredPreview
                        .builder()
                        .attributes(v1.getCounterProposal() != null
                                && v1.getCounterProposal().getCredentialProposal() != null
                                ? toV20CredAttrSpec(v1.getCounterProposal().getCredentialProposal().getAttributes())
                                : null)
                        .build())
                .filter(V20CredFilter
                        .builder()
                        .indy(V20CredFilterIndy
                                .builder()
                                .credDefId(v1.getCounterProposal() != null ? v1.getCounterProposal().getCredDefId() : null)
                                .issuerDid(v1.getCounterProposal() != null ? v1.getCounterProposal().getIssuerDid() : null)
                                .schemaId(v1.getCounterProposal() != null ? v1.getCounterProposal().getSchemaId() : null)
                                .schemaIssuerDid(v1.getCounterProposal() != null ? v1.getCounterProposal().getSchemaIssuerDid() : null)
                                .schemaVersion(v1.getCounterProposal() != null ? v1.getCounterProposal().getSchemaVersion() : null)
                                .schemaName(v1.getCounterProposal() != null ? v1.getCounterProposal().getSchemaName() : null)
                                .build())
                        .build())
                .build();
    }

    /**
     * Converts V1 credential proposal request into its V2 counterpart.
     * @param v1 {@link V1CredentialProposalRequest}
     * @return {@link V2CredentialSendRequest}
     */
    public static V2CredentialSendRequest toV2CredentialSendRequest(@NonNull V1CredentialProposalRequest v1) {
        return V2CredentialSendRequest
                .builder()
                .connectionId(v1.getConnectionId())
                .comment(v1.getComment())
                .trace(v1.getTrace())
                .autoRemove(v1.getAutoRemove())
                .credentialPreview(V2CredentialSendRequest.V2CredentialPreview
                        .builder()
                        .attributes(v1.getCredentialProposal() != null ? v1.getCredentialProposal().getAttributes() : null)
                        .build())
                .filter(V2CredentialSendRequest.V20CredFilter
                        .builder()
                        .indy(V20CredFilterIndy
                                .builder()
                                .credDefId(v1.getCredentialDefinitionId())
                                .issuerDid(v1.getIssuerDid())
                                .schemaId(v1.getSchemaId())
                                .schemaIssuerDid(v1.getSchemaIssuerDid())
                                .schemaVersion(v1.getSchemaVersion())
                                .schemaName(v1.getSchemaName())
                                .build())
                        .build())
                .build();
    }

    public static List<V20CredAttrSpec> toV20CredAttrSpec(@NonNull List<CredAttrSpec> attributes) {
        return attributes
                .stream()
                .map(a -> V20CredAttrSpec
                        .builder()
                        .mimeType(a.getMimeType())
                        .name(a.getName())
                        .value(a.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<V20CredAttrSpec> toV20CredAttrSpecFromAttributes(@NonNull List<CredentialAttributes> attributes) {
        return attributes
                .stream()
                .map(a -> V20CredAttrSpec
                        .builder()
                        .mimeType(a.getMimeType())
                        .name(a.getName())
                        .value(a.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public static V20CredExFree toV20CredExFree(@NonNull V1CredentialProposalRequest v1Proposal) {
        return V20CredExFree
                .builder()
                .connectionId(UUID.fromString(v1Proposal.getConnectionId()))
                .comment(v1Proposal.getComment())
                .autoRemove(v1Proposal.getAutoRemove())
                .trace(v1Proposal.getTrace())
                .filter(V20CredFilter
                        .builder()
                        .indy(V20CredFilterIndy
                                .builder()
                                .schemaName(v1Proposal.getSchemaName())
                                .credDefId(v1Proposal.getCredentialDefinitionId())
                                .schemaVersion(v1Proposal.getSchemaVersion())
                                .issuerDid(v1Proposal.getIssuerDid())
                                .schemaId(v1Proposal.getSchemaId())
                                .schemaIssuerDid(v1Proposal.getSchemaIssuerDid())
                                .build())
                        .build())
                .credentialPreview(V20CredPreview
                        .builder()
                        .attributes(v1Proposal.getCredentialProposal() != null ?
                                toV20CredAttrSpecFromAttributes(v1Proposal.getCredentialProposal().getAttributes())
                                : null)
                        .build())
                .build();
    }
}
