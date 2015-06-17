import java.sql.*;
import javax.sql.*;

import javax.naming.*;

/*
This will be useful IF scalability becomes an issue right now.
Having an initial context problem atm.
Since having issues, just going to use DriverManager until that needs fixing.
 */

public class DataSourceInit {
    public static void main(String[] args){
	Connection conn;
	try
        {
            String userName = "root";
            String password = "pelican93"; //invalidates idea of password, could just not keep source around?
	    //and have a password changing mechanism
	    String database = "EMP";
	    String sourceName = "EMPdb";
	    
	    com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
	    ds.setServerName("localhost");
	    ds.setDatabaseName(database);
	    conn = ds.getConnection(userName, password);

	    Context ctx = new InitialContext();

	    if ( ctx == null){
		System.out.println("JNDI problem. Cannot get InitialContext.");
	    }
	    
	    ctx.bind("jdbc/"+sourceName, ds);

	    
            System.out.println ("Database connection established");
        }
	
        catch (Exception e)
        {
            System.out.println ("Cannot connect to database server " +e.getClass().getName());

        }
    }
}
