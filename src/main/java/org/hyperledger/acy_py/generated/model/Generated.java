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
 * Generated
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class Generated {
    public static final String SERIALIZED_NAME_MASTER_SECRET = "master_secret";
    @SerializedName(SERIALIZED_NAME_MASTER_SECRET)
    private String masterSecret;
    public static final String SERIALIZED_NAME_NUMBER = "number";
    @SerializedName(SERIALIZED_NAME_NUMBER)
    private String number;
    public static final String SERIALIZED_NAME_REMAINDER = "remainder";
    @SerializedName(SERIALIZED_NAME_REMAINDER)
    private String remainder;
}
