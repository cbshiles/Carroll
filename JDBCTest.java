import java.sql.*;
import javax.sql.*;

public class JDBCTest {
    public static void main(String[] args){
	Connection conn;
	try
        {
            String userName = "root";
            String password = "pelican93"; //invalidates idea of password, could just not keep source around?
	    //and have a password changing mechanism

	    	    String url = "jdbc:mysql://localhost/EMP";  //EMP is the database
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);


	    System.out.println ("Database connection established");
        }
	
        catch (Exception e)
        {
            System.out.println ("Cannot connect to database server " +e.getClass().getName());

        }
    }
}
