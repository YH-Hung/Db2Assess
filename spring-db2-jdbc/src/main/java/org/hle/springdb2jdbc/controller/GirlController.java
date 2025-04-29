package org.hle.springdb2jdbc.controller;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.repository.GirlRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/girls")
public class GirlController {
    
    private final GirlRepository girlRepository;

    public GirlController(GirlRepository girlRepository) {
        this.girlRepository = girlRepository;
    }

    @GetMapping
    public ResponseEntity<List<GirlDto>> getAllGirls() {
        return ResponseEntity.ok(girlRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createGirl(@RequestParam String name) {
        girlRepository.create(name);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGirl(@PathVariable int id, @RequestParam String name) {
        girlRepository.update(id, name);
        return ResponseEntity.ok().build();
    }
}