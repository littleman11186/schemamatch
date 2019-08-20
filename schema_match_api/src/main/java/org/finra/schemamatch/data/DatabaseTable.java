package org.finra.schemamatch.data;

import java.util.List;

public class DatabaseTable extends DatabaseEntity {
	protected List<DatabaseColumn> columns;
	
	public DatabaseTable(String label, List<DatabaseColumn> columns) {
		super(label, DatabaseEntityType.TABLE);
		this.columns = columns;
	}

	public List<DatabaseColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DatabaseColumn> columns) {
		this.columns = columns;
	}
}
