package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class SparkPath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "jobs")
    private final String job;

    @PathField(dependency = "job", prefix = "runs")
    private final String run;

    @Override
    public String prefix() {
        return "//spark";
    }
}
