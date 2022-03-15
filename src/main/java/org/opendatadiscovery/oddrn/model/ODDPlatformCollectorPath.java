package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class ODDPlatformCollectorPath implements OddrnPath {

    @PathField(prefix = "collectors")
    private final Long collectorId;

    @Override
    public String prefix() {
        return "//oddplatform";
    }
}
