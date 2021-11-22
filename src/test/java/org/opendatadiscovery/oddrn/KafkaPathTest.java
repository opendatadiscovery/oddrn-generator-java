package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.KafkaPath;

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
    public void shouldFailTablePath() throws Exception {
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
