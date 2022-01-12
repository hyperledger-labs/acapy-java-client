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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * IndyGEProofPred
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class IndyGEProofPred {
    public static final String SERIALIZED_NAME_ATTR_NAME = "attr_name";
    @SerializedName(SERIALIZED_NAME_ATTR_NAME)
    private String attrName;

    /**
     * Predicate type
     */
    @JsonAdapter(PTypeEnum.Adapter.class)
    public enum PTypeEnum {
        LT("LT"),

        LE("LE"),

        GE("GE"),

        GT("GT");

        private String value;

        PTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static PTypeEnum fromValue(String value) {
            for (PTypeEnum b : PTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public static class Adapter extends TypeAdapter<PTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final PTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public PTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return PTypeEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_P_TYPE = "p_type";
    @SerializedName(SERIALIZED_NAME_P_TYPE)
    private PTypeEnum pType;
    public static final String SERIALIZED_NAME_VALUE = "value";
    @SerializedName(SERIALIZED_NAME_VALUE)
    private Integer value;
}
