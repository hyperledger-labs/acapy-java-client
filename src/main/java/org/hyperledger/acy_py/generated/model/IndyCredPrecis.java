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

import java.util.List;

/**
 * IndyCredPrecis
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class IndyCredPrecis {
    public static final String SERIALIZED_NAME_CRED_INFO = "cred_info";
    @SerializedName(SERIALIZED_NAME_CRED_INFO)
    private IndyCredInfo credInfo;
    public static final String SERIALIZED_NAME_INTERVAL = "interval";
    @SerializedName(SERIALIZED_NAME_INTERVAL)
    private IndyNonRevocationInterval interval;
    public static final String SERIALIZED_NAME_PRES_REFERENTS = "pres_referents";
    @SerializedName(SERIALIZED_NAME_PRES_REFERENTS)
    private List<String> presReferents = null;
}
