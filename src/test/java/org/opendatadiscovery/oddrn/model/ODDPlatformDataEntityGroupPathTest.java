package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class ODDPlatformDataEntityGroupPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDataEntityGroupPath() throws Exception {
        shouldGeneratePath(
                ODDPlatformDataEntityGroupPath.builder()
                        .type("KAFKA_SERVICE")
                        .id(1L)
                        .build(),
                "id",
                "//oddplatform_deg/type/KAFKA_SERVICE/dataEntityGroupId/1"
        );
    }

    @Test
    public void shouldFailWhenIdIsNotSet() {
        shouldFail(
                ODDPlatformDataEntityGroupPath.builder()
                        .build(),
                "id",
                EmptyPathValueException.class
        );
    }

}
