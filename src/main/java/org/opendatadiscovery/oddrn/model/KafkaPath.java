package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class KafkaPath implements OddrnPath {
    @PathField
    private final String clusters;

    @PathField(dependency = "host", prefix = "topics")
    private final String topic;

    @PathField(dependency = "topic", prefix = "columns")
    private final String column;

    @Override
    public String prefix() {
        return "//kafka";
    }
}
