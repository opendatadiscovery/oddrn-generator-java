package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.model.DynamodbPath;

public class DynamodbPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .table("dtable")
                .build(),
            "table",
            "//dynamodb/cloud/aws/account/7771111/region/eu-central-1/tables/dtable"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .table("dtable")
                .column("id")
                .build(),
            "column",
            "//dynamodb/cloud/aws/account/7771111/region/eu-central-1/tables/dtable/columns/id"
        );
    }

    @Test
    public void shouldFailColumnPath() throws Exception {
        shouldFail(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .column("id")
                .build(),
            "column",
            EmptyPathValueException.class
        );
    }
}
