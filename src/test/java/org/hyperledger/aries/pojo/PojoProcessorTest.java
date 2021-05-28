/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PojoProcessorTest {

    @Test
    void testGetAttributes() {
        List<String> attr = PojoProcessor.fieldNames(ConcreteExample.class);
        assertEquals(2, attr.size());
        assertEquals("one", attr.get(0));
        assertEquals("two", attr.get(1));
    }

    @Test
    void testGetAttributesExclusion() {
        List<String> attr = PojoProcessor.fieldNames(ConcreteExampleWithExclusion.class);
        assertEquals(3, attr.size());
        assertEquals("name", attr.get(0));
        assertEquals("street", attr.get(1));
        assertEquals("myCity", attr.get(2));
    }

    @Test
    void testGetAttributeGroupName() {
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
