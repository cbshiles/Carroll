import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class BackForm {

    String table;
    Map<String, Datum> map;
    Connection conn;
    Statement stmt;
    
    public BackForm(String tbl, Connection c){
	conn = c;
	try {
	stmt = c.createStatement();
	} catch (SQLException e){
	    System.out.println("Insert err: "+e.getMessage());
	}

	table = tbl;
	map = new HashMap<String, Datum>();
    }

    

    public int nsert() {

	String columns="", values="";
	Iterator it = map.entrySet().iterator();

	boolean first = true;

	for (Map.Entry<String, Datum> pair : map.entrySet()) {
	
	    if (! first)
	    {columns += ","; values += ",";}
	    else first = false;

	    columns += pair.getKey();
	    values += pair.getValue();
	}
	String cmd = "INSERT INTO "+table+" ("+columns+") VALUES ("+values+");";
	System.out.println(cmd);
	return insert(cmd, stmt);
    }

    public static int insert(String sql, Statement stmt) {
	try {
	    return stmt.executeUpdate(sql);
	} catch (SQLException e){
	    System.out.println("Insert err: "+e.getMessage());
	    return 0;
	}
    }
    
}

/* Old iterator way to go through map
	Iterator it = map.entrySet().iterator();

	boolean first = true;
	
	while (it.hasNext()) {
	    if (! first)
	    {columns += ","; values += ",";}
	    else first = false;

	    Map.Entry pair = (Map.Entry) it.next();
	    columns += pair.getKey();
	    values +=
	}



 */
