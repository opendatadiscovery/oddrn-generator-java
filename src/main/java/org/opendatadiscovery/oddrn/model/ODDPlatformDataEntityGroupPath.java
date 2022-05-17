package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder(toBuilder = true)
public class ODDPlatformDataEntityGroupPath implements OddrnPath {

    @PathField(prefix = "type")
    private String type;

    @PathField(dependency = "type", prefix = "dataEntityGroupId")
    private final Long id;

    @Override
    public String prefix() {
        return "//oddplatform_deg";
    }
}
