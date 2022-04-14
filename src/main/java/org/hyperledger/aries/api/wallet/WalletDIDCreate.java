/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.acy_py.generated.model.DIDCreateOptions;

import java.io.IOException;

/**
 * DIDCreate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletDIDCreate {
    private DIDCreate.MethodEnum method;
    private DIDCreateOptions options;
    /** If this parameter is set,
     * allows to use a custom seed to create a local DID.
     * needs --wallet-allow-insecure-seed to be set */
    private String seed;
}
