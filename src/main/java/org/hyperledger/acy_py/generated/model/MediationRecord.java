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
 * MediationRecord
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class MediationRecord {
    public static final String SERIALIZED_NAME_CONNECTION_ID = "connection_id";
    @SerializedName(SERIALIZED_NAME_CONNECTION_ID)
    private String connectionId;
    public static final String SERIALIZED_NAME_CREATED_AT = "created_at";
    @SerializedName(SERIALIZED_NAME_CREATED_AT)
    private String createdAt;
    public static final String SERIALIZED_NAME_ENDPOINT = "endpoint";
    @SerializedName(SERIALIZED_NAME_ENDPOINT)
    private String endpoint;
    public static final String SERIALIZED_NAME_MEDIATION_ID = "mediation_id";
    @SerializedName(SERIALIZED_NAME_MEDIATION_ID)
    private String mediationId;
    public static final String SERIALIZED_NAME_MEDIATOR_TERMS = "mediator_terms";
    @SerializedName(SERIALIZED_NAME_MEDIATOR_TERMS)
    private List<String> mediatorTerms = null;
    public static final String SERIALIZED_NAME_RECIPIENT_TERMS = "recipient_terms";
    @SerializedName(SERIALIZED_NAME_RECIPIENT_TERMS)
    private List<String> recipientTerms = null;
    public static final String SERIALIZED_NAME_ROLE = "role";
    @SerializedName(SERIALIZED_NAME_ROLE)
    private String role;
    public static final String SERIALIZED_NAME_ROUTING_KEYS = "routing_keys";
    @SerializedName(SERIALIZED_NAME_ROUTING_KEYS)
    private List<String> routingKeys = null;
    public static final String SERIALIZED_NAME_STATE = "state";
    @SerializedName(SERIALIZED_NAME_STATE)
    private String state;
    public static final String SERIALIZED_NAME_UPDATED_AT = "updated_at";
    @SerializedName(SERIALIZED_NAME_UPDATED_AT)
    private String updatedAt;
}
