/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.pojo.AttributeName;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data @NoArgsConstructor @AllArgsConstructor
public class CredentialAttributes {

    @SerializedName(value = "mime-type")
    private String mimeType;
    private String name;
    private String value;

    public CredentialAttributes(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }
    public static <T> List<CredentialAttributes> from(@NonNull T instance) {
        List<CredentialAttributes> result = new ArrayList<>();
        Field[] fields = instance.getClass().getDeclaredFields();
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            for (Field field : fields) {
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
                    result.add(new CredentialAttributes(fieldName, fieldValue));
                }
            }
            return null; // nothing to return
        });
        return result;
    }

    public static List<CredentialAttributes> from(@NonNull Map<String, Object> values) {
        List<CredentialAttributes> result = new ArrayList<>();
        // TODO check if complex object
        values.forEach( (k,v) -> result.add(new CredentialAttributes(k, v.toString())));
        return result;
    }
}
