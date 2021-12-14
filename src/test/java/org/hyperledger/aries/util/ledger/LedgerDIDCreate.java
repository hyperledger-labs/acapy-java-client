/*
 * aca-py client
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0.7.2
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
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
public class LedgerDIDCreate {
    public static final String SERIALIZED_NAME_ALIAS = "alias";
    @SerializedName(SERIALIZED_NAME_ALIAS)
    private String alias;

    public static final String SERIALIZED_NAME_DID = "did";
    @SerializedName(SERIALIZED_NAME_DID)
    private String did;

    public static final String SERIALIZED_NAME_ROLE = "role";
    @SerializedName(SERIALIZED_NAME_ROLE)
    private String role;

    public static final String SERIALIZED_NAME_SEED = "seed";
    @SerializedName(SERIALIZED_NAME_SEED)
    private String seed;

    public static final String SERIALIZED_NAME_VERKEY = "verkey";
    @SerializedName(SERIALIZED_NAME_VERKEY)
    private String verkey;
}
