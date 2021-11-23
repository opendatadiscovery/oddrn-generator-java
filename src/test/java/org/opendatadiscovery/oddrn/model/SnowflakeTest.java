package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class SnowflakeTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            SnowflakePath.builder()
                .warehouse("wh")
                .database("dbname")
                .build(),
            "database",
            "//snowflake/warehouses/wh/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            SnowflakePath.builder()
                .warehouse("wh")
                .database("dbname")
                .schema("test")
                .table("test")
                .column("id")
                .build(),
            "column",
            "//snowflake/warehouses/wh/databases/dbname/schemas/test/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            SnowflakePath.builder()
                .warehouse("wh")
                .database("dbname")
                .table("test")
                .build(),
            "table",
            EmptyPathValueException.class
        );
    }
}
