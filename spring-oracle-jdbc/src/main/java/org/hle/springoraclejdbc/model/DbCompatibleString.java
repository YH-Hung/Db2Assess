package org.hle.springoraclejdbc.model;

import java.nio.charset.Charset;

public record DbCompatibleString(String get) {

    public static DbCompatibleString of(String originalStr, Charset dbCodePage, Charset valueEncoding) {
        return new DbCompatibleString(new String(originalStr.getBytes(valueEncoding), dbCodePage));
    }

    public static DbCompatibleString of(String queryResultFromDb) {
        return new DbCompatibleString(queryResultFromDb);
    }
}
