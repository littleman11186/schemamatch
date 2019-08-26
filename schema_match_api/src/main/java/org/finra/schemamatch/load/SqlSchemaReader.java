package org.finra.schemamatch.load;

import org.apache.commons.dbcp2.BasicDataSource;
import org.finra.schemamatch.database.Relationship;
import org.finra.schemamatch.database.DatabaseColumn;
import org.finra.schemamatch.database.DatabaseTable;
import org.finra.schemamatch.database.DatabaseTree;
import org.finra.schemamatch.error.DatabaseException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Metadata reader into database tree structure. Available SQL metadata:
 *
 *
 *
 */

@Service
public class SqlSchemaReader {

	public SqlSchemaReader() {
	}

	public DatabaseTree loadTablesForDatabase(String url, String username, String password, String driver, String dbName) throws MetaDataAccessException, SQLException, DatabaseException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setDriverClassName(driver);
        Connection con = dataSource.getConnection();
		DatabaseTree tree = new DatabaseTree(dbName, DatabaseType.SQL, dataSource);


        DatabaseMetaData dbMeta = con.getMetaData();

        String[] TYPES = {"TABLE", "VIEW"};
        ResultSet allTables = dbMeta.getTables(null, null, "%", TYPES );
        //Load all tables
        List<DatabaseTable> tables = new LinkedList<>();
        while (allTables.next()) {
            String tableName = allTables.getString("TABLE_NAME");

            DatabaseTable dbTable = new DatabaseTable(tableName);

            List<DatabaseColumn> dbColumns = new LinkedList<>();
            ResultSet allColumns = dbMeta.getColumns(null, null, tableName, null);

            //Load all columns
            while (allColumns.next()) {
                String name = allColumns.getString("COLUMN_NAME");
                String type = allColumns.getString("TYPE_NAME");
                int size = allColumns.getInt("COLUMN_SIZE");

                //TODO additional column labeling
            /*String columnName = columns.getString("COLUMN_NAME");
            String datatype = columns.getString("DATA_TYPE");
            String columnsize = columns.getString("COLUMN_SIZE");
            String decimaldigits = columns.getString("DECIMAL_DIGITS");
            String isNullable = columns.getString("IS_NULLABLE");
            String is_autoIncrment = columns.getString("IS_AUTOINCREMENT");*/

                DatabaseColumn dbColumn = new DatabaseColumn(name, type);
                dbColumns.add(dbColumn);
            }
            dbTable.setColumns(dbColumns);
            tables.add(dbTable);
        }


        tree.setTables(tables);

        getFKeyAssociations(tree);

        return tree;
	}

    /**
     * Load schema defined foreign key associations and attach them to the database tree.
     *
     * @param tree
     * @return
     * @throws SQLException
     * @throws MetaDataAccessException
     */
    public void getFKeyAssociations(DatabaseTree tree) throws SQLException, MetaDataAccessException, DatabaseException {
	    if(tree.getDatabaseFormat() != DatabaseType.SQL){
	        throw new DatabaseException("Cannot get foreign keys from non-SQL database");
        }
	    DataSource dataSource = (DataSource)tree.getDataSource();

	    for(DatabaseTable table : tree.getTables()) {
            ResultSet allKeys = dataSource.getConnection().getMetaData().getImportedKeys(null, null, table.getLabel());

            List<Relationship> tableRelationships = new LinkedList<Relationship>();
            while (allKeys.next()) {
                String tableTarget = allKeys.getString("FKTABLE_NAME"); //Table name containing pk
                String targetPk = allKeys.getString("FKCOLUMN_NAME"); //Pk name
                String localFk = allKeys.getString("PKCOLUMN_NAME");//Column name of fk
                String indexName = allKeys.getString("FK_NAME");//Name of fk

                Relationship association = new Relationship("FK "+table.getName() + " " + tableTarget, indexName);

                DatabaseColumn targetColumn = tree.getColumn(tableTarget, targetPk);
                DatabaseColumn localColumn = tree.getColumn(table.getLabel(), localFk);

                if(targetColumn != null && localColumn != null) {
                    //TODO double check direction of vector. Which is parent and which is target?
                    association.setStartNodeId(localColumn.getId());
                    association.setEndNodeId(targetColumn.getId());

                    tableRelationships.add(association);
                }
            }
            if(!tableRelationships.isEmpty()){
                table.setRelationships(tableRelationships);
            }
        }
    }
}
