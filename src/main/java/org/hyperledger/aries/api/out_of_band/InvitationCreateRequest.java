/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.aries.api.connection.ConnectionRecord;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InvitationCreateRequest {
    /**
     * List of mime types in order of preference that should be in the response
     * example: List [ "didcomm/aip1", "didcomm/aip2;env=rfc19" ]
     */
    private List<String> accept;
    private String alias;
    @Singular
    private List<AttachmentDef> attachments;
    private String goal;
    @SerializedName("goal_code")
    private String goalCode;
    @Builder.Default
    private List<String> handshakeProtocols = List.of(
            ConnectionRecord.ConnectionProtocol.DID_EXCHANGE_V1.getValue(),
            ConnectionRecord.ConnectionProtocol.CONNECTION_V1.getValue());
    /** Identifier for active mediation record to be used */
    private String mediationId;
    private Object metadata;
    private String myLabel;
    /**
     * OOB protocol version
     * example: 1.1
     */
    private String protocolVersion;
    private Boolean usePublicDid;
}
