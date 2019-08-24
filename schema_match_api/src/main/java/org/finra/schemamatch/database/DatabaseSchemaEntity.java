package org.finra.schemamatch.database;

import java.io.Serializable;

public abstract class DatabaseSchemaEntity implements Serializable {
	protected String label;
	protected String name;
	protected DatabaseEntityType type;
	protected String id;
	
	protected DatabaseSchemaEntity(String label, DatabaseEntityType type){
		this.name = label;
		this.label = label;
		this.type = type;
		this.id = generateId();
	}

	protected DatabaseSchemaEntity(String name, String label, DatabaseEntityType type){
		this.name = name;
		this.label = label;
		this.type = type;
		this.id = generateId();
	}


	protected DatabaseSchemaEntity(String label, DatabaseEntityType type, String id){
		this.name = label;
		this.label = label;
		this.type = type;
		this.id = id;
	}

	protected DatabaseSchemaEntity(String name, String label, DatabaseEntityType type, String id){
		this.name = name;
		this.label = label;
		this.type = type;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public DatabaseEntityType getType() {
		return type;
	}

	public void setType(DatabaseEntityType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DatabaseSchemaEntity{" +
				"label='" + label + '\'' +
				", name='" + name + '\'' +
				", type=" + type +
				", id='" + id + '\'' +
				'}';
	}

	public static String generateId(){
		return java.util.UUID.randomUUID().toString();
	}
}
