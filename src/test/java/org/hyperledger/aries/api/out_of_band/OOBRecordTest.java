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
    void testSerialiseFromEventString() {
        OOBRecord oob = GsonConfig.defaultConfig().fromJson(OOB_MESSAGE_STRING, OOBRecord.class);
        Assertions.assertEquals(OOBRecord.OOBRole.RECEIVER, oob.getRole());
        Assertions.assertEquals(OOBRecord.OOBState.INITIAL, oob.getState());
        Assertions.assertEquals("did:sov:EraYCDJUPsChbkw7S1vV96", oob.asStringType().getServices().get(0));
    }

    @Test
    void testSerialiseFromEventRFC0067() {
        OOBRecord oob = GsonConfig.defaultConfig().fromJson(OOB_MESSAGE_RFC0067, OOBRecord.class);
        Assertions.assertEquals(OOBRecord.OOBState.DONE, oob.getState());
        Assertions.assertEquals("https://test.something", oob.asRFC0067Type().getServices()
                .get(0).getServiceEndpoint());
    }

    private static final String OOB_MESSAGE_STRING = "{\n" +
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
            "    \"our_recipient_key\": \"8gdhRLtvJHzKoJGyuEqgdN1QZGYfai4wMHFGgtfDXg3D\",\n" +
            "    \"trace\": false\n" +
            "}";

    private static final String OOB_MESSAGE_RFC0067= "{\n" +
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
            "            {\n" +
            "                \"id\": \"#inline\",\n" +
            "                \"type\": \"did-communication\",\n" +
            "                \"recipientKeys\": [\n" +
            "                    \"did:key:z6MkpjQnfym7n2DcWrVzefJxpMdMz1nTBWCQSv4AGrMpdgcK\"\n" +
            "                ],\n" +
            "                \"serviceEndpoint\": \"https://test.something\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"role\": \"receiver\",\n" +
            "    \"created_at\": \"2022-08-11T12:51:45.665135Z\",\n" +
            "    \"state\": \"done\",\n" +
            "    \"updated_at\": \"2022-08-11T12:51:45.665135Z\",\n" +
            "    \"oob_id\": \"bb1b9906-f1b4-4c92-96a6-bda81d6282a8\",\n" +
            "    \"trace\": false\n" +
            "}";
}
