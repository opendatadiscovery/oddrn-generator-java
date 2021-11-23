package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class KafkaConnectorPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            KafkaConnectorPath.builder()
                .host("1.1.1.1")
                .connector("sink")
                .build(),
            "connector",
            "//kafkaconnect/host/1.1.1.1/connectors/sink"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            KafkaConnectorPath.builder()
                .connector("sink")
                .build(),
            "connector",
            EmptyPathValueException.class
        );
    }
}
