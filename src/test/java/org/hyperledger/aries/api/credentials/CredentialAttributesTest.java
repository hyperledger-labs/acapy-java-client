/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.pojo.AttributeName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialAttributesTest {

    @Test
    void testNoExclusions() {
        List<CredentialAttributes> creds = CredentialAttributes.from(
                new ConcreteCredential("testname", "teststreet", "bar"));
        assertEquals(3, creds.size());
        assertEquals("name", creds.get(0).getName());
        assertEquals("testname", creds.get(0).getValue());
    }

    @Test
    void testWithExclusions() {
        List<CredentialAttributes> creds = CredentialAttributes.from(
                new ConcreteCredentialWithExclusion());
        assertEquals(2, creds.size());
    }

    @Test
    void testWithAttributeName() {
        List<CredentialAttributes> creds = CredentialAttributes.from(
                new ConcreteCredentialWithName("testname", "teststreet", "bar"));
        assertEquals(3, creds.size());
        assertEquals("custom", creds.get(2).getName());
        assertEquals("bar", creds.get(2).getValue());
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ConcreteCredential {
        private String name;
        private String street;
        private String other_foo;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ConcreteCredentialWithExclusion {
        private String name;
        @AttributeName("street")
        private String street;
        @AttributeName(excluded = true)
        private String other;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ConcreteCredentialWithName {
        private String name;
        private String street;
        @AttributeName("custom")
        private String other;
    }
}
