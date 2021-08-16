/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.NonNull;
import org.hyperledger.acy_py.generated.model.V20CredExRecord;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.config.GsonConfig;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class V2ToV1IndyCredentialConverter {

    private final Gson gson = GsonConfig.defaultConfig();

    public Optional<Credential> toV1(@NonNull V20CredExRecord v2Record) {
        Optional<Object> credential = getCredential(v2Record);
        if (credential.isPresent()) {
            JsonElement credIssue = gson.toJsonTree(credential.get());
            JsonElement typeIndy = credIssue.getAsJsonObject().get("indy");
            if (typeIndy != null) {
                JsonElement values = typeIndy.getAsJsonObject().get("values");
                if (values != null) {
                    Set<Map.Entry<String, JsonElement>> entries = values.getAsJsonObject().entrySet();
                    Map<String, String> raw = entries
                            .stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    v -> getRawValue(v.getValue())));
                    Credential v1Credential = gson.fromJson(typeIndy, Credential.class);
                    v1Credential.setAttrs(raw);
                    return Optional.of(v1Credential);
                }
            }
        }
        return Optional.empty();
    }

    public static V2ToV1IndyCredentialConverter INSTANCE() {
        return new V2ToV1IndyCredentialConverter();
    }

    private Optional<Object> getCredential(@NonNull V20CredExRecord indy) {
        if (indy.getByFormat() != null && indy.getByFormat().getCredIssue() != null) {
            return Optional.of(indy.getByFormat().getCredIssue());
        }
        else return Optional.empty();
    }

    private String getRawValue(@NonNull JsonElement el) {
        JsonElement raw = el.getAsJsonObject().get("raw");
        if (raw != null) {
            return raw.getAsString();
        }
        return "";
    }
}
