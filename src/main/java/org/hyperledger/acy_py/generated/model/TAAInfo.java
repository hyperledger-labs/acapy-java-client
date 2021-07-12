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
 * TAAInfo
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class TAAInfo {
    public static final String SERIALIZED_NAME_AML_RECORD = "aml_record";
    @SerializedName(SERIALIZED_NAME_AML_RECORD)
    private AMLRecord amlRecord;
    public static final String SERIALIZED_NAME_TAA_ACCEPTED = "taa_accepted";
    @SerializedName(SERIALIZED_NAME_TAA_ACCEPTED)
    private TAAAcceptance taaAccepted;
    public static final String SERIALIZED_NAME_TAA_RECORD = "taa_record";
    @SerializedName(SERIALIZED_NAME_TAA_RECORD)
    private TAARecord taaRecord;
    public static final String SERIALIZED_NAME_TAA_REQUIRED = "taa_required";
    @SerializedName(SERIALIZED_NAME_TAA_REQUIRED)
    private Boolean taaRequired;
}
