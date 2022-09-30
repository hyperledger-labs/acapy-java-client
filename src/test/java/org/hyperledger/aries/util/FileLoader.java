/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileLoader {

    public static String load(String filename) {
        String result = "";
        String fn;

        if (!filename.contains(".")) {
            fn = filename + ".json";
        } else {
            fn = filename;
        }

        try {
            result = Files.readString(Paths.get("src/test/resources/" + fn));
        } catch (IOException e) {
            log.error("Could not read from input stream.", e);
        }

        return result;
    }
}