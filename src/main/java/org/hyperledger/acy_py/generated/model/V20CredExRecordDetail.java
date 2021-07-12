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
 * V20CredExRecordDetail
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class V20CredExRecordDetail {
    public static final String SERIALIZED_NAME_CRED_EX_RECORD = "cred_ex_record";
    @SerializedName(SERIALIZED_NAME_CRED_EX_RECORD)
    private V20CredExRecord credExRecord;
    public static final String SERIALIZED_NAME_INDY = "indy";
    @SerializedName(SERIALIZED_NAME_INDY)
    private V20CredExRecordIndy indy;
    public static final String SERIALIZED_NAME_LD_PROOF = "ld_proof";
    @SerializedName(SERIALIZED_NAME_LD_PROOF)
    private V20CredExRecordLDProof ldProof;
}
