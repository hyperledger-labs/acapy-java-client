/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api;

import lombok.Builder;
import lombok.Data;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class AcaPyRequestFilterTest {

    private final String url = "http://foo.bar/";
    private final HttpUrl base =  HttpUrl.parse(url);

    @Test
    void testAllNull() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        Dummy d = Dummy.builder().build();
        Assertions.assertEquals(url, d.buildParams(b).toString());
    }

    @Test
    void testFilter() {
        HttpUrl.Builder b = Objects.requireNonNull(base).newBuilder();
        Dummy d = Dummy.builder().testMyData("abc").test(DummyEnum.VALUE1).myBool(Boolean.TRUE).build();
        Assertions.assertEquals(url + "?test_my_data=abc&test=value1&my_bool=true",
                d.buildParams(b).toString());
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
        private Boolean myBool;
    }

    private enum DummyEnum {
        VALUE1
    }
}
