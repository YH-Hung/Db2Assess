package org.hle.springdb2jdbc.repository;

import org.hle.springdb2jdbc.dto.GirlDto;
import org.hle.springdb2jdbc.repository.impl.GirlRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Db2Container;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@Testcontainers
class GirlRepositoryImplTest {

    @Container
    static Db2Container db2Container = new Db2Container("ibmcom/db2:11.5.0.0a")
            .acceptLicense()
            .withInitScript("mm_init.sql")
            .withCommand("--codeset=IBM-850 --territory=en");

    @Autowired
    private GirlRepositoryImpl girlRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    void testCreateAndFindAll_WithChineseCharacters() {
        // Given
        String chineseName = "小美"; // Chinese characters for "Xiao Mei"

        // When
        girlRepository.create(chineseName);
        List<GirlDto> girls = girlRepository.findAll();

        // Then
        assertThat(girls.size()).isEqualTo(1);
        assertThat(girls.getFirst().getName()).isEqualTo(chineseName);

        // Verify the raw bytes in the database to ensure proper Big5 encoding
        verifyChineseCharactersStoredAsBig5(chineseName, girls.getFirst().getId());
    }

    @Test
    void testUpdate_WithChineseCharacters() {
        // Given
        String originalName = "Alice";
        String chineseName = "小麗"; // Chinese characters for "Xiao Li"

        // When
        girlRepository.create(originalName);
        List<GirlDto> girls = girlRepository.findAll();
        int id = girls.getFirst().getId();

        girlRepository.update(id, chineseName);
        girls = girlRepository.findAll();

        // Then
        assertThat(girls.size()).isEqualTo(1);
        assertThat(girls.getFirst().getName()).isEqualTo(chineseName);

        // Verify the raw bytes in the database to ensure proper Big5 encoding
        verifyChineseCharactersStoredAsBig5(chineseName, id);
    }

    @Test
    void testMultipleGirls_WithChineseCharacters() {
        // Given
        String chineseName1 = "小美"; // Chinese characters for "Xiao Mei"
        String chineseName2 = "小麗"; // Chinese characters for "Xiao Li"
        String chineseName3 = "小芳"; // Chinese characters for "Xiao Fang"

        // When
        girlRepository.create(chineseName1);
        girlRepository.create(chineseName2);
        girlRepository.create(chineseName3);
        List<GirlDto> girls = girlRepository.findAll();

        // Then
        assertThat(girls.size()).isEqualTo(3);

        // Verify each name is correctly stored and retrieved
        boolean foundName1 = false;
        boolean foundName2 = false;
        boolean foundName3 = false;

        for (GirlDto girl : girls) {
            if (chineseName1.equals(girl.getName())) {
                foundName1 = true;
                verifyChineseCharactersStoredAsBig5(chineseName1, girl.getId());
            } else if (chineseName2.equals(girl.getName())) {
                foundName2 = true;
                verifyChineseCharactersStoredAsBig5(chineseName2, girl.getId());
            } else if (chineseName3.equals(girl.getName())) {
                foundName3 = true;
                verifyChineseCharactersStoredAsBig5(chineseName3, girl.getId());
            }
        }

        assertThat(foundName1).as("Could not find girl with name: " + chineseName1).isTrue();
        assertThat(foundName2).as("Could not find girl with name: " + chineseName2).isTrue();
        assertThat(foundName3).as("Could not find girl with name: " + chineseName3).isTrue();
    }

    /**
     * Verifies that Chinese characters are properly stored in the database using Big5 encoding.
     * This method connects directly to the database to check the raw bytes.
     */
    private void verifyChineseCharactersStoredAsBig5(String expectedName, int id) {
        try (Connection conn = db2Container.createConnection("");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT girl_name FROM girls WHERE girl_id = " + id)) {

            assertThat(rs.next()).as("No record found with ID: " + id).isTrue();
            String storedName = rs.getString("girl_name");

            // Convert the stored name to bytes using IBM-850 (the database code page)
            byte[] storedBytes = storedName.getBytes("IBM-850");

            // Convert those bytes back to a string using Big5
            String decodedName = new String(storedBytes, "Big5");

            // The decoded name should match the expected name
            assertThat(decodedName).as("The Chinese characters were not properly stored in Big5 encoding")
                    .isEqualTo(expectedName);

            // Additional verification: the raw bytes should be different from UTF-8 encoding
            // This ensures we're not just storing UTF-8 bytes
            byte[] utf8Bytes = expectedName.getBytes(StandardCharsets.UTF_8);
            assertThat(storedBytes.length).as("The stored bytes match UTF-8 encoding, which suggests Big5 encoding is not being used")
                    .isNotEqualTo(utf8Bytes.length);

            System.out.println("[DEBUG_LOG] Expected name: " + expectedName);
            System.out.println("[DEBUG_LOG] Stored name: " + storedName);
            System.out.println("[DEBUG_LOG] Decoded name: " + decodedName);
            System.out.println("[DEBUG_LOG] Stored bytes length: " + storedBytes.length);
            System.out.println("[DEBUG_LOG] UTF-8 bytes length: " + utf8Bytes.length);
        } catch (SQLException | java.io.UnsupportedEncodingException e) {
            fail("Exception during verification: " + e.getMessage());
        }
    }
}
