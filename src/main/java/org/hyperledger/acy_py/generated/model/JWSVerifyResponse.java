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
 * JWSVerifyResponse
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class JWSVerifyResponse {
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName(SERIALIZED_NAME_ERROR)
    private String error;
    public static final String SERIALIZED_NAME_HEADERS = "headers";
    @SerializedName(SERIALIZED_NAME_HEADERS)
    private Object headers;
    public static final String SERIALIZED_NAME_KID = "kid";
    @SerializedName(SERIALIZED_NAME_KID)
    private String kid;
    public static final String SERIALIZED_NAME_PAYLOAD = "payload";
    @SerializedName(SERIALIZED_NAME_PAYLOAD)
    private Object payload;
    public static final String SERIALIZED_NAME_VALID = "valid";
    @SerializedName(SERIALIZED_NAME_VALID)
    private Boolean valid;
}
