package org.opendatadiscovery.oddrn.model;

import lombok.Builder;
import lombok.Data;
import org.opendatadiscovery.oddrn.annotation.PathField;

@Data
@Builder
public class ClickhousePath implements OddrnPath {
    @PathField
    private final String host;

    @PathField(dependency = "host", prefix = "databases")
    private final String database;

    @PathField(dependency = "database", prefix = "tables")
    private final String table;

    @PathField(dependency = "database", prefix = "views")
    private final String view;

    @PathField(dependency = "table", prefix = "tables_columns")
    private final String tableColumn;

    @PathField(dependency = "view", prefix = "views_columns")
    private final String viewColumn;

    @Override
    public String prefix() {
        return "//clickhouse";
    }

//        "databases": ("databases",),
//        "tables": ("databases", "tables"),
//        "views": ("databases", "views"),
//        "tables_columns": ("databases", "tables", "tables_columns"),
//        "views_columns": ("databases", "views", "views_columns"),
//    host
}
