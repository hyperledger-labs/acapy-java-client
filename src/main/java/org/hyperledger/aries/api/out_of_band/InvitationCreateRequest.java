/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import lombok.*;
import org.hyperledger.aries.api.connection.ConnectionRecord;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InvitationCreateRequest {
    private String alias;
    @Singular
    private List<AttachmentDef> attachments;
    @Builder.Default
    private List<String> handshakeProtocols = List.of(
            ConnectionRecord.ConnectionProtocol.DID_EXCHANGE_V1.getValue(),
            ConnectionRecord.ConnectionProtocol.CONNECTION_V1.getValue());
    private String mediationId;
    private Object metadata;
    private String myLabel;
    private Boolean usePublicDid;
}
