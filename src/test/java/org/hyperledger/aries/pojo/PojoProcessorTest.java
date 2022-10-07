/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PojoProcessorTest {

    @Test
    void testGetAttributes() {
        Set<String> attr = PojoProcessor.fieldNames(ConcreteExample.class);
        assertEquals(2, attr.size());
        assertTrue(attr.contains("one"));
        assertTrue(attr.contains("two"));
    }

    @Test
    void testGetAttributesExclusion() {
        Set<String> attr = PojoProcessor.fieldNames(ConcreteExampleWithExclusion.class);
        assertEquals(3, attr.size());
        assertTrue(attr.contains("name"));
        assertTrue(attr.contains("street"));
        assertTrue(attr.contains("myCity"));
    }

    @Test
    void testGetAttributeGroupName() {
        assertTrue(PojoProcessor.hasAttributeGroupName(ConcreteExample.class));

        String group = PojoProcessor.getAttributeGroupName(ConcreteExample.class);
        assertNotNull(group);
        assertEquals("group_01", group);
    }

    @AttributeGroupName("group_01")
    @Data @NoArgsConstructor @AllArgsConstructor
    static class ConcreteExample {
        public static final String DUMMY = "public dummy field";
        private String one;
        private String two;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ConcreteExampleWithExclusion {
        private String name;
        private String street;
        @AttributeName(excluded = true)
        private String other;
        @AttributeName(value = "myCity")
        private String city;
    }

}
