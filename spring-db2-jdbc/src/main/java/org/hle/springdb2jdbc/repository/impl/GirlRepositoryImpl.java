package org.hle.springdb2jdbc.repository.impl;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.repository.GirlRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GirlRepositoryImpl implements GirlRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<GirlDto> girlRowMapper = (rs, rowNum) -> {
        GirlDto girl = new GirlDto();
        girl.setId(rs.getInt("girl_id"));
        girl.setName(rs.getString("girl_name"));
        return girl;
    };

    public GirlRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GirlDto> findAll() {
        String sql = "SELECT girl_id, girl_name FROM girls";
        return jdbcTemplate.query(sql, girlRowMapper);
    }

    @Override
    public void create(String name) {
        String sql = "INSERT INTO girls (girl_name) VALUES (?)";
        jdbcTemplate.update(sql, name);
    }

    @Override
    public void update(int id, String name) {
        String sql = "UPDATE girls SET girl_name = ? WHERE girl_id = ?";
        jdbcTemplate.update(sql, name, id);
    }
}