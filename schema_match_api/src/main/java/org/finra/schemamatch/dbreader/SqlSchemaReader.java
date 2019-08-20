package org.finra.schemamatch.dbreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class SqlSchemaReader {
	
	
	public SqlSchemaReader() {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	
	public Connection getConnection(String url, String user, String password) throws SQLException {
		Connection con = DriverManager.getConnection(url,user,password);
		return con;
	}
	
	
}
