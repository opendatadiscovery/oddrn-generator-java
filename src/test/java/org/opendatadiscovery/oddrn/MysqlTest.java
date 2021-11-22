package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.MysqlPath;

public class MysqlTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            MysqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "database",
            "//mysql/host/1.1.1.1/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            MysqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("test")
                .tableColumn("id")
                .build(),
            "tableColumn",
            "//mysql/host/1.1.1.1/databases/dbname/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() throws Exception {
        shouldFail(
            MysqlPath.builder()
                .host("1.1.1.1")
                .table("test")
                .build(),
            "table",
            EmptyPathValueException.class
        );
    }
}
