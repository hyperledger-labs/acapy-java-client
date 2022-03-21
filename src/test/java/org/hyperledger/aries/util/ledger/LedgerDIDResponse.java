/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.util.ledger;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * DID
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class LedgerDIDResponse {
    public static final String SERIALIZED_NAME_DID = "did";
    @SerializedName(SERIALIZED_NAME_DID)
    private String did;

    public static final String SERIALIZED_NAME_SEED = "seed";
    @SerializedName(SERIALIZED_NAME_SEED)
    private String seed;

    public static final String SERIALIZED_NAME_VERKEY = "verkey";
    @SerializedName(SERIALIZED_NAME_VERKEY)
    private String verkey;
}
