package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class AwsS3Path implements OddrnPath {
    @PathField(prefix = "buckets")
    private final String bucket;

    @PathField(dependency = "bucket", prefix = "keys")
    private final String key;

    @Override
    public String prefix() {
        return "//s3/cloud/aws";
    }
}
