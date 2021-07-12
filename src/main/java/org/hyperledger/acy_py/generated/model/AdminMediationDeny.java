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
 * AdminMediationDeny
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class AdminMediationDeny {
    public static final String SERIALIZED_NAME_MEDIATOR_TERMS = "mediator_terms";
    @SerializedName(SERIALIZED_NAME_MEDIATOR_TERMS)
    private List<String> mediatorTerms = null;
    public static final String SERIALIZED_NAME_RECIPIENT_TERMS = "recipient_terms";
    @SerializedName(SERIALIZED_NAME_RECIPIENT_TERMS)
    private List<String> recipientTerms = null;
}
