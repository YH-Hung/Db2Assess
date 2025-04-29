package org.hle.springdb2jdbc.repository.impl;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.repository.GirlRepository;
import org.hle.springdb2jdbc.service.ManualEncodingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GirlRepositoryImpl implements GirlRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final ManualEncodingService manualEncodingService;
    
    private final RowMapper<GirlDto> girlRowMapper;

    public GirlRepositoryImpl(JdbcTemplate jdbcTemplate, ManualEncodingService manualEncodingService) {
        this.jdbcTemplate = jdbcTemplate;
        this.manualEncodingService = manualEncodingService;
        this.girlRowMapper = GirlRowMapperFactory.create(manualEncodingService);
    }

    @Override
    public List<GirlDto> findAll() {
        String sql = "SELECT girl_id, girl_name FROM girls";
        return jdbcTemplate.query(sql, girlRowMapper);
    }

    @Override
    public void create(String name) {
        String sql = "INSERT INTO girls (girl_name) VALUES (?)";
        jdbcTemplate.update(sql, manualEncodeParam(name));
    }

    @Override
    public void update(int id, String name) {
        String sql = "UPDATE girls SET girl_name = ? WHERE girl_id = ?";
        jdbcTemplate.update(sql, manualEncodeParam(name), id);
    }

    private String manualEncodeParam(String param) {
        return manualEncodingService.makeDbCompatible(param).get();
    }
}