package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class HdfsPath implements OddrnPath {
    @PathField
    private final String site;

    @PathField(dependency = "site", prefix = "paths")
    private final String path;

    @Override
    public String prefix() {
        return "//hdfs";
    }
}
