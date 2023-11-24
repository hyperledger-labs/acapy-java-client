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
 * ConfigurableWriteLedgers
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class ConfigurableWriteLedgers {
    public static final String SERIALIZED_NAME_WRITE_LEDGERS = "write_ledgers";
    @SerializedName(SERIALIZED_NAME_WRITE_LEDGERS)
    private List<String> writeLedgers = null;
}
