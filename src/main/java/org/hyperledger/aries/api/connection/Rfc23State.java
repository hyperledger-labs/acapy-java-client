/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Requester states:
 * <p>
 *     <ul>
 *         <li>start</li>
 *         <li>invitation-received</li>
 *         <li>request-sent</li>
 *         <li>response-received</li>
 *         <li>abandoned</li>
 *         <li>completed</li>
 *     </ul>
 * <p>
 * Responder states:
 * <p>
 *     <ul>
 *         <li>start</li>
 *         <li>invitation-sent</li>
 *         <li>request-received</li>
 *         <li>response-sent</li>
 *         <li>abandoned</li>
 *         <li>completed</li>
 *     </ul>
 */
public enum Rfc23State {

    @JsonProperty("abandoned")
    @SerializedName("abandoned")
    ABANDONED,

    @JsonProperty("completed")
    @SerializedName("completed")
    COMPLETED,

    @JsonProperty("invitation-received")
    @SerializedName("invitation-received")
    INVITATION_RECEIVED,

    @JsonProperty("invitation-sent")
    @SerializedName("invitation-sent")
    INVITATION_SENT,

    @JsonProperty("request-received")
    @SerializedName("request-received")
    REQUEST_RECEIVED,

    @JsonProperty("request-sent")
    @SerializedName("request-sent")
    REQUEST_SENT,

    @JsonProperty("response-received")
    @SerializedName("response-received")
    RESPONSE_RECEIVED,

    @JsonProperty("response-sent")
    @SerializedName("response-sent")
    RESPONSE_SENT,

    @JsonProperty("start")
    @SerializedName("start")
    START,
}
