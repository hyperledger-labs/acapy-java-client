/*
 * aca-py client
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0.7.3
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.hyperledger.acy_py.generated.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * IndyEQProof
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class IndyEQProof {
    public static final String SERIALIZED_NAME_A_PRIME = "a_prime";
    @SerializedName(SERIALIZED_NAME_A_PRIME)
    private String aPrime;
    public static final String SERIALIZED_NAME_E = "e";
    @SerializedName(SERIALIZED_NAME_E)
    private String e;
    public static final String SERIALIZED_NAME_M = "m";
    @SerializedName(SERIALIZED_NAME_M)
    private Map<String, String> m = null;
    public static final String SERIALIZED_NAME_M2 = "m2";
    @SerializedName(SERIALIZED_NAME_M2)
    private String m2;
    public static final String SERIALIZED_NAME_REVEALED_ATTRS = "revealed_attrs";
    @SerializedName(SERIALIZED_NAME_REVEALED_ATTRS)
    private Map<String, String> revealedAttrs = null;
    public static final String SERIALIZED_NAME_V = "v";
    @SerializedName(SERIALIZED_NAME_V)
    private String v;
}
