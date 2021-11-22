package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.GrpcServicePath;

public class GrpcServicePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            GrpcServicePath.builder()
                .host("1.1.1.1")
                .service("helloworld")
                .method("call")
                .build(),
            "method",
            "//grpc/host/1.1.1.1/services/helloworld/methods/call"
        );
    }

    @Test
    public void shouldFailTablePath() throws Exception {
        shouldFail(
            GrpcServicePath.builder()
                .host("1.1.1.1")
                .method("call")
                .build(),
            "method",
            EmptyPathValueException.class
        );
    }
}
