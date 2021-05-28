/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api;

import com.google.gson.FieldNamingPolicy;
import lombok.NonNull;
import okhttp3.HttpUrl;
import org.hyperledger.aries.pojo.PojoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public interface AcaPyRequestFilter {

    Logger log = LoggerFactory.getLogger(AcaPyRequestFilter.class);

    default HttpUrl.Builder buildParams(@NonNull HttpUrl.Builder b) {
        List<Field> fields = PojoProcessor.fields(this.getClass());
        fields.forEach(f -> {
            try {
                f.setAccessible(true);
                Object o = f.get(this);
                if (o != null) {
                    String value = null;
                    if (f.getType().isAssignableFrom(String.class)) {
                        value = (String) o;
                    } else if (f.getType().isEnum()) {
                        value = o.toString().toLowerCase(Locale.US);
                    } else if (f.getType().isAssignableFrom(Boolean.class)) {
                        value = ((Boolean) o).toString().toLowerCase(Locale.US);
                    }
                    if (value != null) {
                        b.addQueryParameter(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(f), value);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Cold not extract value for field: {}", f.getName(), e);
            }
        });
        return b;
    }
}
