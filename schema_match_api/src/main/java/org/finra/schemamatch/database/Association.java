package org.finra.schemamatch.database;

import java.util.List;

/**
 * Relationship between entities in a database such as a Foreign Key relationship or
 * a cross database association derived from a matching algorithm.
 */
public class Association{

    String name;

    boolean identical;

    public List<DatabaseSchemaEntity> entries;

    public Association() {

    }

    public List<DatabaseSchemaEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<DatabaseSchemaEntity> entries) {
        this.entries = entries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIdentical() {
        return identical;
    }

    public void setIdentical(boolean identical) {
        this.identical = identical;
    }

    @Override
    public String toString() {
        return "Association{" +
                "name='" + name + '\'' +
                ", identical=" + identical +
                ", entries=" + entries +
                '}';
    }
}
