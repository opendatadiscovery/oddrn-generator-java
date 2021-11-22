package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.KafkaConnectorPath;

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
    public void shouldFailTablePath() throws Exception {
        shouldFail(
            KafkaConnectorPath.builder()
                .connector("sink")
                .build(),
            "connector",
            EmptyPathValueException.class
        );
    }
}
