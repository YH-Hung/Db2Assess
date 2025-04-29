package org.hle.springdb2jdbc.repository;

import org.hle.springdb2jdbc.dto.GirlDto;

import java.util.List;

public interface GirlRepository {
    List<GirlDto> findAll();
    void create(String name);
    void update(int id, String name);
}
