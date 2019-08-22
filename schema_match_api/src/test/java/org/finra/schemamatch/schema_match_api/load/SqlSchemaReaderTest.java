package org.finra.schemamatch.schema_match_api.load;

import org.apache.commons.dbcp2.BasicDataSource;
import org.finra.schemamatch.database.DatabaseTree;
import org.finra.schemamatch.load.SqlSchemaReader;
import org.finra.schemamatch.security.CredStashStringConverter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

@Profile("local")
public class SqlSchemaReaderTest {

    @Value("schemamatch.test.db.urlList")
    String[] urls;

    @Value("schemamatch.test.db.usernameList")
    String[] usernames;

    @Value("schemamatch.test.db.passwordList")
    String[] passwords;

    @Value("schemamatch.test.db.driverList")
    String[] drivers;

    @Test
    public void sqlSchemaReaderTest() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException, MetaDataAccessException {

        for(int pos = 0; pos<urls.length; pos++) {

            SqlSchemaReader reader = new SqlSchemaReader();

            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setDriverClassName(drivers[pos]);
            dataSource.setUsername(usernames[pos]);
            dataSource.setPassword(passwords[pos]);
            dataSource.setUrl(urls[pos]);
            dataSource.setMaxIdle(5);
            dataSource.setInitialSize(5);
            dataSource.setValidationQuery("SELECT 1");

            DatabaseTree tree = reader.loadTablesForDatabase(dataSource, "Test DB");
            System.out.println(tree);
        }
    }
}
