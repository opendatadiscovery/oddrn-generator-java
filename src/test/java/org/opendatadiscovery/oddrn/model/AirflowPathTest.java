package org.opendatadiscovery.oddrn.model;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class AirflowPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDagPath() throws Exception {
        shouldGeneratePath(
            AirflowPath.builder()
                .host("1.1.1.1")
                .dag("etl")
                .build(),
            "dag",
            "//airflow/host/1.1.1.1/dags/etl"
        );
    }

    @Test
    public void shouldGenerateTaskPath() throws Exception {
        shouldGeneratePath(
            AirflowPath.builder()
                .host("1.1.1.1")
                .dag("etl")
                .task("transform")
                .build(),
            "task",
            "//airflow/host/1.1.1.1/dags/etl/tasks/transform"
        );
    }

    @Test
    public void shouldFailRunPath() {
        shouldFail(
            AirflowPath.builder()
                .host("1.1.1.1")
                .dag("etl")
                .run(UUID.randomUUID().toString())
                .build(),
            "run",
            EmptyPathValueException.class
        );
    }
}
