package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class MysqlPath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "databases")
    private final String database;

    @PathField(dependency = "database", prefix = "tables")
    private final String table;

    @PathField(dependency = "database", prefix = "views")
    private final String view;

    @PathField(dependency = {"table", "view"}, prefix = "columns")
    private final String column;

    @Override
    public String prefix() {
        return "//mysql";
    }
}
