package sourceone.sql;

import java.sql.*;
import javax.sql.*;
import java.time.LocalDate;
import sourceone.key.*;

public class QueryIn implements Input{

    ResultSet rs;
    int i = 1;
    
    public QueryIn(String q) throws SQLException{
	rs = SQLBot.bot.query(q);
    }
    
    public QueryIn(Key k, String rest) throws SQLException{
	if (k.name == null)
	    throw new SQLException("Table keys must be named");

	String cmd = "SELECT "+k.sqlNames()+" FROM "+k.name+" "+rest;//+';';
	rs = SQLBot.bot.query(cmd);	
    }
    
    public QueryIn(Key a, Key b, String rest) throws SQLException{
	if (a.name == null || b.name == null)
	    throw new SQLException("Table keys must be named for a join");

	String cmd = "SELECT "+a.sqlNames(true)+", "+b.sqlNames(true)+" FROM "+a.name+", "+b.name+" "+rest;
	rs = SQLBot.bot.query(cmd);	
    }
    
    public boolean hasEntries(){
	i=1;
	try {return rs.next();}
	catch (SQLException e){
	    System.err.println("QueryIn hasEntries Exception\n"+e);
	    return false;
	}
    }

    public String getString() throws InputXcpt{
	try {
	    return rs.getString(i++);
	} catch (SQLException e) {
	    throw new InputXcpt(e);
	}
    }

    public int getInt() throws InputXcpt{
	try {
	    return rs.getInt(i++);
	} catch (SQLException e) {
	    throw new InputXcpt(e);
	}
    }

    public float getFloat() throws InputXcpt{
	try {
	    return rs.getFloat(i++);
	} catch (SQLException e) {
	    throw new InputXcpt(e);
	}
    }

    public LocalDate getDate() throws InputXcpt{
	try {
	    return convertDate(rs.getDate(i++));
	} catch (SQLException e) {
	    throw new InputXcpt(e);
	}
    }

    public static LocalDate convertDate(Date d){
	if (d == null) return null;
	else return LocalDate.ofEpochDay(d.getTime()/86400000); //millis in day
    }

    public void done(){
	SQLBot.bot.done();
    }

    public void clear(){rs = null;}
    
}
