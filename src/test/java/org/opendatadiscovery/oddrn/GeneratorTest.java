package org.opendatadiscovery.oddrn;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;
import org.opendatadiscovery.oddrn.exception.WrongPathOrderException;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeneratorTest {

    private final Generator generator = new Generator();

    @Test
    public void shouldGenerateDatabasePath()
            throws PathDoesntExistException, WrongPathOrderException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException {
        final String oddrn = generator.generate(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "database"
        );

        assertEquals("//postgresql/host/1.1.1.1/databases/dbname", oddrn);
    }

    @Test
    public void shouldGenerateColumnPath()
            throws PathDoesntExistException, WrongPathOrderException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException {
        final String oddrn = generator.generate(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .schema("public")
                .table("test")
                .tableColumn("id")
                .build(),
            "tableColumn"
        );

        assertEquals(
            "//postgresql/host/1.1.1.1/databases/dbname/schemas/public/tables/test/columns/id",
            oddrn
        );
    }

    @Test
    public void shouldFailDatabasePath() {
        final Exception exception = assertThrows(EmptyPathValueException.class, () ->
            generator.generate(
                PostgreSqlPath.builder()
                    .host("1.1.1.1")
                    .database("dbname")
                    .table("test")
                    .build(),
                "table"
            )
        );
    }
}