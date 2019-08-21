package org.finra.schemamatch.database;

import java.util.List;
import java.util.Map;

public class DatabaseTable extends DatabaseEntity {
	protected List<DatabaseColumn> columns;

	protected DatabaseColumn primaryKey;
	
	public DatabaseTable(String label) {
		super(label, DatabaseEntityType.TABLE);
	}

	public List<DatabaseColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DatabaseColumn> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "DatabaseTable{" +
				"columns=" + columns +
				", primaryKey=" + primaryKey +
				", label='" + label + '\'' +
				", type=" + type +
				", id=" + id +
				'}';
	}
}
