package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class HttpServicePath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host")
    private final String method;

    @PathField(dependency = "method")
    private final String path;

    @Override
    public String prefix() {
        return "//http";
    }
}
