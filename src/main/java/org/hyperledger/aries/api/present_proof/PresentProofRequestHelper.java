/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.pojo.PojoProcessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class that provides some convenience methods to build a {@link PresentProofRequest}
 * For more complex usecases use the builder methods directly in the class.
 */
@SuppressWarnings("unused")
public class PresentProofRequestHelper {

    /**
     * Appends the restrictions for each field in the attribute list
     *
     * <pre>{@code
     * "name":"proof_req_2",
     * "version":"0.1",
     * "requested_attributes": {
     *     "attr1": {
     *         "name":"attr1",
     *         "restrictions": [
     *         {
     *             "schema_id": "123:2:mySchema:1.0"
     *         }, {}]
     *     },
     *     "attr2": {
     *         "name":"attr2",
     *         "restrictions": [
     *         {
     *             "schema_id": "123:2:mySchema:1.0"
     *         }, {}]
     *     }
     * }
     * "requested_predicates": {}
     * }</pre>
     * @param connectionId The connection id
     * @param attributes Set of schema attributes
     * @param restrictions list of {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @return {@link PresentProofRequest}
     */
    public static PresentProofRequest buildForEachAttribute(
            String connectionId,
            @NonNull Set<String> attributes,
            @NonNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions) {

        Map<String, PresentProofRequest.ProofRequest.ProofRequestedAttributes> attr = new LinkedHashMap<>();
        attributes.forEach(name -> attr.put(name,
                PresentProofRequest.ProofRequest.ProofRequestedAttributes
                .builder()
                .name(name)
                .restrictions(restrictions.stream().map(PresentProofRequest.ProofRequest.
                        ProofRestrictions::toJsonObject).collect(Collectors.toList()))
                .build()));
        return PresentProofRequest
                .builder()
                .proofRequest(PresentProofRequest.ProofRequest
                        .builder()
                        .requestedAttributes(attr)
                        .build())
                .connectionId(connectionId)
                .build();
    }

    /**
     * Appends the restriction for each attribute within the list
     * @param connectionId The connection id
     * @param attributes Set of schema attributes
     * @param restrictions {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @return {@link PresentProofRequest}
     */
    public static PresentProofRequest buildForEachAttribute(
            String connectionId,
            @NonNull Set<String> attributes,
            PresentProofRequest.ProofRequest.ProofRestrictions restrictions) {
        return buildForEachAttribute(connectionId, attributes, restrictions != null ?  List.of(restrictions) : List.of());
    }

    /**
     * Appends the restrictions for each field of the Pojo class
     * @param connectionId The connection id
     * @param attributes Pojo class that represents the schema and its attributes
     * @param restrictions {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @param <T> Class type
     * @return {@link PresentProofRequest}
     */
    public static <T> PresentProofRequest buildForEachAttribute(
            String connectionId,
            @NonNull Class<T> attributes,
            @Nullable List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions) {
        return buildForEachAttribute(connectionId, PojoProcessor.fieldNames(attributes),
                restrictions != null ? restrictions : List.of());
    }

    /**
     * Appends the restriction for each field of the Pojo class
     * @param connectionId The connection id
     * @param attributes Pojo class that represents the schema and its attributes
     * @param restrictions {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @param <T> Class type
     * @return {@link PresentProofRequest}
     */
    public static <T> PresentProofRequest buildForEachAttribute(
            String connectionId,
            @NonNull Class<T> attributes,
            @Nullable PresentProofRequest.ProofRequest.ProofRestrictions restrictions) {
        return buildForEachAttribute(connectionId, PojoProcessor.fieldNames(attributes), restrictions);
    }

    /**
     * Appends the restrictions to the attribute list
     * @param connectionId The connection id
     * @param attributes List of schema attributes
     * @param restrictions list of {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @return {@link PresentProofRequest}
     */
    public static PresentProofRequest buildForAllAttributes(
            String connectionId,
            @NonNull Set<String> attributes,
            @NonNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions) {
        return buildForAllAttributes(connectionId, null, attributes, restrictions, null);
    }

