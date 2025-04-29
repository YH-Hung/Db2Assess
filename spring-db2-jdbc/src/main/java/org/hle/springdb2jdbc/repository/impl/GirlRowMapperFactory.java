package org.hle.springdb2jdbc.repository.impl;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.model.DbCompatibleString;
import org.hle.springdb2jdbc.service.ManualEncodingService;
import org.springframework.jdbc.core.RowMapper;

public class GirlRowMapperFactory {
    public static RowMapper<GirlDto> create(ManualEncodingService manualEncodingService) {
        return (rs, rowNum) -> {
            GirlDto girl = new GirlDto();
            girl.setId(rs.getInt("girl_id"));

            DbCompatibleString nameFromDb = DbCompatibleString.of(rs.getString("girl_name"));
            girl.setName(manualEncodingService.restoreFromDbCompatible(nameFromDb).get());
            return girl;
        };
    }
}
