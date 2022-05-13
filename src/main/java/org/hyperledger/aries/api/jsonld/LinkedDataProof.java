/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.jsonld;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public final class LinkedDataProof {
    private String challenge;
    private String created;
    private String domain;
    private String jws;
    private String nonce;
    @SerializedName("proofPurpose")
    private String proofPurpose;
    @SerializedName("proofValue")
    private String proofValue;
    private ProofType type;
    @SerializedName("verificationMethod")
    private String verificationMethod;
}
