/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.Gson;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvitationMessageTest {

    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testTypedSerializationWithDidServiceList() {
        InvitationMessage<String> inv1 = InvitationMessage
                .<String>builder()
                .service("did:sov:1")
                .service("did:sov:2")
                .build();
        String inviteJson = gson.toJson(inv1);
        InvitationMessage<String> inv2 = gson.fromJson(inviteJson, InvitationMessage.STRING_TYPE);
        Assertions.assertEquals(inv1, inv2);
    }

    @Test
    void testTypedSerializationWithRFCServiceList() {
        InvitationMessage<InvitationMessage.InvitationMessageService> inv1 = InvitationMessage
                .<InvitationMessage.InvitationMessageService>builder()
                .service(InvitationMessage.InvitationMessageService.builder().serviceEndpoint("https://foo.bar").build())
                .build();
        String inviteJson = gson.toJson(inv1);
        InvitationMessage<InvitationMessage.InvitationMessageService> inv2 = gson.fromJson(inviteJson,
                InvitationMessage.RFC0067_TYPE);
        Assertions.assertEquals(inv1, inv2);
    }
}
