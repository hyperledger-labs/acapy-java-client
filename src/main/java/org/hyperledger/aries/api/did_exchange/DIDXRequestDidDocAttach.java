/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.did_exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DIDXRequestDidDocAttach {

    @SerializedName("@id")
    private String id;

    private Integer byteCount;

    private AttachDecoratorData data;

    private String description;

    private String filename;

    private String lastmodTime;

    @SerializedName("mime-type")
    private String mimeType;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AttachDecoratorData {
        private String base64;
        @JsonSerialize(using = JsonObjectSerializer.class)
        @JsonDeserialize(using = JsonObjectDeserializer.class)
        private JsonObject json;
        private AttachDecoratorDataJws jws;
        private List<String> links;
        /** SHA256 hash (binhex encoded) of content */
        private String sha256;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AttachDecoratorDataJws {

        private AttachDecoratorDataJwsHeader header;

        @SerializedName("protected")
        private String _protected;

        private String signature;

        private List<AttachDecoratorDataJwsSignature> signatures;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AttachDecoratorDataJwsSignature {

        private AttachDecoratorDataJwsHeader header;

        @SerializedName("protected")
        private String _protected;

        private String signature;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AttachDecoratorDataJwsHeader {
        private String kid;
    }
}
