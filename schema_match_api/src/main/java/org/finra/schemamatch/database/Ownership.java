package org.finra.schemamatch.database;

import java.io.Serializable;

/**
 * Mechanism for storing tree structure ownership models
 */
public class Ownership implements Serializable {

    protected long parentNodeId;

    protected long childNodeId;
}
