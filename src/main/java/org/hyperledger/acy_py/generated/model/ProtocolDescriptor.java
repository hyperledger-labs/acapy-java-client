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

import java.util.List;

/**
 * ProtocolDescriptor
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class ProtocolDescriptor {
    public static final String SERIALIZED_NAME_PID = "pid";
    @SerializedName(SERIALIZED_NAME_PID)
    private String pid;
    public static final String SERIALIZED_NAME_ROLES = "roles";
    @SerializedName(SERIALIZED_NAME_ROLES)
    private List<String> roles = null;
}
