/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.aries.AriesWebSocketClient;
import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class OOBRecordIntegrationTest extends IntegrationTestBase {

    @Test
    void testOOBCreateInvitation() throws Exception {
        InvitationCreateRequest invCreate = InvitationCreateRequest.builder()
                .alias("test")
                .usePublicDid(false)
                .build();
        CreateInvitationFilter invFilter = CreateInvitationFilter.builder()
                .autoAccept(Boolean.TRUE)
                .multiUse(Boolean.TRUE)
                .build();

        try (AriesWebSocketClient ws = AriesWebSocketClient.builder()
                .url(getWSAdminUrl())
                .build()) {

            InvitationRecord invitationRecord = ac.outOfBandCreateInvitation(invCreate, invFilter).orElseThrow();

            UUID oobInvitationMsgId = Objects.requireNonNull(ws.outOfBand().blockFirst(Duration.ofSeconds(5))).getInviMsgId();
            Assertions.assertEquals(invitationRecord.getInviMsgId(), oobInvitationMsgId.toString());
        }
    }
}
