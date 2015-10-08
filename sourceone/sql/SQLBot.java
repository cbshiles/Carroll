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
    public String path;
    Statement stmt;
    String ttl;
    
    public SQLBot(String pFile) throws Exception{

	Properties props = new Properties();
	props.load(new FileInputStream(pFile));
	String user = props.getProperty("user");
	String password = props.getProperty("password", "");
	String host = props.getProperty("hostname", "localhost");
	String db = props.getProperty("database");
	ttl = host+"/"+db;
	System.out.println("connecting to "+ttl);
	path = props.getProperty("csvPath", "");
	
	String url = "jdbc:mysql://"+ttl;
	Class.forName ("com.mysql.jdbc.Driver").newInstance();

	conn = DriverManager.getConnection (url, user, password);
	done();
    }

    public String title(){return ttl;}

    public void dbInfo() throws SQLException{
	DatabaseMetaData databaseMetaData = conn.getMetaData();

	int    majorVersion   = databaseMetaData.getDatabaseMajorVersion();
	int    minorVersion   = databaseMetaData.getDatabaseMinorVersion();

	String productName    = databaseMetaData.getDatabaseProductName();
	String productVersion = databaseMetaData.getDatabaseProductVersion();
	System.out.println(majorVersion+"."+minorVersion+" "+productName+" "+productVersion);
    }

    public void done(){
	try{
	    if (stmt != null) stmt.close();
	    stmt = conn.createStatement();
	    stmt.setQueryTimeout(5);
	} catch (Exception e) {System.out.println("Couldn't make new statement."); System.exit(1);}
    }

    public int update(String sql) throws SQLException{

	return stmt.executeUpdate(sql);
    }

    public int updateGetID(String sql) throws SQLException{
	System.out.println(sql);
	stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	ResultSet rs = stmt.getGeneratedKeys();
	rs.next();
	return rs.getInt(1);
    }
    
    public ResultSet query(String sql) throws SQLException{

	return stmt.executeQuery(sql);
    }

    public LocalDate query1Date(String sql) throws SQLException{
	ResultSet rs = query(sql);
	rs.next();
	return QueryIn.convertDate(rs.getDate(1));
    }

    public float query1Float(String sql) throws SQLException{
	ResultSet rs = query(sql);
	if (rs.next()){
	    return rs.getFloat(1);
	} else {
	    System.err.println(sql);
	    return 0f;
	}
    }
    
    public int query1Int(String sql) throws SQLException{
	ResultSet rs = query(sql);
	rs.next();
	return rs.getInt(1);
    }

    public String query1Name(String  sql) throws SQLException{
	ResultSet rs = query(sql);
	if (rs.next()){
	    return rs.getString(2)+", "+rs.getString(1);
	} else {
	    System.err.println(sql);
	    return "";
	}
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
