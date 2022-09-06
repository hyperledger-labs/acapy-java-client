/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used in scenarios where 'auto-respond-presentation-request' is set to false.
 * The helper takes the result of the following two api calls:
 * 1. /present-proof/records/{pres_ex_id}
 * 2. /present-proof/records/{pres_ex_id}/credentials
 * To generate the model for /present-proof/records/{pres_ex_id}/send-presentation
 *
 * @see <a href="https://github.com/hyperledger/aries-rfcs/blob/master/concepts/0441-present-proof-best-practices/README.md">
 *     0441-present-proof-best-practices</a>
 */
public class SendPresentationRequestHelper {

    public static SendPresentationRequest acceptSelected(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<PresentationRequestCredentials, Boolean> selectedCredentials) {
        return buildRequest(presentationExchange, selectedCredentials, null);
    }

    public static SendPresentationRequest acceptSelected(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<PresentationRequestCredentials, Boolean> selectedCredentials,
            @Nullable Map<String, String> selfAttestedAttributes) {
        return buildRequest(presentationExchange, selectedCredentials, selfAttestedAttributes);
    }

    /**
     * Auto accept all selected credentials
     * @param presentationExchange {@link PresentationExchangeRecord}
     * @param selectedCredentials {@link PresentationRequestCredentials}
     * @return {@link SendPresentationRequest}
     */
    public static SendPresentationRequest acceptAll(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> selectedCredentials) {
        return acceptAll(presentationExchange, selectedCredentials, null);
    }

    /**
     * Auto accept all selected credentials
     * @param presentationExchange {@link PresentationExchangeRecord}
     * @param selectedCredentials {@link PresentationRequestCredentials}
     * @param selfAttestedAttributes  map of self attested attributes, k = group name, v = value
     * @return {@link SendPresentationRequest}
     */
    public static SendPresentationRequest acceptAll(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> selectedCredentials,
            @Nullable Map<String, String> selfAttestedAttributes) {
        Map<PresentationRequestCredentials, Boolean> acceptAll = selectedCredentials.stream()
                .map(sel -> Map.entry(sel, Boolean.TRUE))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return buildRequest(presentationExchange, acceptAll, selfAttestedAttributes);
    }

    private static SendPresentationRequest buildRequest(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<PresentationRequestCredentials, Boolean> selectedCredentials,
            @Nullable Map<String, String> selfAttestedAttributes) {

        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> requestedAttributes =
                buildRequestedAttributes(presentationExchange, selectedCredentials);
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> requestedPredicates =
                buildRequestedPredicates(presentationExchange, selectedCredentials);

        return SendPresentationRequest
                .builder()
                .requestedAttributes(requestedAttributes)
                .requestedPredicates(requestedPredicates)
                .selfAttestedAttributes(selfAttestedAttributes)
                .build();
    }

    public static List<PresentationRequestCredentials> filterMatchingCredentialsByReferents(
            @NonNull List<PresentationRequestCredentials> matchingCredentials,
            @Nullable List<String> selectedReferents) {
        if (selectedReferents == null || selectedReferents.isEmpty()) {
            return matchingCredentials;
        }
        return matchingCredentials
                .stream()
                .filter(c -> selectedReferents.contains(c.getCredentialInfo().getReferent()))
                .collect(Collectors.toList());
    }

    public static Map<PresentationRequestCredentials, Boolean> filterMatchingCredentialsByReferents(
            @NonNull List<PresentationRequestCredentials> matchingCredentials,
            @Nullable Map<String, Boolean> selectedReferents) {
        if (selectedReferents == null || selectedReferents.isEmpty()) {
            return matchingCredentials.stream()
                    .map(match -> Map.entry(match, Boolean.TRUE))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return matchingCredentials
                .stream()
                .filter(match -> selectedReferents.containsKey(match.getCredentialInfo().getReferent()))
                .map(match -> {
                    String referent = match.getCredentialInfo().getReferent();
                    return Map.entry(match, selectedReferents.get(referent));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> buildRequestedAttributes(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<PresentationRequestCredentials, Boolean> matchingCredentials) {

        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> result = new LinkedHashMap<>();
        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedAttributes() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedAttributes().keySet();
            requestedReferents.forEach(ref -> {
                // find requested referent in matching wallet credentials
                matchReferent(matchingCredentials, ref).ifPresent(
                        match -> result.put(ref, SendPresentationRequest.IndyRequestedCredsRequestedAttr
                                .builder()
                                .credId(match.getKey())
                                .revealed(match.getValue() != null ? match.getValue() : Boolean.TRUE)
                                .build()));
            });
        }
        return result;
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> buildRequestedPredicates(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull Map<PresentationRequestCredentials, Boolean> matchingCredentials) {
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> result = new LinkedHashMap<>();

        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedPredicates() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedPredicates().keySet();
            requestedReferents.forEach(ref -> matchReferent(matchingCredentials, ref).ifPresent(
                    match -> result.put(ref, SendPresentationRequest.IndyRequestedCredsRequestedPred
                            .builder()
                            .credId(match.getKey())
                            // .timestamp let aca-py do this
                            .build())));
        }
        return result;
    }

    private static Optional<Map.Entry<String, Boolean>> matchReferent(
            @NotNull Map<PresentationRequestCredentials, Boolean> matchingCredentials, String ref) {
        return matchingCredentials
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getPresentationReferents().contains(ref))
                .map(e -> Map.entry(e.getKey().getCredentialInfo(), e.getValue()))
                .map(e -> Map.entry(e.getKey().getReferent(), e.getValue()))
                .findFirst();
    }
}
