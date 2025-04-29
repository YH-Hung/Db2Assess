package org.hle.springdb2jdbc.model;

import java.nio.charset.Charset;

public record RestoreFromDbCompatible(String get) {

    public static RestoreFromDbCompatible of(DbCompatibleString dbCompatibleString, Charset dbCodePage, Charset valueEncoding) {
        return new RestoreFromDbCompatible(new String(dbCompatibleString.get().getBytes(dbCodePage), valueEncoding));
    }
}
