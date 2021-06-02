/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PresentationRequestBuilderTest {

    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testSingleCredential() {
        PresentationRequestCredentials cred = gson.fromJson(requestCredentialsSingle,
                PresentationRequestCredentials.class);
        PresentationExchangeRecord ex = gson.fromJson(presExSingle, PresentationExchangeRecord.class);

        Optional<PresentationRequest> presentationRequest = PresentationRequestBuilder.acceptAll(ex, List.of(cred));

        Assertions.assertTrue(presentationRequest.isPresent());

        Assertions.assertEquals(1, presentationRequest.get().getRequestedAttributes().size());
        Assertions.assertNotNull(presentationRequest.get().getRequestedAttributes().get("attribute_group_0"));
        Assertions.assertEquals("ef25ac99-372e-4076-af47-19ce6cee4579",
                presentationRequest.get().getRequestedAttributes().get("attribute_group_0").getCredId());

        // System.out.println(GsonConfig.prettyPrinter().toJson(presentationRequest.get()));
    }

    @Test
    void testMultipleCredentials() {
        List<PresentationRequestCredentials> cred = gson.fromJson(requestCredentialsMulti,
                new TypeToken<Collection<PresentationRequestCredentials>>() {
                }.getType());
        PresentationExchangeRecord ex = gson.fromJson(presExMulti, PresentationExchangeRecord.class);

        Optional<PresentationRequest> presentationRequest = PresentationRequestBuilder.acceptAll(ex, cred);

        Assertions.assertTrue(presentationRequest.isPresent());
        Assertions.assertEquals(2, presentationRequest.get().getRequestedAttributes().size());

        Assertions.assertNotNull(presentationRequest.get().getRequestedAttributes().get("bank_account"));
        Assertions.assertEquals("ef25ac99-372e-4076-af47-19ce6cee4579",
                presentationRequest.get().getRequestedAttributes().get("bank_account").getCredId());

        Assertions.assertNotNull(presentationRequest.get().getRequestedAttributes().get("masterId"));
        Assertions.assertEquals("6348003e-e7c4-4ace-a9ce-f65fabf4d810",
                presentationRequest.get().getRequestedAttributes().get("masterId").getCredId());

        // System.out.println(GsonConfig.prettyPrinter().toJson(presentationRequest.get()));
    }

    @Test
    void testNoMatchingCredentialFound() {
        PresentationExchangeRecord ex = gson.fromJson(presExMulti, PresentationExchangeRecord.class);
        Optional<PresentationRequest> presentationRequest = PresentationRequestBuilder.acceptAll(ex, List.of());
        Assertions.assertFalse(presentationRequest.isPresent());
    }

    @Test
    void testWithPredicates() {
        List<PresentationRequestCredentials> cred = gson.fromJson(requestCredentialsPredicates,
                new TypeToken<Collection<PresentationRequestCredentials>>() {
                }.getType());
        PresentationExchangeRecord ex = gson.fromJson(presExPredicates, PresentationExchangeRecord.class);

        Optional<PresentationRequest> presentationRequest = PresentationRequestBuilder.acceptAll(ex, cred);

        Assertions.assertTrue(presentationRequest.isPresent());

        Assertions.assertEquals(1, presentationRequest.get().getRequestedAttributes().size());
        Assertions.assertNotNull(presentationRequest.get().getRequestedAttributes().get("bank_account"));
        Assertions.assertEquals("ef25ac99-372e-4076-af47-19ce6cee4579",
                presentationRequest.get().getRequestedAttributes().get("bank_account").getCredId());

        Assertions.assertEquals(1, presentationRequest.get().getRequestedPredicates().size());
        Assertions.assertEquals("75df600d-b2da-408e-8b4c-25df81dee9f5",
                presentationRequest.get().getRequestedPredicates().get("construct_partner").getCredId());

        // System.out.println(GsonConfig.prettyPrinter().toJson(presentationRequest.get()));
    }

    private final String requestCredentialsSingle = "  {\n" +
            "    \"cred_info\": {\n" +
            "      \"referent\": \"ef25ac99-372e-4076-af47-19ce6cee4579\",\n" +
            "      \"attrs\": {\n" +
            "        \"iban\": \"1234\",\n" +
            "        \"bic\": \"4321\"\n" +
            "      },\n" +
            "      \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\",\n" +
            "      \"cred_def_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:3:CL:571:Bank Account V2\",\n" +
            "      \"rev_reg_id\": null,\n" +
            "      \"cred_rev_id\": null\n" +
            "    },\n" +
            "    \"interval\": null,\n" +
            "    \"presentation_referents\": [\n" +
            "      \"attribute_group_0\"\n" +
            "    ]\n" +
            "  }";

    private final String presExSingle = "    {\n" +
            "      \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "          \"attribute_group_0\": {\n" +
            "            \"names\": [\n" +
            "              \"bic\",\n" +
            "              \"iban\"\n" +
            "            ],\n" +
            "            \"restrictions\": [\n" +
            "              {\n" +
            "                \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"requested_predicates\": {},\n" +
            "        \"nonce\": \"1046094633050809199221859\"\n" +
            "      },\n" +
            "      \"updated_at\": \"2021-05-31 15:09:24.971834Z\",\n" +
            "      \"trace\": false,\n" +
            "      \"presentation_exchange_id\": \"9bdba6ce-115e-45ab-8b10-7ef1ab1003ed\",\n" +
            "      \"state\": \"request_received\",\n" +
            "      \"thread_id\": \"689e2ab1-4168-446a-902f-81ceaec16412\"\n" +
            "    }";

    private final String requestCredentialsMulti = "[\n" +
            "  {\n" +
            "    \"cred_info\": {\n" +
            "      \"referent\": \"6348003e-e7c4-4ace-a9ce-f65fabf4d810\",\n" +
            "      \"attrs\": {\n" +
            "        \"dateOfBirth\": \"mmm\",\n" +
            "        \"dateOfExpiry\": \"lmlmmlM\",\n" +
            "        \"nationality\": \"mmm\",\n" +
            "        \"academicTitle\": \"mmm\",\n" +
            "        \"addressZipCode\": \"kklln\",\n" +
            "        \"documentType\": \"mmmm\",\n" +
            "        \"familyName\": \"mmm\",\n" +
            "        \"addressCity\": \"mm\",\n" +
            "        \"birthName\": \"mmm\",\n" +
            "        \"addressStreet\": \"mmm\",\n" +
            "        \"placeOfBirth\": \"mmm\",\n" +
            "        \"firstName\": \"mmm\",\n" +
            "        \"addressCountry\": \"mmllm\"\n" +
            "      },\n" +
            "      \"schema_id\": \"847fVkFJiNZ4FUew9g6Zn4:2:Basis-ID:1.0\",\n" +
            "      \"cred_def_id\": \"VoSfM3eGaPxduty34ySygw:3:CL:2899:Basis-ID-Alice\",\n" +
            "      \"rev_reg_id\": null,\n" +
            "      \"cred_rev_id\": null\n" +
            "    },\n" +
            "    \"interval\": null,\n" +
            "    \"presentation_referents\": [\n" +
            "      \"masterId\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"cred_info\": {\n" +
            "      \"referent\": \"ef25ac99-372e-4076-af47-19ce6cee4579\",\n" +
            "      \"attrs\": {\n" +
            "        \"bic\": \"4321\",\n" +
            "        \"iban\": \"1234\"\n" +
            "      },\n" +
            "      \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\",\n" +
            "      \"cred_def_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:3:CL:571:Bank Account V2\",\n" +
            "      \"rev_reg_id\": null,\n" +
            "      \"cred_rev_id\": null\n" +
            "    },\n" +
            "    \"interval\": null,\n" +
            "    \"presentation_referents\": [\n" +
            "      \"bank_account\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    private final String presExMulti = "{\n" +
            "    \"connection_id\": \"8bc1ccef-7ae3-40e9-90bf-12d3228856d3\",\n" +
            "    \"thread_id\": \"c1695bbc-7fed-4e49-b848-67f56f31b6c5\",\n" +
            "    \"role\": \"prover\",\n" +
            "    \"presentation_exchange_id\": \"2b72e261-b7c1-4ae9-832a-991aa10d9c3f\",\n" +
            "    \"state\": \"request_received\",\n" +
            "    \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "            \"bank_account\": {\n" +
            "                \"names\": [\n" +
            "                    \"iban\"\n" +
            "                ],\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            \"masterId\": {\n" +
            "                \"names\": [\n" +
            "                    \"firstName\"\n" +
            "                ],\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"847fVkFJiNZ4FUew9g6Zn4:2:Basis-ID:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {},\n" +
            "        \"nonce\": \"557987630597052052548096\"\n" +
            "    }\n" +
            "}";

    private final String requestCredentialsPredicates = "[\n" +
            "  {\n" +
            "    \"cred_info\": {\n" +
            "      \"referent\": \"ef25ac99-372e-4076-af47-19ce6cee4579\",\n" +
            "      \"attrs\": {\n" +
            "        \"bic\": \"4321\",\n" +
            "        \"iban\": \"1234\"\n" +
            "      },\n" +
            "      \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\",\n" +
            "      \"cred_def_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:3:CL:571:Bank Account V2\",\n" +
            "      \"rev_reg_id\": null,\n" +
            "      \"cred_rev_id\": null\n" +
            "    },\n" +
            "    \"interval\": null,\n" +
            "    \"presentation_referents\": [\n" +
            "      \"bank_account\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"cred_info\": {\n" +
            "      \"referent\": \"75df600d-b2da-408e-8b4c-25df81dee9f5\",\n" +
            "      \"attrs\": {\n" +
            "        \"validtill\": \"01.01.2022\",\n" +
            "        \"name\": \"Phil\",\n" +
            "        \"sub\": \"Sub\",\n" +
            "        \"registration_nr\": \"Reg\",\n" +
            "        \"address\": \"Street\",\n" +
            "        \"company\": \"Corp\",\n" +
            "        \"phone\": \"20\",\n" +
            "        \"email\": \"foo@bar.com\"\n" +
            "      },\n" +
            "      \"schema_id\": \"HET4uocEgd9e1jbu7vhmqE:2:ConstructPartner:1.3\",\n" +
            "      \"cred_def_id\": \"Ni2hE7fEHJ25xUBc7ZESf6:3:CL:1726:Partners\",\n" +
            "      \"rev_reg_id\": \"Ni2hE7fEHJ25xUBc7ZESf6:4:Ni2hE7fEHJ25xUBc7ZESf6:3:CL:1726:Partners:CL_ACCUM:e3bd48ad-a546-436b-ba28-cd4ac2c9afce\",\n" +
            "      \"cred_rev_id\": \"2\"\n" +
            "    },\n" +
            "    \"interval\": null,\n" +
            "    \"presentation_referents\": [\n" +
            "      \"construct_partner\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    private final String presExPredicates = "{\n" +
            "    \"thread_id\": \"673e93b5-eb38-42cf-a1e7-7861563ef7c6\",\n" +
            "    \"created_at\": \"2021-06-02 12:41:32.643524Z\",\n" +
            "    \"initiator\": \"external\",\n" +
            "    \"trace\": false,\n" +
            "    \"updated_at\": \"2021-06-02 12:41:32.643524Z\",\n" +
            "    \"connection_id\": \"8bc1ccef-7ae3-40e9-90bf-12d3228856d3\",\n" +
            "    \"role\": \"prover\",\n" +
            "    \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "            \"bank_account\": {\n" +
            "                \"name\": \"iban\",\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {\n" +
            "            \"construct_partner\": {\n" +
            "                \"name\": \"phone\",\n" +
            "                \"p_type\": \">=\",\n" +
            "                \"p_value\": 20\n" +
            "            }\n" +
            "        },\n" +
            "        \"nonce\": \"557987630597052052548096\"\n" +
            "    },\n" +
            "    \"presentation_exchange_id\": \"e6e808b3-327e-4b1f-9917-41ece901a3c3\",\n" +
            "    \"state\": \"request_received\"\n" +
            "}";
}
