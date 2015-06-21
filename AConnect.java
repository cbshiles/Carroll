import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class AConnect {

    public static Connection link(String pFile) {
	try {
	    Properties props = new Properties();
	    props.load(new FileInputStream(pFile));

	    String user = props.getProperty("user");
	    String password = props.getProperty("password");
	    String url = "jdbc:mysql://localhost/"+props.getProperty("database");
	    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	    return DriverManager.getConnection (url, user, password);
        }
	
        catch (Exception e)
        {
            System.out.println ("Cannot connect to database server " +e.getClass().getName());
	    return null;

        }
    }

    public static void dbInfo() throws SQLException{
	DatabaseMetaData databaseMetaData = conn.getMetaData();

	int    majorVersion   = databaseMetaData.getDatabaseMajorVersion();
	int    minorVersion   = databaseMetaData.getDatabaseMinorVersion();

	String productName    = databaseMetaData.getDatabaseProductName();
	String productVersion = databaseMetaData.getDatabaseProductVersion();
	System.out.println(majorVersion+"."+minorVersion+" "+productName+" "+productVersion);
    }

    public static int insert(String sql, Statement stmt) {
	try {
	    return stmt.executeUpdate(sql);
	} catch (SQLException e){
	    System.out.println("Insert err: "+e.getMessage());
	    return 0;
	}
    }
    
    public static void main(String[] args){
	Connection conn = link("db.properties");

	int i = 0;
	String lname = "Biggins";
	String fname= "Martha";
	String email= "mama@large.com";
	String phone= "4235552323";
	String cmd = "INSERT INTO Persons (LastName, FirstName, email, phone) VALUES ('"+lname+"', '"+fname+"', '"+email+"', '"+phone+"');";
	try {
	    dbInfo(conn);
	    Statement stmt = conn.createStatement();
	    insert(cmd, stmt);

	    BackForm bf = new BackForm("Persons", conn);

	    
	    
	    ResultSet rs = stmt.executeQuery("SELECT * FROM Persons");
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int nc = rsmd.getColumnCount();
	    System.out.println("nc: "+nc);
	    while (rs.next()) {
		for (int j=1; j<=nc; j++){
		    System.out.print(rs.getString(j)+" ");
		}
		System.out.println();
		i++;
	    }
	}	
	    catch (Exception e)
	    {
		System.out.println ("Main() err " +e.getClass().getName() + " : "+e.getMessage());
	    }

	System.out.println(i);
    }
}
