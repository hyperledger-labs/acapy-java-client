/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.Identifier;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.pojo.PojoProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class EventParser {

    private static final Type IDENTIFIER_TYPE = new TypeToken<Collection<Identifier>>(){}.getType();

    private final Gson gson = GsonConfig.defaultConfig();
    private final Gson pretty = GsonConfig.prettyPrinter();

    public String prettyJson(@NonNull String json) {
        JsonElement el = JsonParser.parseString(json);
        return pretty.toJson(el);
    }

    public <T> Optional<T> parseValueSave(@NonNull String json, @NonNull Class<T> valueType) {
        Optional<T> t = Optional.empty();
        try {
            t = Optional.ofNullable(gson.fromJson(json, valueType));
        } catch (JsonSyntaxException e) {
            log.error("Could not format json body", e);
        }
        return t;
    }

    public Optional<PresentationExchangeRecord> parsePresentProof(String json) {
        Optional<PresentationExchangeRecord> presentation = parseValueSave(json, PresentationExchangeRecord.class);
        if (presentation.isPresent()) {
            JsonElement je = presentation.get()
                    .getPresentation()
                    .getAsJsonObject().get("identifiers")
                    ;
            List<Identifier> identifiers = gson.fromJson(je, IDENTIFIER_TYPE);
            presentation.get().setIdentifiers(identifiers);
        }
        return presentation;
    }

    /**
     * Converts the present_proof.presentation into an instance of the provided class type:
     * @param <T> The class type
     * @param json present_proof.presentation
     * @param type POJO instance
     * @return Instantiated type with all matching properties set
     */
    public static <T> T from(@NonNull String json, @NonNull Class<T> type) {
        T result = PojoProcessor.getInstance(type);

        final Set<Entry<String, JsonElement>> revealedAttrs = getRevealedAttributes(json);
        final Set<Entry<String, JsonElement>> revealedAttrGroups = getRevealedAttrGroups(
                json, PojoProcessor.getAttributeGroupName(type));

        List<Field> fields = PojoProcessor.fields(type);
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            for(Field field: fields) {
                String fieldName = PojoProcessor.fieldName(field);
                String fieldValue = getValueFor(fieldName, revealedAttrs.isEmpty() ? revealedAttrGroups : revealedAttrs);
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

    /**
     * Finds the attribute names in the present_proof.presentation and extracts their corresponding values.
     * @param json present_proof.presentation
     * @param names Set of attribute names
     * @return Map containing the attribute names and their corresponding values
     */
    public static Map<String, Object> from(@NonNull String json, @NonNull Set<String> names) {
        Map<String, Object> result = new HashMap<>();

        final Set<Entry<String, JsonElement>> revealedAttrs = getRevealedAttributes(json);
        final Set<Entry<String, JsonElement>> revealedAttrGroups = getRevealedAttrGroups(json, null);

        names.forEach(name -> {
            String value = getValueFor(name, revealedAttrs.isEmpty() ? revealedAttrGroups : revealedAttrs);
            result.put(name, value);
        });
        return result;
    }

    private static String getValueFor(String name, Set<Entry<String, JsonElement>> revealedAttrs) {
        String result = null;
        for (Entry<String, JsonElement> e : revealedAttrs) {
            String k = e.getKey();
            if (k.equals(name) || k.contains(name + "_uuid") && e.getValue() != null) {
                final JsonElement raw = e.getValue().getAsJsonObject().get("raw");
                if (raw != null) {
                    result = raw.getAsString();
                }
                break;
            }
        }
        return result;
    }

    private static Set<Entry<String, JsonElement>> getRevealedAttributes(String json) {
        return JsonParser
                .parseString(json)
                .getAsJsonObject().get("requested_proof")
                .getAsJsonObject().get("revealed_attrs").getAsJsonObject()
                .entrySet()
                ;
    }

    private static Set<Entry<String, JsonElement>> getRevealedAttrGroups(@NonNull String json, String groupName) {
        Set<Entry<String, JsonElement>> result = new LinkedHashSet<>();
        final JsonElement attr = JsonParser
                .parseString(json)
                .getAsJsonObject().get("requested_proof")
                .getAsJsonObject().get("revealed_attr_groups")
                ;
        if (attr == null) { // not an attr group
            return result;
        }
        JsonObject attrGroup = attr.getAsJsonObject();
        final Set<String> children = attrGroup.keySet();
        if (StringUtils.isNotEmpty(groupName)) {
            extract(result, attrGroup, groupName);
        } else { // brute force
            children.forEach(c -> extract(result, attrGroup, c));
        }
        return result;
    }

    private static void extract(Set<Entry<String, JsonElement>> result, JsonObject attrGroup, String groupName) {
        final Set<Entry<String, JsonElement>> attrs = attrGroup
                .get(groupName).getAsJsonObject().get("values").getAsJsonObject().entrySet();
        attrs.forEach(a -> {
            if(!result.contains(a)) {
                result.add(a);
            }
        });
    }

}
