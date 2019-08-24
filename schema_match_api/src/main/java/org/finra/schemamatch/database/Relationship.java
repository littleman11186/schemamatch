package org.finra.schemamatch.database;

import org.finra.schemamatch.database.DatabaseSchemaEntity;
import org.finra.schemamatch.database.DatabaseEntityType;

import java.io.Serializable;

/**
 * Database row to database row link.
 */
public class Relationship extends DatabaseSchemaEntity implements Serializable {

	public Relationship(String name, String label, String id){
		super(name, label, DatabaseEntityType.FOREIGN_KEY, id);
	}

	public Relationship(String name, String label){
		super(name, label, DatabaseEntityType.FOREIGN_KEY);
	}

	protected String startNodeId;
	protected String endNodeId;

	public String getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}

	public String getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}
}
