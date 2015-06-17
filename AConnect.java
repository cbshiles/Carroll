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

    public static void insert(String lname, String fname, String email, String phone, Statement stmt) {
	try {
	    int rs = stmt.executeUpdate("INSERT INTO Persons (LastName, FirstName, email, phone) VALUES ('"+lname+"', '"+fname+"', '"+email+"', '"+phone+"');");	}
	catch (SQLException e){
	    System.out.println("Insert err: "+e.getMessage());
	}
    }
    
    public static void main(String[] args){
	Connection conn = link("db.properties");

		    int i = 0;
	try {
	    Statement stmt = conn.createStatement();
	    insert("Biggins", "Martha", "mama@large.com", "4235552323", stmt);
	    
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
