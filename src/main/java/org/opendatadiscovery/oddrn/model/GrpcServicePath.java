package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class GrpcServicePath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "services")
    private final String service;

    @PathField(dependency = "service", prefix = "methods")
    private final String method;

    @Override
    public String prefix() {
        return "//grpc";
    }
}