    /**
     * Appends the restrictions to the attribute list
     *
     * <pre>{@code
     * "name":"proof_req_2",
     * "version":"0.1",
     * "requested_attributes": {
     *     "attribute_group_0": {
     *         "names":["attr1", "attr2"],
     *         "restrictions": [{
     *             "schema_id": "123:2:mySchema:1.0",
     *             "issuer_did": "issuer1"
     *         },
     *         {
     *             "schema_id": "123:2:otherSchema:1.0",
     *             "issuer_did": "issuer2"
     *         }]
     *     }]
     * }
     * "requested_predicates": {}
     * }</pre>
     * @param connectionId The connection id
     * @param attributeGroupName nullable name of the attribute group, will be 'attribute_group_0' if null
     * @param attributes List of schema attributes
     * @param restrictions list of {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @param nonRevoked {@link PresentProofRequest.ProofRequest.ProofNonRevoked}
     * @return {@link PresentProofRequest}
     */
    public static PresentProofRequest buildForAllAttributes(
            String connectionId,
            String attributeGroupName,
            @NonNull Set<String> attributes,
            @NonNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions,
            PresentProofRequest.ProofRequest.ProofNonRevoked nonRevoked) {
        PresentProofRequest.ProofRequest.ProofRequestedAttributes attr = buildAttributeForAll(
                attributes, restrictions, nonRevoked);
        return buildProofRequest(
                connectionId, Map.of(
                        StringUtils.isEmpty(attributeGroupName) ? "attribute_group_0" : attributeGroupName,
                        attr));
    }

    /**
     * Appends the restrictions to the attribute list
     * @param connectionId The connection id
     * @param type pojo class that will be converted into a list of schema attributes
     * @param restrictions list of {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @return {@link PresentProofRequest}
     */
    public static <T> PresentProofRequest buildForAllAttributes(
            String connectionId,
            @NonNull Class<T> type,
            @NonNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions) {
        return buildForAllAttributes(
                connectionId,
                PojoProcessor.getAttributeGroupName(type),
                PojoProcessor.fieldNames(type),
                restrictions,
                null);
    }

    /**
     * Appends the restrictions to the attribute list
     * @param connectionId The connection id
     * @param type pojo class that will be converted into a list of schema attributes
     * @param restrictions list of {@link PresentProofRequest.ProofRequest.ProofRestrictions}
     * @param nonRevoked {@link PresentProofRequest.ProofRequest.ProofNonRevoked}
     * @return {@link PresentProofRequest}
     */
    public static <T> PresentProofRequest buildForAllAttributes(
            String connectionId,
            @NonNull Class<T> type,
            @NonNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions,
            PresentProofRequest.ProofRequest.ProofNonRevoked nonRevoked) {
        return buildForAllAttributes(
                connectionId,
                PojoProcessor.getAttributeGroupName(type),
                PojoProcessor.fieldNames(type),
                restrictions,
                nonRevoked);
    }

    public static PresentProofRequest.ProofRequest.ProofRequestedAttributes buildAttributeForAll(
            @NotNull Set<String> attributes,
            @NotNull List<PresentProofRequest.ProofRequest.ProofRestrictions> restrictions,
            PresentProofRequest.ProofRequest.ProofNonRevoked nonRevoked) {
        return PresentProofRequest.ProofRequest.ProofRequestedAttributes
                .builder()
                .names(new ArrayList<>(attributes))
                .nonRevoked(nonRevoked)
                .restrictions(restrictions.size() > 0
                        ? restrictions.stream().map(PresentProofRequest.ProofRequest.
                        ProofRestrictions::toJsonObject).collect(Collectors.toList())
                        : List.of(PresentProofRequest.ProofRequest.ProofRestrictions
                        .builder().build().toJsonObject()))
                .build();
    }

    public static PresentProofRequest buildProofRequest(
            String connectionId,
            @NonNull Map<String, PresentProofRequest.ProofRequest.ProofRequestedAttributes> attrs) {
        return PresentProofRequest
                .builder()
                .proofRequest(PresentProofRequest.ProofRequest
                        .builder()
                        .requestedAttributes(attrs)
                        .build())
                .connectionId(connectionId)
                .build();
    }
}
