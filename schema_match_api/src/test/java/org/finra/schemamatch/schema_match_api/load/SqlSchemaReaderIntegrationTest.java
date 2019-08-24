package org.finra.schemamatch.schema_match_api.load;

import org.apache.commons.dbcp2.BasicDataSource;
import org.finra.schemamatch.database.DatabaseTree;
import org.finra.schemamatch.database.DatabaseConnections;
import org.finra.schemamatch.load.SqlSchemaReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"org.finra.schemamatch"})
public class SqlSchemaReaderIntegrationTest {
    EmbeddedDatabase embeddedSource;

    @Before
    public void setupDataSource() {

        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.H2) //.H2 or .DERBY
                .addScript("/db/simplesampledatabase.sql")
                .build();
        this.embeddedSource = db;
    }

    @Test
    public void sqlSchemaReaderTest() throws Exception {
        SqlSchemaReader reader = new SqlSchemaReader();

        DatabaseTree tree = reader.loadTablesForDatabase("jdbc:h2:mem:testdb", "sa", "", "org.h2.Driver", "Test DB");
        System.out.println(tree);
    }
}
