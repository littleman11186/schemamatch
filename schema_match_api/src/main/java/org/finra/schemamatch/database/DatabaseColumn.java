package org.finra.schemamatch.database;

import org.finra.schemamatch.pattern.DataPattern;
import org.finra.schemamatch.pattern.LabelLogic;

public class DatabaseColumn extends DatabaseEntity{

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
