package org.finra.schemamatch.data;

import java.util.List;

public class Relationship {

	public String name;
	
	public List<DatabaseEntity> entries;
	
	public Relationship() {
		
	}

	public List<DatabaseEntity> getEntries() {
		return entries;
	}

	public void setEntries(List<DatabaseEntity> entries) {
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
