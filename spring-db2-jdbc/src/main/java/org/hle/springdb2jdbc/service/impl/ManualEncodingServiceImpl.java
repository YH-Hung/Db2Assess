package org.hle.springdb2jdbc.service.impl;

import org.hle.springdb2jdbc.config.EncodingConfigProp;
import org.hle.springdb2jdbc.model.DbCompatibleString;
import org.hle.springdb2jdbc.model.RestoreFromDbCompatible;
import org.hle.springdb2jdbc.service.ManualEncodingService;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
public class ManualEncodingServiceImpl implements ManualEncodingService {

    private final Charset dbCodePage;
    private final Charset valueEncoding;

    public ManualEncodingServiceImpl(EncodingConfigProp encodingConfigProp) {
        this.dbCodePage = Charset.forName(encodingConfigProp.getDatabaseCodePage());
        this.valueEncoding = Charset.forName(encodingConfigProp.getValueEncoding());
    }

    @Override
    public DbCompatibleString makeDbCompatible(String originalStr) {
        return DbCompatibleString.of(originalStr, dbCodePage, valueEncoding);
    }

    @Override
    public RestoreFromDbCompatible restoreFromDbCompatible(DbCompatibleString dbCompatibleString) {
        return RestoreFromDbCompatible.of(dbCompatibleString, dbCodePage, valueEncoding);
    }
}
