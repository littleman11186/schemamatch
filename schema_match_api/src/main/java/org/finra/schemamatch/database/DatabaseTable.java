package org.finra.schemamatch.database;

import java.util.List;

public class DatabaseTable extends DatabaseSchemaEntity {
	protected List<DatabaseColumn> columns;

	protected List<Relationship> relationships;

	protected List<DatabaseColumn> primaryKeys;
	
	public DatabaseTable(String label) {
		super(label, DatabaseEntityType.TABLE);
	}

	public List<DatabaseColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DatabaseColumn> columns) {
		this.columns = columns;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	public List<DatabaseColumn> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<DatabaseColumn> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	@Override
	public String toString() {
		return "DatabaseTable{" +
				"columns=" + columns +
				", relationships=" + relationships +
				", primaryKeys=" + primaryKeys +
				", label='" + label + '\'' +
				", name='" + name + '\'' +
				", type=" + type +
				", id='" + id + '\'' +
				'}';
	}
}
