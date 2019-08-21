package org.finra.schemamatch.data;

import org.finra.schemamatch.database.Association;

import java.io.Serializable;

/**
 * Database row to database row link.
 */
public class Relationship implements Serializable {

	public String associationName;

	public Association associationModel;

	private long startNodeId;
	private long endNodeId;

	DatabaseRow startNode;
	DatabaseRow endNode;
}
