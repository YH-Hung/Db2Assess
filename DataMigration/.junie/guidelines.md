# DataMigration Project Guidelines

This document provides guidelines and instructions for developing and working with the DataMigration project.

## Build/Configuration Instructions

### Project Setup

1. **Python Version**: This project requires Python 3.12 or higher.

2. **Installation**:
   ```bash
   # Install the package in development mode with dev dependencies
   pip install -e ".[dev]"
   ```

3. **Project Structure**:
   - `main.py`: Contains the core functionality for data migration
   - `tests/`: Directory containing test files
   - `pyproject.toml`: Project configuration and dependencies

## Testing Information

### Running Tests

Tests are written using pytest and can be run with the following command:

```bash
# Run all tests
pytest

# Run tests with verbose output
pytest -v

# Run tests with coverage report
pytest --cov=datamigration
```

### Adding New Tests

1. Create test files in the `tests/` directory with the naming convention `test_*.py`.
2. Use pytest fixtures for common setup and teardown operations.
3. Follow the existing test structure for consistency.

### Example Test

Here's an example of how to write a test for this project:

```python
# tests/test_example.py
import pytest
from main import migrate_data

def test_migrate_data_custom_case():
    """Test migration with a custom case."""
    source = {"type": "oracle", "host": "example.com", "port": 1521}
    target = {"type": "sqlserver", "host": "target.com", "port": 1433}
    
    result = migrate_data(source, target, ["customers", "orders"])
    
    assert result["status"] == "success"
    assert "customers" in result["tables_migrated"]
    assert result["source"] == "oracle"
    assert result["target"] == "sqlserver"
```

## Additional Development Information

### Code Style

- Follow PEP 8 guidelines for Python code style.
- Use docstrings for all functions, classes, and modules.
- Include type hints where appropriate.

### Project Dependencies

- Core dependencies are defined in the `dependencies` section of `pyproject.toml`.
- Development dependencies (pytest, etc.) are in the `project.optional-dependencies.dev` section.

### Error Handling

- Use appropriate exception handling for database operations.
- Log errors and warnings with sufficient context for debugging.

### Performance Considerations

- For large data migrations, consider implementing batch processing.
- Monitor memory usage when dealing with large datasets.

### Future Development

- Consider adding support for more database types.
- Implement progress tracking for long-running migrations.
- Add validation for schema compatibility between source and target databases.