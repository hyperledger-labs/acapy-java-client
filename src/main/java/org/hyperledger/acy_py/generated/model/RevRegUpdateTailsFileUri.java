/*
 * aca-py client
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0.7.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.hyperledger.acy_py.generated.model;

import com.google.gson.annotations.SerializedName;

/**
 * RevRegUpdateTailsFileUri
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class RevRegUpdateTailsFileUri {
    public static final String SERIALIZED_NAME_TAILS_PUBLIC_URI = "tails_public_uri";
    @SerializedName(SERIALIZED_NAME_TAILS_PUBLIC_URI)
    private String tailsPublicUri;
}
