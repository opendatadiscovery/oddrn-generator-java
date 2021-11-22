package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;

public class PostgreSqlTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "database",
            "//postgresql/host/1.1.1.1/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .schema("public")
                .table("test")
                .tableColumn("id")
                .build(),
            "tableColumn",
            "//postgresql/host/1.1.1.1/databases/dbname/schemas/public/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() throws Exception {
        shouldFail(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("test")
                .build(),
            "table",
            EmptyPathValueException.class
        );
    }
}