package org.finra.schemamatch.database;

import java.io.Serializable;

/**
 * Mechanism for storing tree structure ownership models
 */
public class Ownership extends DatabaseSchemaEntity implements Serializable {

    public Ownership(String name, String id){
        super(name, name, DatabaseEntityType.OWNERSHIP, id);
    }

    public Ownership(String name){
        super(name, name, DatabaseEntityType.OWNERSHIP);
    }

    protected String parentNodeId;

    protected String childNodeId;

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getChildNodeId() {
        return childNodeId;
    }

    public void setChildNodeId(String childNodeId) {
        this.childNodeId = childNodeId;
    }
}
