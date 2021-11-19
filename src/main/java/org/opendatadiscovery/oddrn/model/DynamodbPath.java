package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class DynamodbPath implements OddrnPath {
    @PathField
    private final String account;

    @PathField(dependency = "account")
    private final String region;

    @PathField(dependency = "region", prefix = "tables")
    private final String table;

    @PathField(dependency = "table", prefix = "columns")
    private final String column;

    @Override
    public String prefix() {
        return "//dynamodb/cloud/aws";
    }
}
