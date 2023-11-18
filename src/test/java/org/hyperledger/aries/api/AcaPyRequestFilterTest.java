/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import okhttp3.HttpUrl;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueCredentialRecordsFilter;
import org.hyperledger.aries.api.wallet.ListWalletDidFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class AcaPyRequestFilterTest {

    private final String url = "https://foo.bar/";
    private final HttpUrl base = HttpUrl.parse(url);

    @Test
    void testAllNull() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        Dummy d = Dummy.builder().build();
        Assertions.assertEquals(url, d.buildParams(b).toString());
    }

    @Test
    void testFilter() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        Dummy d = Dummy.builder().testMyData("abc").test(DummyEnum.VALUE1).test2(AnnotatedDummyEnum.VALUE2)
                .myBool(Boolean.TRUE).build();
        Assertions.assertEquals(url + "?test_my_data=abc&test=value1&test2=value_2&my_bool=true",
                d.buildParams(b).toString());
    }

    @Test
    void testV2IssueCredRole() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        V2IssueCredentialRecordsFilter f = V2IssueCredentialRecordsFilter.builder()
                .state(V2IssueCredentialRecordsFilter.V2CredExRecordState.CREDENTIAL_RECEIVED).build();
        Assertions.assertEquals(url + "?state=credential-received", f.buildParams(b).toString());
    }

    @Test
    void testListWalletDid() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        ListWalletDidFilter f = ListWalletDidFilter.builder().keyType(DID.KeyTypeEnum.BLS12381G2).build();
        Assertions.assertEquals(url + "?key_type=bls12381g2", f.buildParams(b).toString());
    }

    @Test
    void testAnnotatedBoolean() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        AnnotatedBoolean ab = AnnotatedBoolean.builder().isPublic(Boolean.TRUE).build();
        Assertions.assertEquals(url + "?public=true", ab.buildParams(b).toString());
    }

    @Test
    void testEmptyString() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        Dummy d = Dummy.builder().testMyData("").build();
        Assertions.assertEquals(url + "?test_my_data=", d.buildParams(b).toString());
    }

    @Data @Builder
    private static final class Dummy implements AcaPyRequestFilter {
        private String testMyData;
        public String publicValue;
        private DummyEnum test;
        private AnnotatedDummyEnum test2;
        private Boolean myBool;
    }

    private enum DummyEnum {
        VALUE1
    }

    private enum AnnotatedDummyEnum {
        @SerializedName("value_2")
        VALUE2
    }

    @Builder
    private static final class AnnotatedBoolean implements AcaPyRequestFilter {
        @SerializedName("public")
        private Boolean isPublic;
    }
}
