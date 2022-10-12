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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.present_proof.PresentProofRequest.ProofRequest;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.RequestedProofType;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.RevealedAttribute;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.RevealedAttributeGroup;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.pojo.PojoProcessor;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RequestedProofParser {

    private static final Gson gson = GsonConfig.defaultConfig();

    public static Map<String, RevealedAttributeGroup> collectAll(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        Map<String, PresentationExchangeRecord.RevealedAttributeGroup> res = new LinkedHashMap<>();
        res.putAll(collectRevealedGroups(presentation));
        res.putAll(collectRevealedAttributes(presentation, presentationRequest));
        res.putAll(collectPredicates(presentation, presentationRequest));
        res.putAll(collectUnrevealedAttributes(presentation, presentationRequest));
        res.putAll(collectSelfAttestedAttributes(presentation, presentationRequest));
        return res;
    }

    public static Map<String, RevealedAttributeGroup> collectRevealedGroups(@NonNull JsonObject presentation) {
        Map<String, RevealedAttributeGroup> result = new HashMap<>();
        RequestedProofType type = RequestedProofType.REVEALED_ATTR_GROUPS;
        final JsonObject groupsJson = getByType(presentation, type);
        if (groupsJson == null) { // not an attribute group
            return result;
        }
        final Set<String> attrGroupNames = groupsJson.keySet();
        attrGroupNames.forEach(name -> {
            RevealedAttributeGroup.RevealedAttributeGroupBuilder groupBuilder = RevealedAttributeGroup.builder();

            final Set<Map.Entry<String, JsonElement>> attrs = groupsJson
                    .getAsJsonObject(name)
                    .getAsJsonObject("values")
                    .entrySet();
            attrs.forEach(e -> groupBuilder.revealedAttribute(e.getKey(),
                    e.getValue().getAsJsonObject().get("raw").getAsString()));

            int subProofIndex = groupsJson.get(name).getAsJsonObject().get("sub_proof_index").getAsInt();
            groupBuilder
                    .identifier(getIdentifierAtIndex(presentation, subProofIndex))
                    .type(type);

            result.put(name, groupBuilder.build());
        });
        return result;
    }

    public static Map<String, RevealedAttributeGroup> collectRevealedAttributes(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        return collectRevealedAttributes(presentation)
                .entrySet()
                .stream()
                .map(e ->
                    Map.entry(e.getKey(), RevealedAttributeGroup.builder()
                            .revealedAttributes(Map.of(findAttributeNameForRevealedAttribute(e.getKey(),
                                    presentationRequest), e.getValue().getRaw()))
                            .identifier(getIdentifierAtIndex(presentation, e.getValue().getSubProofIndex()))
                            .type(RequestedProofType.REVEALED_ATTRS)
                            .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ;
    }

    public static Map<String, RevealedAttribute> collectRevealedAttributes(@NonNull JsonObject presentation) {
        return getRevealedAttributes(presentation)
                .stream()
                .collect(Collectors
                        .toMap(
                                Map.Entry::getKey,
                                v -> gson.fromJson(v.getValue(), RevealedAttribute.class)))
                ;
    }

    public static Map<String, String> collectRevealedAttributesValues(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        return collectRevealedAttributes(presentation)
                .entrySet()
                .stream()
                .map(e -> Map.entry(
                        findAttributeNameForRevealedAttribute(e.getKey(), presentationRequest),
                        e.getValue().getRaw()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, RevealedAttributeGroup> collectPredicates(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        Map<String, RevealedAttributeGroup> predicates = collectIdentifiers(presentation, RequestedProofType.PREDICATES);
        predicates.forEach((key, value) -> {
            if (presentationRequest.hasRequestedPredicates()) {
                ProofRequest.ProofRequestedPredicates pred = presentationRequest.getRequestedPredicates().get(key);
                value.setRequestedPredicates(pred);
            }
        });
        return predicates;
    }

    public static Map<String, RevealedAttributeGroup> collectUnrevealedAttributes(
            @NonNull JsonObject presentation,  @NonNull ProofRequest presentationRequest) {
        Map<String, RevealedAttributeGroup> notRevealed = collectIdentifiers(presentation,
                RequestedProofType.UNREVEALED_ATTRS);
        notRevealed.forEach((key, value) -> {
            if (presentationRequest.hasRequestedAttributes()) {
                ProofRequest.ProofRequestedAttributes attr = presentationRequest.getRequestedAttributes().get(key);
                Set<String> names = new HashSet<>();
                if (StringUtils.isNotEmpty(attr.getName()))
                    names.add(attr.getName());
                if (attr.getNames() != null && attr.getNames().size() > 0)
                    names.addAll(attr.getNames());
                if (names.size() > 0)
                    value.setRevealedAttributes(names.stream().map(n -> Map.entry(n, ""))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            }
        });
        return notRevealed;
    }

    public static Map<String, RevealedAttributeGroup> collectSelfAttestedAttributes(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        RequestedProofType type = RequestedProofType.SELF_ATTESTED_ATTRS;
        JsonObject selfAttested = getByType(presentation, type);
        return selfAttested != null ? selfAttested.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), RevealedAttributeGroup.builder()
                        .type(type)
                        .revealedAttribute(findAttributeNameForRevealedAttribute(e.getKey(), presentationRequest),
                                e.getValue().getAsString())
                        .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : Map.of()
                ;
    }

    private static Map<String, RevealedAttributeGroup> collectIdentifiers(
            @NonNull JsonObject presentation, @NonNull RequestedProofType type) {
        JsonObject byType = getByType(presentation, type);
        return byType != null ? byType.entrySet().stream()
                .map(e ->
                        Map.entry(e.getKey(), RevealedAttributeGroup.builder()
                                .identifier(getIdentifierAtIndex(presentation,
                                        e.getValue().getAsJsonObject().get("sub_proof_index").getAsInt()))
                                .type(type)
                                .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : Map.of()
        ;
    }

    private static PresentationExchangeRecord.Identifier getIdentifierAtIndex(@NonNull JsonObject presentation, int index) {
        JsonElement identifier = presentation
                .getAsJsonArray("identifiers")
                .get(index);
        return gson.fromJson(identifier, PresentationExchangeRecord.Identifier.class);
    }

    private static JsonObject getRequestedProof(@NonNull JsonObject presentation) {
        return presentation
                .getAsJsonObject("requested_proof")
                ;
    }

    private static Set<Map.Entry<String, JsonElement>> getRevealedAttributes(@NonNull JsonObject presentation) {
        JsonObject revealedAttrs = getByType(presentation, RequestedProofType.REVEALED_ATTRS);
        return revealedAttrs == null ? Set.of() : revealedAttrs.entrySet();
    }

    private static JsonObject getByType(@NonNull JsonObject presentation, @NonNull RequestedProofType type) {
        JsonObject requestedProof = getRequestedProof(presentation);
        return requestedProof.has(type.getName())
                ? requestedProof.getAsJsonObject(type.getName())
                : null;
    }

    private static String findAttributeNameForRevealedAttribute(
            @NonNull String key, @NonNull ProofRequest presentationRequest) {
        return presentationRequest.getRequestedAttributes().get(key).getName();
    }

    /**
     * Converts the present_proof.presentation into an instance of the provided class type:
     * @param <T> The class type
     * @param presentation present_proof.presentation
     * @param type POJO instance
     * @return Instantiated type with all matching properties set
     */
    public static <T> T from(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest, @NonNull Class<T> type) {

        Map<String, String> nameToValue;

        if (PojoProcessor.hasAttributeGroupName(type)) {
            Map<String, RevealedAttributeGroup> allGroups = collectAll(presentation, presentationRequest);
            RevealedAttributeGroup group = allGroups.get(PojoProcessor.getAttributeGroupName(type));
            nameToValue = group != null ? group.getRevealedAttributes() : Map.of();
        } else {
            // brute force
            nameToValue = aggregateAll(presentation, presentationRequest);
        }

        return PojoProcessor.setValues(type, nameToValue);
    }

    public static Map<String, Object> from(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest, @NonNull Set<String> names) {

        Map<String, Object> result = new HashMap<>();
        Map<String, String> all = aggregateAll(presentation, presentationRequest);

        names.forEach(name -> {
            String value = all.get(name);
            if (StringUtils.isNotEmpty(value)) {
                result.put(name, value);
            }
        });
        return result;
    }

    private static Map<String, String> aggregateAll(
            @NonNull JsonObject presentation, @NonNull ProofRequest presentationRequest) {
        Map<String, RevealedAttributeGroup> all = collectAll(presentation, presentationRequest);
        return all.values().stream()
                .filter(revealedAttributeGroup -> revealedAttributeGroup.getRevealedAttributes() != null)
                .map(revealedAttributeGroup -> revealedAttributeGroup.getRevealedAttributes().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
