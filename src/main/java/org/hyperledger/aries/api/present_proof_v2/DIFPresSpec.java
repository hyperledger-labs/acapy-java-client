/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
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

import java.util.List;
import java.util.Map;

/**
 * DIFPresSpec
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DIFPresSpec {

    /**
     * Optional, but causes issues if not set.
     * Confusing naming on the aca-py side, because in some cases this is the (public) DID of the holder.
     * If this field is not set the presentation will not be signed correctly and hence not validate
     * correctly on the verifier side.
     */
    private String issuerId;

    /**
     * Optional, but causes issues if not set.
     * Received presentation definition from the presentation request but without the holder information, see
     * {@link V2DIFProofRequest#resetHolderConstraints()}
     */
    private V2DIFProofRequest.PresentationDefinition presentationDefinition;

    /**
     * Optional, but causes issues if not set.
     * Mapping of input_descriptor id to list of stored W3C credential record_id
     * example: {@code "<input descriptor id_1>": ["<record id_1>", "<record id_2>"]}
     */
    private Map<String, List<String>> recordIds;

    /**
     * Optional,
     * reveal doc [JSON-LD frame] dict used to derive the credential when selective disclosure is required
     */
    private JsonObject revealDoc;
}
