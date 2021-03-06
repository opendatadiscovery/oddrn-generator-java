package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class CustomS3Path implements OddrnPath {
    @PathField
    private final String endpoint;

    @PathField(dependency = "endpoint", prefix = "buckets")
    private final String bucket;

    @PathField(dependency = "bucket", prefix = "keys")
    private final String key;

    @Override
    public String prefix() {
        return "//s3-custom";
    }
}
