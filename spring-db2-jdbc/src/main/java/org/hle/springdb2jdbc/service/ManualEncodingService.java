package org.hle.springdb2jdbc.service;

import org.hle.springdb2jdbc.model.DbCompatibleString;
import org.hle.springdb2jdbc.model.RestoreFromDbCompatible;

public interface ManualEncodingService {
    // ready to db: encode with value, decode with db
    DbCompatibleString makeDbCompatible(String originalStr);
    // restore: encode with db, decode with value
    RestoreFromDbCompatible restoreFromDbCompatible(DbCompatibleString dbCompatibleString);
}
