# Spring DB2 JDBC Project Guidelines

## Project Overview
This Spring Boot application demonstrates handling character encoding issues between a Java application (UTF-8) and a DB2 database (IBM-850/Big5). It provides a REST API for managing "Girl" entities with proper encoding/decoding of Chinese characters.

## Tech Stack
- Java 21
- Spring Boot 3.4.5
- Spring JDBC (JdbcTemplate)
- IBM DB2 Database
- Maven
- Lombok
- TestContainers for testing

## Project Structure
```
src/
├── main/
│   ├── java/org/hle/springdb2jdbc/
│   │   ├── config/             # Configuration classes
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── model/              # Domain models
│   │   ├── repository/         # Data access layer
│   │   │   └── impl/           # Repository implementations
│   │   ├── service/            # Business logic layer
│   │   │   └── impl/           # Service implementations
│   │   └── SpringDb2JdbcApplication.java  # Main application class
│   └── resources/
│       └── application.properties  # Application configuration
└── test/
    ├── java/org/hle/springdb2jdbc/
    │   ├── controller/         # Controller tests
    │   └── repository/         # Repository tests
    └── resources/
        ├── application.properties  # Test configuration
        ├── application-utf8.properties  # UTF-8 test configuration
        └── mm_init.sql         # Database initialization script
```

## Key Components
1. **GirlController**: REST API endpoints for managing Girl entities
2. **GirlRepository**: Interface for data operations
3. **GirlRepositoryImpl**: Implementation using JdbcTemplate with encoding handling
4. **ManualEncodingService**: Handles character encoding conversion between application and DB2
5. **EncodingConfigProp**: Configuration for encoding settings

## Environment Setup
The application requires the following environment variables:
- `DB2_URL`: JDBC URL for the DB2 database
- `ENCODING_ADAPTER_DB_CODE_PAGE`: Database code page (e.g., IBM-850)
- `ENCODING_ADAPTER_VALUE_ENCODING`: Application encoding (e.g., Big5)

## Running Tests
Tests use TestContainers to spin up a DB2 container, so Docker must be running on the development machine.

To run tests:
```bash
mvn test
```

The tests verify that:
1. Chinese characters can be properly stored in and retrieved from the DB2 database
2. Encoding conversion works correctly between UTF-8 and Big5/IBM-850

## Best Practices
1. **Encoding Handling**: Always use the ManualEncodingService for converting strings between application and database
2. **Testing**: Write tests that verify both functionality and proper encoding
3. **Configuration**: Use environment variables for database connection and encoding settings
4. **Error Handling**: Add proper error handling for encoding/decoding failures

## Common Issues
- Character encoding mismatches between application and database
- DB2 connection issues with TestContainers
- Environment variable configuration
