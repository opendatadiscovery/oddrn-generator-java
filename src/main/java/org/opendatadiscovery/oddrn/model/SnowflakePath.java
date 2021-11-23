package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class SnowflakePath implements OddrnPath {
    @PathField(prefix = "warehouses")
    private final String warehouse;

    @PathField(dependency = "warehouse", prefix = "databases")
    private final String database;

    @PathField(dependency = "database", prefix = "schemas")
    private final String schema;

    @PathField(dependency = "schema", prefix = "tables")
    private final String table;

    @PathField(dependency = "schema", prefix = "views")
    private final String view;

    @PathField(dependency = {"table", "view"}, prefix = "columns")
    private final String column;

    @PathField(dependency = "database", prefix = "owners")
    private final String owner;

    @Override
    public String prefix() {
        return "//snowflake";
    }
}
