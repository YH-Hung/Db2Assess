package org.hle.springoraclejdbc.repository;

import org.hle.springoraclejdbc.dto.GirlDto;

import java.util.List;

public interface GirlRepository {
    List<GirlDto> findAll();
    void create(String name);
    void update(int id, String name);
}
