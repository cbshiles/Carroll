package sourceone.sql;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

public class SQLBot {

    public static SQLBot bot;
    Connection conn;
    Statement stmt;
    
    public SQLBot(String pFile) throws Exception{

	Properties props = new Properties();
	props.load(new FileInputStream(pFile));
	String user = props.getProperty("user");
	String password = props.getProperty("password");
	String url = "jdbc:mysql://localhost/"+props.getProperty("database");
	Class.forName ("com.mysql.jdbc.Driver").newInstance();

	conn = DriverManager.getConnection (url, user, password);
	stmt = conn.createStatement();
    }

    public void dbInfo() throws SQLException{
	DatabaseMetaData databaseMetaData = conn.getMetaData();

	int    majorVersion   = databaseMetaData.getDatabaseMajorVersion();
	int    minorVersion   = databaseMetaData.getDatabaseMinorVersion();

	String productName    = databaseMetaData.getDatabaseProductName();
	String productVersion = databaseMetaData.getDatabaseProductVersion();
	System.out.println(majorVersion+"."+minorVersion+" "+productName+" "+productVersion);
    }

    public int update(String sql) throws SQLException{
	return stmt.executeUpdate(sql);
    }

    public ResultSet query(String sql) throws SQLException{
	return stmt.executeQuery(sql);
    }

    public void printSet(ResultSet rs) throws SQLException{
	ResultSetMetaData rsmd = rs.getMetaData();
	int nc = rsmd.getColumnCount();
	while (rs.next()) {
	    for (int j=1; j<=nc; j++){
		System.out.print(rs.getString(j)+" ");
	    }
	    System.out.println();
	}
    }	
}
