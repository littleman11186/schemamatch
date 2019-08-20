package org.finra.schemamatch.data;

public abstract class DatabaseEntity {
	String label;
	DatabaseEntityType type;
	
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
}
