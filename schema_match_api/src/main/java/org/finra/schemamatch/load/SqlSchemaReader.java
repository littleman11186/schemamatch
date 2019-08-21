package org.finra.schemamatch.load;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.finra.schemamatch.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class SqlSchemaReader {

	
	public SqlSchemaReader() {

	}

	public DatabaseTree loadTablesForDatabase(DataSource dataSource, String label) throws MetaDataAccessException, SQLException {

		DatabaseTree tree = new DatabaseTree(label, DatabaseType.SQL, dataSource);

        ResultSet allTables = (ResultSet) JdbcUtils.extractDatabaseMetaData(
				dataSource,
				dbmd -> {
					ResultSet tables = dbmd.getTables(null, null, "Version", new String[] {"TABLE"});
					return tables;
				});


        //Load all tables
        List<DatabaseTable> tables = new LinkedList<>();
        while(allTables.next()){
            String tableName = allTables.getString("TABLE");

            DatabaseTable dbTable = new DatabaseTable(tableName);

            List<DatabaseColumn> dbColumns = new LinkedList<>();
            ResultSet allColumns = (ResultSet) JdbcUtils.extractDatabaseMetaData(
                    dataSource,
                    dbmd -> {
                        ResultSet columns = dbmd.getColumns(null, null, tableName, null);
                        return columns;
                    });

            //Load all columns
            while(allColumns.next()){
                String name = allColumns.getString("COLUMN_NAME");
                String type = allColumns.getString("TYPE_NAME");
                int size = allColumns.getInt("COLUMN_SIZE");

                DatabaseColumn dbColumn = new DatabaseColumn(name, type);
                dbColumns.add(dbColumn);
            }
            dbTable.setColumns(dbColumns);
            tables.add(dbTable);
        }

        tree.setTables(tables);
        return tree;
	}

    /**
     * Load schema defined foreign key associations
     *
     * @param tree
     * @return
     * @throws SQLException
     * @throws InvalidArgumentException
     * @throws MetaDataAccessException
     */
    public List<Association> getFKeyAssociations(DatabaseTree tree) throws SQLException, InvalidArgumentException, MetaDataAccessException {
	    if(tree.getDatabaseFormat() != DatabaseType.SQL){
	        throw new InvalidArgumentException(new String[]{"Cannot get foreign keys from non-SQL database"});
        }
	    DataSource dataSource = (DataSource)tree.getDataSource();

	    Map<String, Association> mappedAssociations = new HashMap<>();
	    List<Association> associations = new LinkedList<Association>();
	    for(DatabaseTable table : tree.getTables()) {
            ResultSet allKeys = (ResultSet) JdbcUtils.extractDatabaseMetaData(
                    dataSource,
                    dbmd -> {
                        ResultSet tables = dbmd.getImportedKeys(null, null, table.getLabel());
                        return tables;
                    });

            while (allKeys.next()) {
                String tableTarget = allKeys.getString(3); //Table name containing pk
                String targetPk = allKeys.getString(4); //Pk name
                String localFk = allKeys.getString(8);//Column name of fk

                Association association = mappedAssociations.get(tableTarget+"."+targetPk);
                if(association == null){
                    association = new Association();
                    association.setName("Foreign Key "+tableTarget+"."+targetPk);
                    association.setEntries(new LinkedList<DatabaseEntity>());
                    association.setIdentical(true);
                }

                DatabaseColumn targetColumn = tree.getColumn(tableTarget, targetPk);
                DatabaseColumn localColumn = tree.getColumn(table.getLabel(), localFk);

                if(!association.getEntries().contains(targetColumn)){
                    association.getEntries().add(targetColumn);
                }

                if(!association.getEntries().contains(localColumn)){
                    association.getEntries().add(localColumn);
                }
            }
        }
        return associations;
    }
}
