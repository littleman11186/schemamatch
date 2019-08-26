package org.finra.schemamatch.database;

public class DatabaseColumn extends DatabaseSchemaEntity {

	String columnType;

	DatabaseTable parentTable;

	public DatabaseColumn(DatabaseTable parent, String label, String columnType) {
		super(label, DatabaseEntityType.COLUMN);
		this.columnType = columnType;
		this.parentTable = parent;
	}


	@Override
	public String toString() {
		return "DatabaseColumn{" +
				"columnType='" + columnType + '\'' +
				", label='" + label + '\'' +
				", type=" + type +
				", id=" + id +
				'}';
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public DatabaseTable getParentTable() {
		return parentTable;
	}

	public void setParentTable(DatabaseTable parentTable) {
		this.parentTable = parentTable;
	}
}
