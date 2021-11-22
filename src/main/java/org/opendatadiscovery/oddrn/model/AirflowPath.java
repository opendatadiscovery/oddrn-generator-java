package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class AirflowPath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "dags")
    private final String dag;

    @PathField(dependency = "dag", prefix = "tasks")
    private final String task;

    @PathField(dependency = "task", prefix = "runs")
    private final String run;

    @Override
    public String prefix() {
        return "//airflow";
    }
}
