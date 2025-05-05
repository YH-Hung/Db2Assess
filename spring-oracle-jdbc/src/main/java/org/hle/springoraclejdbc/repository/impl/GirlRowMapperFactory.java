package org.hle.springoraclejdbc.repository.impl;

import org.hle.springoraclejdbc.dto.GirlDto;
import org.hle.springoraclejdbc.model.DbCompatibleString;
import org.hle.springoraclejdbc.service.ManualEncodingService;
import org.springframework.jdbc.core.RowMapper;

public class GirlRowMapperFactory {
    public static RowMapper<GirlDto> createByByte(ManualEncodingService manualEncodingService) {
        return (rs, rowNum) -> {
            GirlDto girl = new GirlDto();
            girl.setId(rs.getInt("girl_id"));

            girl.setName(manualEncodingService.restoreFromDbCompatible(rs.getBytes("girl_name")).get());
            return girl;
        };
    }

    public static RowMapper<GirlDto> createByString(ManualEncodingService manualEncodingService) {
        return (rs, rowNum) -> {
            GirlDto girl = new GirlDto();
            girl.setId(rs.getInt("girl_id"));

            DbCompatibleString nameFromDb = DbCompatibleString.of(rs.getString("girl_name"));
            girl.setName(manualEncodingService.restoreFromDbCompatible(nameFromDb).get());
            return girl;
        };
    }
}
