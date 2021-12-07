package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class ODDPlatformDataSourcePath implements OddrnPath {
    @PathField(prefix = "datasources")
    private final Long datasourceId;

    @Override
    public String prefix() {
        return "//oddplatform";
    }
}
