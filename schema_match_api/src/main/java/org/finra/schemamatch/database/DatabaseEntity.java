package org.finra.schemamatch.database;

import java.io.Serializable;

public abstract class DatabaseEntity implements Serializable {
	String label;
	DatabaseEntityType type;
	protected long id;
	
	DatabaseEntity(String label, DatabaseEntityType type){
		this.label = label;
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "DatabaseEntity{" +
				"label='" + label + '\'' +
				", type=" + type +
				", id=" + id +
				'}';
	}
}
