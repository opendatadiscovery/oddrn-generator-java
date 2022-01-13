package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

import java.util.UUID;

public class HdfsPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateBucketPath() throws Exception {
        shouldGeneratePath(
                HdfsPath.builder()
                        .site("sandbox.com:8020")
                        .path("path/to")
                        .build(),
                "path",
                "//hdfs/site/sandbox.com:8020/paths/path\\\\to"
        );
    }

    @Test
    public void shouldGenerateNamePath() throws Exception {
        shouldGeneratePath(
                HdfsPath.builder()
                        .site("sandbox.com:8020")
                        .path("path/to")
                        .name("file1.ext")
                        .build(),
                "name",
                "//hdfs/site/sandbox.com:8020/paths/path\\\\to/names/file1.ext"
        );
    }

    @Test
    public void shouldFailKeyPath() {
        shouldFail(
                HdfsPath.builder()
                        .site("sandbox.com:8020")
                        .name(UUID.randomUUID().toString())
                        .build(),
                "name",
                EmptyPathValueException.class
        );
    }
}
