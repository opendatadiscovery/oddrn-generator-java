package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class HivePath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "databases")
    private final String database;

    @PathField(dependency = "database", prefix = "schemas")
    private final String schema;

    @PathField(dependency = "schema", prefix = "tables")
    private final String table;

    @PathField(dependency = "schema", prefix = "views")
    private final String view;

    @PathField(dependency = {"table", "view"}, prefix = "columns")
    private final String column;

    @PathField(prefix = "owners")
    private final String owner;

    @Override
    public String prefix() {
        return "//hive";
    }
}
