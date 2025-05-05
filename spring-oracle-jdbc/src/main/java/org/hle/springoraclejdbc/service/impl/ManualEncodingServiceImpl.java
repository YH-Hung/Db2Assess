package org.hle.springoraclejdbc.service.impl;

import org.hle.springoraclejdbc.config.EncodingConfigProp;
import org.hle.springoraclejdbc.model.DbCompatibleString;
import org.hle.springoraclejdbc.model.RestoreFromDbCompatible;
import org.hle.springoraclejdbc.service.ManualEncodingService;
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
    public RestoreFromDbCompatible restoreFromDbCompatible(byte[] queryResultFromDb) {
        return RestoreFromDbCompatible.of(queryResultFromDb, valueEncoding);
    }

    @Override
    public RestoreFromDbCompatible restoreFromDbCompatible(DbCompatibleString dbCompatibleString) {
        return RestoreFromDbCompatible.of(dbCompatibleString, dbCodePage, valueEncoding);
    }
}
