package org.hle.springdb2jdbc.controller;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.repository.GirlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Db2Container;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class GirlControllerTest {

    @Container
    static Db2Container db2Container = new Db2Container("ibmcom/db2:11.5.0.0a")
            .acceptLicense()
            .withInitScript("mm_init.sql")
            .withCommand("--codeset=IBM-850 --territory=en");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GirlRepository girlRepository;

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db2Container::getJdbcUrl);
        registry.add("spring.datasource.username", db2Container::getUsername);
        registry.add("spring.datasource.password", db2Container::getPassword);
    }

    @BeforeEach
    void setUp() {
        // Clear the table before each test
        jdbcTemplate.update("DELETE FROM girls");
    }

    @Test
    void testGetAllGirls_EmptyList() throws Exception {
        // Test getting all girls when the table is empty
        mockMvc.perform(get("/api/girls"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAllGirls_WithData() throws Exception {
        // Given
        girlRepository.create("Alice");
        girlRepository.create("Bob");
        girlRepository.create("Charlie");

        // When & Then
        mockMvc.perform(get("/api/girls"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Alice")))
                .andExpect(jsonPath("$[1].name", is("Bob")))
                .andExpect(jsonPath("$[2].name", is("Charlie")));
    }

    @Test
    void testCreateGirl() throws Exception {
        // When
        mockMvc.perform(post("/api/girls")
                .param("name", "Diana")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        // Then
        List<GirlDto> girls = girlRepository.findAll();
        assertThat(girls).hasSize(1);
        assertThat(girls.getFirst().getName()).isEqualTo("Diana");
    }

    @Test
    void testCreateGirl_WithChineseCharacters() throws Exception {
        // Given
        String chineseName = "小美"; // Chinese characters for "Xiao Mei"

        // When
        mockMvc.perform(post("/api/girls")
                .param("name", chineseName)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        // Then
        List<GirlDto> girls = girlRepository.findAll();
        assertThat(girls).hasSize(1);
        assertThat(girls.getFirst().getName()).isEqualTo(chineseName);
    }

    @Test
    void testUpdateGirl() throws Exception {
        // Given
        girlRepository.create("Eve");
        List<GirlDto> girls = girlRepository.findAll();
        int id = girls.getFirst().getId();

        // When
        mockMvc.perform(put("/api/girls/" + id)
                .param("name", "Updated Eve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        // Then
        girls = girlRepository.findAll();
        assertThat(girls).hasSize(1);
        assertThat(girls.getFirst().getName()).isEqualTo("Updated Eve");
    }

    @Test
    void testUpdateGirl_WithChineseCharacters() throws Exception {
        // Given
        girlRepository.create("Frank");
        List<GirlDto> girls = girlRepository.findAll();
        int id = girls.getFirst().getId();
        String chineseName = "小麗"; // Chinese characters for "Xiao Li"

        // When
        mockMvc.perform(put("/api/girls/" + id)
                .param("name", chineseName)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        // Then
        girls = girlRepository.findAll();
        assertThat(girls).hasSize(1);
        assertThat(girls.getFirst().getName()).isEqualTo(chineseName);
    }
}