/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OOBRecordTest {

    @Test
    void testSerialiseFromEvent() {
        OOBRecord oob = GsonConfig.defaultConfig().fromJson(OOB_MESSAGE, OOBRecord.class);
        Assertions.assertEquals(OOBRecord.OOBRole.RECEIVER, oob.getRole());
        Assertions.assertEquals(OOBRecord.OOBState.INITIAL, oob.getState());
        Assertions.assertEquals("did:sov:EraYCDJUPsChbkw7S1vV96", oob.asStringType().getServices().get(0));
    }

    private static final String OOB_MESSAGE = "{\n" +
            "    \"invi_msg_id\": \"ddb8c451-bd3f-4441-bd34-b897c2f21f07\",\n" +
            "    \"connection_id\": \"85d8d882-ba7d-440c-9ede-10a48acc1bc6\",\n" +
            "    \"invitation\": {\n" +
            "        \"@type\": \"https://didcomm.org/out-of-band/1.0/invitation\",\n" +
            "        \"@id\": \"ddb8c451-bd3f-4441-bd34-b897c2f21f07\",\n" +
            "        \"handshake_protocols\": [\n" +
            "            \"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/didexchange/1.0\",\n" +
            "            \"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0\"\n" +
            "        ],\n" +
            "        \"label\": \"bob\",\n" +
            "        \"services\": [\n" +
            "            \"did:sov:EraYCDJUPsChbkw7S1vV96\"\n" +
            "        ]\n" +
            "    },\n" +
            "    \"role\": \"receiver\",\n" +
            "    \"created_at\": \"2022-08-11T12:51:45.665135Z\",\n" +
            "    \"state\": \"initial\",\n" +
            "    \"updated_at\": \"2022-08-11T12:51:45.665135Z\",\n" +
            "    \"oob_id\": \"bb1b9906-f1b4-4c92-96a6-bda81d6282a8\",\n" +
            "    \"trace\": false\n" +
            "}";
}
