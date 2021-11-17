/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.config;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    private static final DateTimeFormatter ISO_INSTANT_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    public static String currentTimeFormatted() {
        return currentTimeFormatted(Instant.now());
    }

    public static String currentTimeFormatted(@Nullable Instant instant) {
        if (instant == null) {
            return null;
        }
        return ISO_INSTANT_FORMATTER.format(instant.truncatedTo(ChronoUnit.SECONDS));
    }
}
