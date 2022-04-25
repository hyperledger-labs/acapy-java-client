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

/**
 * EndorserInfo
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class EndorserInfo {
    public static final String SERIALIZED_NAME_ENDORSER_DID = "endorser_did";
    @SerializedName(SERIALIZED_NAME_ENDORSER_DID)
    private String endorserDid;
    public static final String SERIALIZED_NAME_ENDORSER_NAME = "endorser_name";
    @SerializedName(SERIALIZED_NAME_ENDORSER_NAME)
    private String endorserName;
}
