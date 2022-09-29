/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hyperledger.aries.config.GsonConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestedProofParser {

    private static final Gson gson = GsonConfig.defaultConfig();

    public static Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectAll(
            @NonNull JsonObject presentation, @NonNull PresentProofRequest.ProofRequest presentationRequest) {
        Map<String, PresentationExchangeRecord.RevealedAttributeGroup> res = new LinkedHashMap<>();
        res.putAll(collectRevealedGroups(presentation));
        res.putAll(collectRevealedAttributes(presentation, presentationRequest));
        return res;
    }

    public static Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectRevealedGroups(@NonNull JsonObject presentation) {
        Map<String, PresentationExchangeRecord.RevealedAttributeGroup> result = new HashMap<>();
        final JsonObject groupsJson = getByType(presentation, RequestedProofType.REVEALED_ATTR_GROUPS);
        if (groupsJson == null) { // not an attribute group
            return result;
        }
        final Set<String> attrGroupNames = groupsJson.keySet();
        attrGroupNames.forEach(name -> {
            PresentationExchangeRecord.RevealedAttributeGroup
                    .RevealedAttributeGroupBuilder groupBuilder = PresentationExchangeRecord.RevealedAttributeGroup.builder();

            final Set<Map.Entry<String, JsonElement>> attrs = groupsJson
                    .getAsJsonObject(name)
                    .getAsJsonObject("values")
                    .entrySet();
            attrs.forEach(e -> groupBuilder.revealedAttribute(e.getKey(), e.getValue().getAsJsonObject().get("raw").getAsString()));

            int subProofIndex = groupsJson.get(name).getAsJsonObject().get("sub_proof_index").getAsInt();
            groupBuilder.identifier(getIdentifierAtIndex(presentation, subProofIndex));

            result.put(name, groupBuilder.build());
        });
        return result;
    }

    public static Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectRevealedAttributes(
            @NonNull JsonObject presentation, @NonNull PresentProofRequest.ProofRequest presentationRequest) {
        return getRevealedAttributes(presentation)
                .stream()
                .collect(Collectors
                        .toMap(
                                Map.Entry::getKey,
                                v -> gson.fromJson(v.getValue(), PresentationExchangeRecord.RevealedAttribute.class)))
                .entrySet()
                .stream()
                .map(e ->
                    Map.entry(e.getKey(), PresentationExchangeRecord.RevealedAttributeGroup
                            .builder()
                            .revealedAttributes(Map.of(findAttributeNameForRevealedAttribute(e.getKey(),
                                    presentationRequest), e.getValue().getRaw()))
                            .identifier(getIdentifierAtIndex(presentation, e.getValue().getSubProofIndex()))
                            .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ;
    }

    public static Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectPredicates(
            @NonNull JsonObject presentation) {
        return getByType(presentation, RequestedProofType.PREDICATES).entrySet().stream()
                .map(e ->
                        Map.entry(e.getKey(), PresentationExchangeRecord.RevealedAttributeGroup
                                        .builder()
                                        .identifier(getIdentifierAtIndex(presentation,
                                                e.getValue().getAsJsonObject().get("sub_proof_index").getAsInt()))
                                        .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        ;
    }

    public Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectUnrevealedAttributes() {
        return Map.of();
    }

    public Map<String, PresentationExchangeRecord.RevealedAttributeGroup> collectSelfAttestedAttributes() {
        return Map.of();
    }

    private static Set<Map.Entry<String, JsonElement>> getRevealedAttributes(@NonNull JsonObject presentation) {
        JsonObject revealedAttrs = getByType(presentation, RequestedProofType.REVEALED_ATTRS);
        return revealedAttrs == null ? Set.of() : revealedAttrs.entrySet();
    }

    private static JsonObject getByType(@NonNull JsonObject presentation, @NonNull RequestedProofType type) {
        JsonObject requestedProof = getRequestedProof(presentation);
        return requestedProof.has(type.name)
                ? requestedProof.getAsJsonObject(type.name)
                : null;
    }

    private static JsonObject getRequestedProof(@NonNull JsonObject presentation) {
        return presentation
                .getAsJsonObject("requested_proof")
                ;
    }

    private static PresentationExchangeRecord.Identifier getIdentifierAtIndex(@NonNull JsonObject presentation, int index) {
        JsonElement identifier = presentation
                .getAsJsonObject("identifiers")
                .getAsJsonArray()
                .get(index);
        return gson.fromJson(identifier, PresentationExchangeRecord.Identifier.class);
    }

    private static String findAttributeNameForRevealedAttribute(
            @NonNull String key, @NonNull PresentProofRequest.ProofRequest presentationRequest) {
        return presentationRequest.getRequestedAttributes().get(key).getName();
    }


    @AllArgsConstructor
    public enum RequestedProofType {
        REVEALED_ATTRS("revealed_attrs"),
        REVEALED_ATTR_GROUPS("revealed_attr_groups"),
        SELF_ATTESTED_ATTRS("self_attested_attrs"),
        UNREVEALED_ATTRS("unrevealed_attrs"),
        PREDICATES("predicates")
        ;

        private final String name;
    }
}
