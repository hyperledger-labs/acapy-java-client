/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RevocationInfoTest {

    @Test
    void testParseRevocationNotification() {
        RevocationNotificationBase.RevocationInfo info = RevocationNotificationEvent.builder()
                .threadId("indy::test::12")
                .build()
                .toRevocationInfo();
        assertEquals("test", info.getRevRegId());
        assertEquals("12", info.getCredRevId());
        assertThrows(IllegalArgumentException.class,
                () -> RevocationNotificationEvent.builder().threadId("12:foo:1::12").build().toRevocationInfo());
    }

    @Test
    void testParseRevocationNotificationV2() {
        RevocationNotificationBase.RevocationInfo info = RevocationNotificationEventV2.builder()
                .credentialId("test::12")
                .build()
                .toRevocationInfo();
        assertEquals("test", info.getRevRegId());
        assertEquals("12", info.getCredRevId());
        assertThrows(IllegalArgumentException.class,
                () -> RevocationNotificationEventV2.builder().credentialId("foo::1::12").build().toRevocationInfo());
    }
}
