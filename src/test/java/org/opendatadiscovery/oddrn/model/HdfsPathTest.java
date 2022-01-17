package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;

import java.util.UUID;

public class HdfsPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGeneratePath() throws Exception {
        shouldGeneratePath(
                HdfsPath.builder()
                        .site("sandbox.com:8020")
                        .path("path/to/file1.ext")
                        .build(),
                "path",
                "//hdfs/site/sandbox.com:8020/paths/path\\\\to\\\\file1.ext"
        );
    }

    @Test
    public void shouldFailPath() {
        shouldFail(
                HdfsPath.builder()
                        .path(UUID.randomUUID().toString())
                        .build(),
                "name",
                PathDoesntExistException.class
        );
    }
}
