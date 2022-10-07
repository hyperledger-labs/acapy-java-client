/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.acy_py.generated.model.CredAttrSpec;
import org.hyperledger.aries.pojo.AttributeName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CredentialAttributes {

    @SerializedName(value = "mime-type")
    private String mimeType;
    private String name;
    private String value;

    public static <T> List<CredentialAttributes> from(@NonNull T instance) {
        List<CredentialAttributes> result = new ArrayList<>();
        Field[] fields = instance.getClass().getDeclaredFields();
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers())) {
                    String fieldName = field.getName();
                    AttributeName a = field.getAnnotation(AttributeName.class);
                    if (a != null && StringUtils.isNotEmpty(a.value())) {
                        fieldName = a.value();
                    }
                    if (a == null || !a.excluded()) {
                        String fieldValue = "";
                        try {
                            field.setAccessible(true);
                            Object fv = field.get(instance);
                            if (fv != null) {
                                fieldValue = fv.toString();
                            }
                        } catch (IllegalAccessException | IllegalArgumentException e) {
                            log.error("Could not get value of field: {}", fieldName, e);
                        }

                        result.add(CredentialAttributes.builder().name(fieldName).value(fieldValue).build());
                    }
                }
            }
            return null; // nothing to return
        });
        return result;
    }

    public static List<CredentialAttributes> from(@NonNull Map<String, Object> values) {
        List<CredentialAttributes> result = new ArrayList<>();
        // TODO check if complex object
        values.forEach( (k,v) -> result.add(CredentialAttributes.builder().name(k).value(v.toString()).build()));
        return result;
    }

    public static List<CredentialAttributes> fromMap(@NonNull Map<String, String> values) {
        List<CredentialAttributes> result = new ArrayList<>();
        values.forEach( (k,v) -> result.add(CredentialAttributes.builder().name(k).value(v).build()));
        return result;
    }

    public static Map<String, String> toMap(@NonNull List<CredentialAttributes> attributes) {
        return attributes.stream()
                .collect(Collectors.toMap(CredentialAttributes::getName, CredentialAttributes::getValue));
    }

    public CredAttrSpec toCredAttrSpec() {
        return CredAttrSpec
                .builder()
                .name(this.name)
                .value(this.value)
                .mimeType(this.mimeType)
                .build();
    }
}
