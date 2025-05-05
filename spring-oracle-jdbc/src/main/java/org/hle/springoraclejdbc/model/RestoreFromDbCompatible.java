package org.hle.springoraclejdbc.model;

import java.nio.charset.Charset;

public record RestoreFromDbCompatible(String get) {

    public static RestoreFromDbCompatible of(DbCompatibleString dbCompatibleString, Charset dbCodePage, Charset valueEncoding) {
        return new RestoreFromDbCompatible(new String(dbCompatibleString.get().getBytes(dbCodePage), valueEncoding));
    }

    public static RestoreFromDbCompatible of(byte[] queryResultFromDb, Charset valueEncoding) {
        return new RestoreFromDbCompatible(new String(queryResultFromDb, valueEncoding));
    }
}
