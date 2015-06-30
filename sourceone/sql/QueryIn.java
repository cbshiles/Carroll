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

    public boolean hasEntries(){
	i=1;
	try {return rs.next();}
	catch (Exception e){
	    System.out.println("QueryIn hasEntries Exception");
	    return false;
	}
    }

    public String getString() throws InputXcpt{
	try {
	    return rs.getString(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    public int getInt() throws InputXcpt{
	try {
	    return rs.getInt(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    public float getFloat() throws InputXcpt{
	try {
	    return rs.getFloat(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    public LocalDate getDate() throws InputXcpt{
	try {
	    Date d =  rs.getDate(i++);
	    if (d == null) return null;
	    else return LocalDate.ofEpochDay(d.getTime()/86400000); //millis in day
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }
    
}
