package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class KafkaPath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "topics")
    private final String topic;

    @PathField(dependency = "column", prefix = "topic")
    private final String column;

    @Override
    public String prefix() {
        return "//kafka";
    }
}
