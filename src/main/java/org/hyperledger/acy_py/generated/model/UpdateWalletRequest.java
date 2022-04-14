/*
 * aca-py client
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0.7.3
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.hyperledger.acy_py.generated.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

/**
 * UpdateWalletRequest
 */

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class UpdateWalletRequest {
    public static final String SERIALIZED_NAME_IMAGE_URL = "image_url";
    @SerializedName(SERIALIZED_NAME_IMAGE_URL)
    private String imageUrl;
    public static final String SERIALIZED_NAME_LABEL = "label";
    @SerializedName(SERIALIZED_NAME_LABEL)
    private String label;

    /**
     * Webhook target dispatch type for this wallet. default - Dispatch only to webhooks associated with this wallet.
     * base - Dispatch only to webhooks associated with the base wallet. both - Dispatch to both webhook targets.
     */
    @JsonAdapter(WalletDispatchTypeEnum.Adapter.class)
    public enum WalletDispatchTypeEnum {
        DEFAULT("default"),

        BOTH("both"),

        BASE("base");

        private String value;

        WalletDispatchTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static WalletDispatchTypeEnum fromValue(String value) {
            for (WalletDispatchTypeEnum b : WalletDispatchTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public static class Adapter extends TypeAdapter<WalletDispatchTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final WalletDispatchTypeEnum enumeration)
                    throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public WalletDispatchTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return WalletDispatchTypeEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_WALLET_DISPATCH_TYPE = "wallet_dispatch_type";
    @SerializedName(SERIALIZED_NAME_WALLET_DISPATCH_TYPE)
    private WalletDispatchTypeEnum walletDispatchType;
    public static final String SERIALIZED_NAME_WALLET_WEBHOOK_URLS = "wallet_webhook_urls";
    @SerializedName(SERIALIZED_NAME_WALLET_WEBHOOK_URLS)
    private List<String> walletWebhookUrls = null;
}
