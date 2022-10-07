/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.pojo;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.api.exception.ParsingException;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class PojoProcessor {

    public static @Nonnull <T> Set<String> fieldNames(@NonNull Class<T> type) {
        return fields(type).stream().map(PojoProcessor::fieldName).collect(Collectors.toSet());
    }

    public static @Nonnull <T> Set<Field> fields(@NonNull Class<T> type) {
        Set<Field> result = new LinkedHashSet<>();
        Field[] fields = type.getDeclaredFields();

        for (Field field : fields) {
            AttributeName an = field.getAnnotation(AttributeName.class);
            if ((an == null || !an.excluded())
                    && Modifier.isPrivate(field.getModifiers())) {
                result.add(field);
            }
        }
        return result;
    }

    public static @Nonnull String fieldName(@NonNull Field field) {
        String fieldName;
        AttributeName an = field.getAnnotation(AttributeName.class);
        if (an != null) {
            fieldName = an.value();
        } else {
            fieldName = field.getName();
        }
        return fieldName;
    }

    public static String getAttributeGroupName(@NonNull Class<?> type) {
        String group = null;
        if (type.isAnnotationPresent(AttributeGroupName.class)) {
            group = type.getDeclaredAnnotation(AttributeGroupName.class).value();
        }
        return group;
    }

    public static boolean hasAttributeGroupName(@NonNull Class<?> type) {
        return type.isAnnotationPresent(AttributeGroupName.class);
    }

    public static @Nonnull <T> T getInstance(@NonNull Class<T> type) {
        return AccessController.doPrivileged((PrivilegedAction<T>) () -> {
            T result;
            try {
                result = type.getConstructor().newInstance();
            } catch (Exception e) {
                String msg = "Class: " + type.getName() + " is missing a public default constructor.";
                log.error(msg, e);
                throw new ParsingException(msg);
            }
            return result;
        });
    }

    public static <T> T setValues(@NonNull Class<T> type, @NonNull Map<String, String> values) {
        T result = PojoProcessor.getInstance(type);

        Set<Field> fields = PojoProcessor.fields(type);
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            for(Field field: fields) {
                String fieldName = PojoProcessor.fieldName(field);
                String fieldValue = values.get(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(result, fieldValue);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    log.error("Could not set value of field: {} to: {}", fieldName, fieldValue, e);
                }
            }
            return null; // nothing to return
        });
        return result;
    }
}
