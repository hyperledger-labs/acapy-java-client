package org.hyperledger.aries.api.multitenancy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum WalletKeyDerivation {

    @JsonProperty("ARGON2I_MOD")
    @SerializedName("ARGON2I_MOD")
    ARGON2I_MOD,

    @JsonProperty("ARGON2I_INT")
    @SerializedName("ARGON2I_INT")
    ARGON2I_INT,

    @JsonProperty("RAW")
    @SerializedName("RAW")
    RAW;
}
