package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class NamedMicroservicePath implements OddrnPath {
    @PathField
    private final String name;

    @Override
    public String prefix() {
        return "//microservice/named";
    }

}
