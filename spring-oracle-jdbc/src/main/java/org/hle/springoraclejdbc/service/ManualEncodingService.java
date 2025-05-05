package org.hle.springoraclejdbc.service;


import org.hle.springoraclejdbc.model.DbCompatibleString;
import org.hle.springoraclejdbc.model.RestoreFromDbCompatible;

public interface ManualEncodingService {
    // ready to db: encode with value, decode with db
    DbCompatibleString makeDbCompatible(String originalStr);
    // restore: encode with db, decode with value
    RestoreFromDbCompatible restoreFromDbCompatible(byte[] queryResultFromDb);

    RestoreFromDbCompatible restoreFromDbCompatible(DbCompatibleString dbCompatibleString);

}
