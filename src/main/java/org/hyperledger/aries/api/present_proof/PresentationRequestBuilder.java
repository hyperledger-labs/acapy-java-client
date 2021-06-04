/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Used in scenarios where 'auto-respond-presentation-request' is set to false.
 * The helper takes the result of the following two api calls:
 * 1. /present-proof/records/{pres_ex_id}
 * 2. /present-proof/records/{pres_ex_id}/credentials
 * To generate the model for
 * /present-proof/records/{pres_ex_id}/send-presentation
 *
 * https://github.com/hyperledger/aries-rfcs/blob/master/concepts/0441-present-proof-best-practices/README.md
 */
public class PresentationRequestBuilder {

    /**
     * Auto accept all matching credentials
     * @param presentationExchange {@link PresentationExchangeRecord}
     * @param matchingCredentials {@link PresentationRequestCredentials}
     * @return {@link PresentationRequest}
     */
    public static Optional<PresentationRequest> acceptAll(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {

        Optional<PresentationRequest> result = Optional.empty();
        Map<String, PresentationRequest.IndyRequestedCredsRequestedAttr> requestedAttributes =
                buildRequestedAttributes(presentationExchange, matchingCredentials);
        Map<String, PresentationRequest.IndyRequestedCredsRequestedPred> requestedPredicates =
                buildRequestedPredicates(presentationExchange, matchingCredentials);

        if (!requestedAttributes.isEmpty() || !requestedPredicates.isEmpty()) {
            result = Optional.of(PresentationRequest
                    .builder()
                    .requestedAttributes(requestedAttributes)
                    .requestedPredicates(requestedPredicates)
                    .build());
        }
        return result;
    }

    private static Map<String, PresentationRequest.IndyRequestedCredsRequestedAttr> buildRequestedAttributes(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {

        Map<String, PresentationRequest.IndyRequestedCredsRequestedAttr> result = new LinkedHashMap<>();
        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedAttributes() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedAttributes().keySet();
            requestedReferents.forEach(ref -> {
                // find requested referent in matching wallet credentials
                matchReferent(matchingCredentials, ref).ifPresent(
                        match -> result.put(ref, PresentationRequest.IndyRequestedCredsRequestedAttr
                                .builder()
                                .credId(match)
                                .revealed(Boolean.TRUE)
                                .build()));
            });
        }
        return result;
    }

    private static Map<String, PresentationRequest.IndyRequestedCredsRequestedPred> buildRequestedPredicates(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {
        Map<String, PresentationRequest.IndyRequestedCredsRequestedPred> result = new LinkedHashMap<>();

        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedPredicates() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedPredicates().keySet();
            requestedReferents.forEach(ref -> matchReferent(matchingCredentials, ref).ifPresent(
                    match -> result.put(ref, PresentationRequest.IndyRequestedCredsRequestedPred
                            .builder()
                            .credId(match)
                            // .timestamp let aca-py do this
                            .build())));
        }
        return result;
    }

    private static Optional<String> matchReferent(
            @NotNull List<PresentationRequestCredentials> matchingCredentials, String ref) {
        return matchingCredentials
                .stream()
                .filter(cred -> cred.getPresentationReferents().contains(ref))
                .map(PresentationRequestCredentials::getCredentialInfo)
                .map(PresentationRequestCredentials.CredentialInfo::getReferent)
                .findFirst();
    }
}
