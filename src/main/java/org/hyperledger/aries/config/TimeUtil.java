/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.config;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class TimeUtil {

    private static final DateTimeFormatter ISO_INSTANT_FORMATTER = DateTimeFormatter.ISO_INSTANT;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSz");

    public static String currentTimeFormatted() {
        return currentTimeFormatted(Instant.now());
    }

    public static String currentTimeFormatted(@Nullable Instant instant) {
        if (instant == null) {
            return null;
        }
        return ISO_INSTANT_FORMATTER.format(instant.truncatedTo(ChronoUnit.SECONDS));
    }

    /**
     * Converts a timestamp to an Instant
     * @param ts timestamp in the format yyyy-MM-dd HH:mm:ss.SSSSSSz
     * @return {@link Instant}
     */
    public static Optional<Instant> from(String ts) {
        Optional<Instant> result = Optional.empty();
        if (StringUtils.isNotEmpty(ts)) {
            TemporalAccessor ta = FORMATTER.parse(ts);
            result = Optional.of(Instant.from(ta));
        }
        return result;
    }
}
