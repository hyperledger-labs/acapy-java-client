/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.schema;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SchemaSendResponse {
    private String schemaId;
    private Schema schema;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static final class Schema {
        private String ver;
        private String id;
        private String name;
        private String version;
        @SerializedName("attrNames")
        private List<String> attrNames;
        @SerializedName("seqNo")
        private Integer seqNo;
    }
}
