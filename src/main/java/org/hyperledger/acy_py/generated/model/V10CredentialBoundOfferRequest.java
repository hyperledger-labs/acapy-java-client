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
 * V10CredentialBoundOfferRequest
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class V10CredentialBoundOfferRequest {
    public static final String SERIALIZED_NAME_COUNTER_PROPOSAL = "counter_proposal";
    @SerializedName(SERIALIZED_NAME_COUNTER_PROPOSAL)
    private CredentialProposal counterProposal;
}
