package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class DockerMicroservicePath implements OddrnPath {

    @PathField
    private final String image;

    @Override
    public String prefix() {
        return "//microservice/docker";
    }
}
