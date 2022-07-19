/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.DIFField.PredicateEnum;

import java.util.List;

/**
 * DIFField as part of the Input Descriptor Object
 * @see <a href="https://identity.foundation/presentation-exchange/#input-descriptor-object">input-descriptor-object</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DIFField {

    private Filter filter;

    private String id;

    /** array of JSONPath string expressions */
    private List<String> path;

    /**
     * required: limit submitted fields to those listed in the fields array (if present).
     * preferred: submitted fields should be limited to those listed in the fields array (if present).
     */
    private PredicateEnum predicate;

    private String purpose;

    /**
     * A JSON Schema descriptor used to filter against the values returned from evaluation
     * of the JSONPath string expressions in the path array.
     * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6">rfc.section.6</a>
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Filter {

        // Note, only the following four filters are supported by aca-py

        /**
         * Value may be of any type including null
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.1.3">rfc.section.6.1.3</a>
         */
        @SerializedName("const")
        private Object _const;

        /**
         * Same as const but as list where there has to be at least one match
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.1.2">rfc.section.6.1.2</a>
         */
        @SerializedName(value = "enum", alternate = {"enums"})
        private List<Object> _enum;

        @SerializedName(value = "not", alternate = {"_not"})
        private Boolean not;

        @SerializedName(value = "type", alternate = {"_type"})
        private Type type;

        /**
         * Not supported by aca-py
         * less than (not equal to)
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.3">rfc.section.6.2.3</a>
         */
        @SerializedName(value = "exclusiveMaximum", alternate = {"exclusive_maximum", "exclusive_max"})
        private String exclusiveMaximum;

        /**
         * Not supported by aca-py
         * greater than (not equal to)
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.5">rfc.section.6.2.5</a>
         */
        @SerializedName(value = "exclusiveMinimum", alternate = {"exclusive_minimum", "exclusive_min"})
        private String exclusiveMinimum;

        /**
         * Not supported by aca-py
         * less than or exactly equal to
         */
        private String maximum;

        /**
         * Not supported by aca-py
         * greater than or exactly equal to
         */
        private String minimum;

        /**
         * Not supported by aca-py
         * String length, valid if length is less than, or equal to
         */
        @SerializedName(value = "maxLength", alternate = {"max_length"})
        private Integer maxLength;

        /**
         * Not supported by aca-py
         * String length,  valid if length is greater than, or equal to
         */
        @SerializedName(value = "minLength", alternate = {"min_length"})
        private Integer minLength;

        /**
         * Not supported by aca-py
         * RFC, ISO... standard describing email, hostname, datetime etc.
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.7">rfc.section.7</a>
         */
        @SerializedName(value = "format", alternate = {"fmt"})
        private String format;

        /**
         * Not supported by aca-py
         * ECMA-262 regular expression
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.3.3">rfc.section.6.3.3</a>
         */
        private String pattern;

        /**
         * JSON schema type
         * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.1.1">rfc.section.6.1.1</a>
         */
        public enum Type {
            @JsonProperty("null")
            @SerializedName("null")
            NULL,

            @JsonProperty("boolean")
            @SerializedName("boolean")
            BOOLEAN,

            @JsonProperty("object")
            @SerializedName("object")
            OBJECT,

            @JsonProperty("array")
            @SerializedName("array")
            ARRAY,

            @JsonProperty("number")
            @SerializedName("number")
            NUMBER,

            @JsonProperty("string")
            @SerializedName("string")
            STRING,

            @JsonProperty("integer")
            @SerializedName("integer")
            INTEGER
        }
    }
}
