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
 * To generate the model for /present-proof/records/{pres_ex_id}/send-presentation
 *
 * @see <a href="https://github.com/hyperledger/aries-rfcs/blob/master/concepts/0441-present-proof-best-practices/README.md">
 *     0441-present-proof-best-practices</a>
 */
public class SendPresentationRequestHelper {

    /**
     * Auto accept all matching credentials
     * @param presentationExchange {@link PresentationExchangeRecord}
     * @param matchingCredentials {@link PresentationRequestCredentials}
     * @return {@link SendPresentationRequest}
     */
    public static Optional<SendPresentationRequest> acceptAll(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {

        Optional<SendPresentationRequest> result = Optional.empty();
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> requestedAttributes =
                buildRequestedAttributes(presentationExchange, matchingCredentials);
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> requestedPredicates =
                buildRequestedPredicates(presentationExchange, matchingCredentials);

        if (!requestedAttributes.isEmpty() || !requestedPredicates.isEmpty()) {
            result = Optional.of(SendPresentationRequest
                    .builder()
                    .requestedAttributes(requestedAttributes)
                    .requestedPredicates(requestedPredicates)
                    .build());
        }
        return result;
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> buildRequestedAttributes(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {

        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedAttr> result = new LinkedHashMap<>();
        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedAttributes() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedAttributes().keySet();
            requestedReferents.forEach(ref -> {
                // find requested referent in matching wallet credentials
                matchReferent(matchingCredentials, ref).ifPresent(
                        match -> result.put(ref, SendPresentationRequest.IndyRequestedCredsRequestedAttr
                                .builder()
                                .credId(match)
                                .revealed(Boolean.TRUE)
                                .build()));
            });
        }
        return result;
    }

    private static Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> buildRequestedPredicates(
            @NonNull PresentationExchangeRecord presentationExchange,
            @NonNull List<PresentationRequestCredentials> matchingCredentials) {
        Map<String, SendPresentationRequest.IndyRequestedCredsRequestedPred> result = new LinkedHashMap<>();

        PresentProofRequest.ProofRequest presentationRequest = presentationExchange.getPresentationRequest();
        if (presentationRequest != null && presentationRequest.getRequestedPredicates() != null) {
            Set<String> requestedReferents = presentationRequest.getRequestedPredicates().keySet();
            requestedReferents.forEach(ref -> matchReferent(matchingCredentials, ref).ifPresent(
                    match -> result.put(ref, SendPresentationRequest.IndyRequestedCredsRequestedPred
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
