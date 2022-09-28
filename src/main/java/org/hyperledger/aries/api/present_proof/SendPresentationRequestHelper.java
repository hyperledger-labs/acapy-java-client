/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.exception.AriesException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class that maps a flat list of attribute or predicate group names back into their respective groups:
 * selectedAttributes, selectedPredicates and selfAttestedAttributes in reference to the matching poof request.
 */
public class SendPresentationRequestHelper {

    public static SendPresentationRequest buildRequest(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<String, SelectedMatch.ReferentInfo> selectedCredentials) {

        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> requestedAttributes =
                buildRequestedAttributes(presentationExchange, selectedCredentials);
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> requestedPredicates =
                buildRequestedPredicates(presentationExchange, selectedCredentials);

        return SendPresentationRequest
                .builder()
                .requestedAttributes(requestedAttributes)
                .requestedPredicates(requestedPredicates)
                .selfAttestedAttributes(findSelfAttested(selectedCredentials))
                .build();
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> buildRequestedAttributes(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<String, SelectedMatch.ReferentInfo> matchingCredentials) {
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> result = new LinkedHashMap<>();
        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedAttributes() != null) {
            Set<String> attributeGroupNames = presentationRequest.getRequestedAttributes().keySet();
            result = matchingCredentials.entrySet().stream()
                    .filter(e -> attributeGroupNames.contains(e.getKey()))
                    .filter(e -> e.getValue().getReferent() != null)
                    .map(e -> Map.entry(e.getKey(), SendPresentationRequest.IndyRequestedCredsRequestedAttr
                            .builder()
                            .credId(e.getValue().getReferent())
                            .revealed(e.getValue().getRevealed() != null ? e.getValue().getRevealed() : Boolean.TRUE)
                            // .timestamp let aca-py do this
                            .build()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return result;
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> buildRequestedPredicates(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<String, SelectedMatch.ReferentInfo> matchingCredentials) {
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> result = new LinkedHashMap<>();

        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedPredicates() != null) {
            Set<String> predicateGroupNames = presentationRequest.getRequestedPredicates().keySet();
            result = matchingCredentials.entrySet().stream()
                    .filter(e -> predicateGroupNames.contains(e.getKey()))
                    .map(e -> Map.entry(e.getKey(), SendPresentationRequest.IndyRequestedCredsRequestedPred
                            .builder()
                            .credId(e.getValue().getReferent())
                            // .timestamp let aca-py do this
                            .build()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (predicateGroupNames.size() > result.size()) {
                throw new AriesException(0, "Provided predicate group size does not match the requested size");
            }
        }
        return result;
    }

    private static Map<String, String> findSelfAttested(
            @NonNull Map<String, SelectedMatch.ReferentInfo> selectedCredentials) {
        return selectedCredentials.entrySet().stream()
                .filter(e -> e.getValue().getReferent() == null)
                .filter(e -> StringUtils.isNotEmpty(e.getValue().getSelfAttestedValue()))
                .map(e -> Map.entry(e.getKey(), e.getValue().getSelfAttestedValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Data
    @NoArgsConstructor
    public static class SelectedMatch {

        // group name to referent information
        private Map<String, ReferentInfo> selectedReferents;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ReferentInfo {
            private String referent;
            private Boolean revealed;
            private String selfAttestedValue;
        }
    }
}
