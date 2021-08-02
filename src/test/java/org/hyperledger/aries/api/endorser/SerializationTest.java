/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.endorser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson  gson = GsonConfig.defaultConfig();

    @Test
    void testFromGsonToJackson() throws Exception {
        EndorseTransactionRecord trxGson = gson.fromJson(event, EndorseTransactionRecord.class);
        System.out.println(trxGson);

        String jacksonString = mapper.writeValueAsString(trxGson);
        // System.out.println(jacksonString);
        EndorseTransactionRecord trxJackson = mapper.readValue(jacksonString, EndorseTransactionRecord.class);

        Assertions.assertEquals(trxGson, trxJackson);
    }

    private final String event = "{\n" +
            "    \"messages_attach\": [\n" +
            "        {\n" +
            "            \"@id\": \"75b18588-aff9-44d7-8223-6c262b182ecc\",\n" +
            "            \"mime-type\": \"application/json\",\n" +
            "            \"data\": {\n" +
            "                \"json\": \"{\\\"endorser\\\":\\\"EraYCDJUPsChbkw7S1vV96\\\",\\\"identifier\\\":\\\"F6dB7dMVHUQSC64qemnBi7\\\",\\\"operation\\\":{\\\"data\\\":{\\\"attr_names\\\":[\\\"score\\\"],\\\"name\\\":\\\"prefs\\\",\\\"version\\\":\\\"1.0\\\"},\\\"type\\\":\\\"101\\\"},\\\"protocolVersion\\\":2,\\\"reqId\\\":1627907084319634400,\\\"signatures\\\":{\\\"F6dB7dMVHUQSC64qemnBi7\\\":\\\"4cVbH47cUmXKZDCFe5Ux8DhqdUhPGquQ3AZ4gH1CnaawAbjThnxmTW1rUj5bwm14XGmNRAQ1LnyXdAw6oJoKSb6W\\\"}}\"\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"_type\": \"https://didcomm.org/sign-attachment/%VER/signature-request\",\n" +
            "    \"state\": \"request_received\",\n" +
            "    \"connection_id\": \"2c30d2d4-93f8-4416-ab9c-993e757c0cf1\",\n" +
            "    \"signature_response\": [],\n" +
            "    \"timing\": {\n" +
            "        \"expires_time\": \"2021-08-29T05:22:19Z\"\n" +
            "    },\n" +
            "    \"formats\": [\n" +
            "        {\n" +
            "            \"attach_id\": \"75b18588-aff9-44d7-8223-6c262b182ecc\",\n" +
            "            \"format\": \"dif/endorse-transaction/request@v1.0\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"created_at\": \"2021-08-02 12:29:03.531682Z\",\n" +
            "    \"trace\": false,\n" +
            "    \"updated_at\": \"2021-08-02 12:29:03.531682Z\",\n" +
            "    \"endorser_write_txn\": true,\n" +
            "    \"thread_id\": \"73209c1a-187e-40d3-8212-2272bc19d000\",\n" +
            "    \"transaction_id\": \"79478734-6093-4268-9923-6a146c71ecd3\",\n" +
            "    \"signature_request\": [\n" +
            "        {\n" +
            "            \"context\": \"did:sov\",\n" +
            "            \"method\": \"add-signature\",\n" +
            "            \"signature_type\": \"<requested signature type>\",\n" +
            "            \"signer_goal_code\": \"transaction.endorse\",\n" +
            "            \"author_goal_code\": \"transaction.ledger.write\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
