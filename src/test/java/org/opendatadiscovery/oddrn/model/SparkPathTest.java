package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

import java.util.UUID;

public class SparkPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDagPath() throws Exception {
        shouldGeneratePath(
            SparkPath.builder()
                .host("127.0.0.1")
                .job("etl")
                .build(),
            "job",
            "//spark/host/127.0.0.1/jobs/etl"
        );
    }

    @Test
    public void shouldGenerateTaskPath() throws Exception {
        shouldGeneratePath(
            SparkPath.builder()
                .host("127.0.0.1")
                .job("etl")
                .run("affjfkhx2p")
                .build(),
            "run",
            "//spark/host/127.0.0.1/jobs/etl/runs/affjfkhx2p"
        );
    }

    @Test
    public void shouldFailRunPath() {
        shouldFail(
            SparkPath.builder()
                .host("127.0.0.1")
                .run(UUID.randomUUID().toString())
                .build(),
            "run",
            EmptyPathValueException.class
        );
    }
}
