package org.finra.schemamatch.data;

import org.finra.schemamatch.database.DatabaseTable;

import java.util.Map;

public class DatabaseRow {

    Map<String, Object> fields;

    protected String originId;
    DatabaseTable origin;
}
