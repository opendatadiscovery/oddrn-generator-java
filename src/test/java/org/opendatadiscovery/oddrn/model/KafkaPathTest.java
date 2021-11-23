package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class KafkaPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            KafkaPath.builder()
                .host("1.1.1.1")
                .topic("topic-test")
                .build(),
            "topic",
            "//kafka/host/1.1.1.1/topics/topic-test"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            KafkaPath.builder()
                .host("1.1.1.1")
                .topic("topic-test")
                .column("topic-column")
                .build(),
            "column",
            "//kafka/host/1.1.1.1/topics/topic-test/columns/topic-column"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            KafkaPath.builder()
                .host("1.1.1.1")
                .column("topic-column")
                .build(),
            "column",
            EmptyPathValueException.class
        );
    }
}
