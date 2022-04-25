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
 * IndyRequestedCredsRequestedAttr
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class IndyRequestedCredsRequestedAttr {
    public static final String SERIALIZED_NAME_CRED_ID = "cred_id";
    @SerializedName(SERIALIZED_NAME_CRED_ID)
    private String credId;
    public static final String SERIALIZED_NAME_REVEALED = "revealed";
    @SerializedName(SERIALIZED_NAME_REVEALED)
    private Boolean revealed;
}
