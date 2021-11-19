package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class KafkaConnectorPath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "connectors")
    private final String connector;

    @Override
    public String prefix() {
        return "//kafkaconnect";
    }
}
