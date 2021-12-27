package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

import java.util.UUID;

public class AwsS3PathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateBucketPath() throws Exception {
        shouldGeneratePath(
                AwsS3Path.builder()
                        .bucket("my_bucket")
                        .build(),
                "bucket",
                "//s3-aws/bucket/my_bucket"
        );
    }

    @Test
    public void shouldGenerateKeyPath() throws Exception {
        shouldGeneratePath(
                AwsS3Path.builder()
                        .bucket("my_bucket")
                        .key("file1")
                        .build(),
                "key",
                "//s3-aws/bucket/my_bucket/keys/file1"
        );
    }

    @Test
    public void shouldFailKeyPath() {
        shouldFail(
                AwsS3Path.builder()
                        .key(UUID.randomUUID().toString())
                        .build(),
                "key",
                EmptyPathValueException.class
        );
    }
}
