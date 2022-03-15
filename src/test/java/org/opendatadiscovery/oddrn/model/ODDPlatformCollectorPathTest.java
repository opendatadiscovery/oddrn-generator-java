package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class ODDPlatformCollectorPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateCollectorPath() throws Exception {
        shouldGeneratePath(
                ODDPlatformCollectorPath.builder()
                        .collectorId(1L)
                        .build(),
                "collectorId",
                "//oddplatform/collectors/1"
        );
    }

    @Test
    public void shouldFailWhenCollectorIsNotSet() {
        shouldFail(
                ODDPlatformCollectorPath.builder()
                        .build(),
                "collectorId",
                EmptyPathValueException.class
        );
    }

}
