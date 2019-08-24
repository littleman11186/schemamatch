package org.finra.schemamatch.database;

public class DatabaseColumn extends DatabaseSchemaEntity {

	String columnType;

	public DatabaseColumn(String label, String columnType) {
		super(label, DatabaseEntityType.COLUMN);
		this.columnType = columnType;
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
}
