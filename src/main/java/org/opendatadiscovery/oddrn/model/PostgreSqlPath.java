package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Getter;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Getter
@Builder
public class PostgreSqlPath implements OddrnPath {
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

    @PathField(dependency = "table", prefix = "columns")
    private final String tableColumn;

    @PathField(dependency = "view", prefix = "columns")
    private final String viewColumn;

    @Override
    public String prefix() {
        return "//postgresql";
    }
}
