/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class RevRegCreateResponse {
    private RevocRegDef revocRegDef;
    private String tailsHash;
    private String recordId;
    private String updatedAt;
    private String state;
    private String revocDefType;
    @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDDEFID)
    private String credDefId;
    private RevocRegEntry revocRegEntry;
    private JsonArray pendingPub;
    private String revocRegId;
    private String createdAt;
    private Long maxCredNum;
    private String tailsLocalPath;
    private String tailsPublicUri;
    private String issuerDid;
    private String tag;

    @Data @NoArgsConstructor
    public static final class RevocRegEntry {
        private String ver;
        @JsonSerialize(using = JsonObjectSerializer.class)
        @JsonDeserialize(using = JsonObjectDeserializer.class)
        private JsonObject value;
    }

    @Data @NoArgsConstructor
    public static final class RevocRegDef {
        private String ver;
        private String id;
        @SerializedName(value = "revocDefType", alternate = "revoc_def_type")
        private String revocDefType;
        private String tag;
        @SerializedName(value = CredDefId.CREDDEFID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
        private String credDefId;
        private RevocRegDefValue value;

        @Data @NoArgsConstructor
        public static final class RevocRegDefValue {
            @SerializedName(value = "issuanceType", alternate = "issuance_type")
            private String issuanceType;
            @SerializedName(value = "maxCredNum", alternate = "max_cred_num")
            private Long maxCredNum;
            @SerializedName(value = "publicKeys", alternate = "public_keys")
            @JsonSerialize(using = JsonObjectSerializer.class)
            @JsonDeserialize(using = JsonObjectDeserializer.class)
            private JsonObject publicKeys;
            @SerializedName(value = "tailsHash", alternate = "tails_hash")
            private String tailsHash;
            @SerializedName(value = "tailsLocation", alternate = "tails_location")
            private String tailsLocation;
        }
    }

    @Data @NoArgsConstructor
    public static final class RevocationModuleResponse {
        // epmty for now
    }

}
