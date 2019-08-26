package org.finra.schemamatch.database;

import org.finra.schemamatch.load.DatabaseType;

import javax.sql.DataSource;
import java.util.List;

public class DatabaseTree {
	protected List<DatabaseTable> tables;

	protected String name;

	protected long id;

	protected DatabaseType databaseFormat;

	protected Object dataSource; //Connection definitions

	public DatabaseTree(String name, DatabaseType dbType, Object dataSource){
		this.name = name;
		this.databaseFormat = dbType;
		this.dataSource = dataSource;
	}

    public List<DatabaseTable> getTables() {
        return tables;
    }

    public void setTables(List<DatabaseTable> tables) {
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DatabaseType getDatabaseFormat() {
        return databaseFormat;
    }

    public void setDatabaseFormat(DatabaseType databaseFormat) {
        this.databaseFormat = databaseFormat;
    }

    public Object getDataSource() {
        return dataSource;
    }

    public void setDataSource(Object dataSource) {
        this.dataSource = dataSource;
    }

    public DatabaseColumn getColumn(String tableName, String columnName){
	    for(DatabaseTable table : tables){
	        if(table.getLabel().equalsIgnoreCase(tableName)){
	            for(DatabaseColumn column : table.getColumns()){
	                if(column.getLabel().equalsIgnoreCase(columnName)){
	                    return column;
                    }
                }
            }
        }
	    return null;
    }

    public DatabaseTable getTable(String tableName){
        for(DatabaseTable table : tables){
            if(table.getLabel().equalsIgnoreCase(tableName)){
                return table;
            }
        }
        return null;
    }


    @Override
    public String toString(){
	    StringBuilder builder = new StringBuilder();
	    builder.append("Database: ").append(name).append(System.lineSeparator());
	    builder.append("   - type: ").append(databaseFormat.toString()).append(System.lineSeparator());
	    if(tables != null) {
            for (DatabaseTable table : tables) {
                builder.append("   - table: ").append(table.getLabel()).append(System.lineSeparator());
                for (DatabaseColumn column : table.getColumns()) {
                    builder.append("      - column: ").append(column.getLabel()).append(System.lineSeparator());
                }
                if(table.getRelationships() != null) {
                    for (Relationship relationship : table.getRelationships()) {
                        builder.append("      - key: ").append(relationship.getLabel()).append(System.lineSeparator());
                    }
                }
            }
        }

	    return builder.toString();
    }
}
