package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.Gson;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DifPresentationExchangeTest {

    private final FileLoader loader = FileLoader.newLoader();
    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testParsePresentationRequest() {
        String json = loader.load("files/present-proof-v2/verifier-done-dif.json");
        V20PresExRecord v2 = gson.fromJson(json, V20PresExRecord.class);

        Assertions.assertTrue(v2.isDif());
        Assertions.assertTrue(v2.resolveDifPresentationRequest(V2DIFProofRequest.INPUT_URI_TYPE).isPresent());
        Assertions.assertNotNull(v2.resolveDifPresentation());

    }
}
