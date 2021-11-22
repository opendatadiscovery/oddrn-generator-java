package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.SnowflakePath;

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
                .tableColumn("id")
                .build(),
            "tableColumn",
            "//snowflake/warehouses/wh/databases/dbname/schemas/test/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() throws Exception {
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
