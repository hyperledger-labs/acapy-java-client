/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.jsonld;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.jsonld.SignRequest.SignDocument.Options;
import org.hyperledger.aries.api.jsonld.VerifiableCredential.VerifiableIndyCredential;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@Slf4j
class JsonldTest extends IntegrationTestBase {

    private final Gson gson = GsonConfig.defaultConfig();
    private final Gson pretty = GsonConfig.prettyPrinter();
    private final FileLoader loader = FileLoader.newLoader();

    @Test
    void testSignAndVerifyVC() throws Exception {

        // first create a local did
        DID localDid = createLocalDid();

        VerifiableCredential vc = VerifiableCredential.builder().build();

        SignRequest sr = SignRequest.from(
                localDid.getVerkey(),
                vc,
                Options.builder()
                        .proofPurpose("assertionMethod")
                        .verificationMethod(localDid.getDid())
                        .build());

        log.debug("sign request: \n{}", pretty.toJson(sr));

        // sign the structure
        Optional<VerifiableCredential> signed = ac.jsonldSign(sr, VerifiableCredential.class);
        Assertions.assertTrue(signed.isPresent());
        Assertions.assertNotNull(signed.get().getProof());

        log.debug("sign response: \n{}", pretty.toJson(signed.get()));

        Assertions.assertEquals("assertionMethod", signed.get().getProof().getProofPurpose());
        Assertions.assertTrue(signed.get().getProof().getJws().startsWith("eyJhbGciOiA"));

        // verify the structure
        final Optional<VerifyResponse> verified = ac.jsonldVerify(localDid.getVerkey(), signed.get());
        Assertions.assertTrue(verified.isPresent());
        Assertions.assertTrue(verified.get().isValid());
    }

    @Test
    void testSignAndVerifyVP() throws Exception {

        // first create a local did
        DID localDid = createLocalDid();

        String json = loader.load("json-ld/verifiablePresentationUnsigned.json");
        VerifiablePresentation<VerifiableIndyCredential> vp = gson.fromJson(
                json, VerifiablePresentation.INDY_CREDENTIAL_TYPE);

        SignRequest sr = SignRequest.from(
                localDid.getVerkey(),
                vp,
                Options.builderWithDefaults()
                        .verificationMethod(localDid.getDid())
                        .build());

        log.debug("sign request: \n{}", pretty.toJson(sr));

        // sign the structure
        Optional<VerifiablePresentation<VerifiableIndyCredential>> signed = ac.jsonldSign(
                sr, VerifiablePresentation.class);
        Assertions.assertTrue(signed.isPresent());
        Assertions.assertNotNull(signed.get().getProof());

        log.debug("sign response: \n{}", pretty.toJson(signed.get()));

        Assertions.assertEquals("authentication", signed.get().getProof().getProofPurpose());
        Assertions.assertTrue(signed.get().getProof().getJws().startsWith("eyJhbGciOiA"));

        // verify the structure
        final Optional<VerifyResponse> verified = ac.jsonldVerify(localDid.getVerkey(), signed.get());
        Assertions.assertTrue(verified.isPresent());
        Assertions.assertTrue(verified.get().isValid());
    }

    @Test
    void testVerifyWrongCredentialType() {
        Assertions.assertThrows(IllegalStateException.class, () -> ac.jsonldVerify("1234", new Object()));
    }

    private DID createLocalDid() throws Exception {
        final Optional<DID> localDid = ac.walletDidCreate(DIDCreate
                .builder()
                .method(DIDCreate.MethodEnum.SOV)
                .build());
        Assertions.assertTrue(localDid.isPresent());
        log.debug("localDid: {}", localDid.get());
        return localDid.get();
    }

}
