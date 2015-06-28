import java.sql.*;
import javax.sql.*;
import java.time.LocalDate;

public class QueryIn{

    ResultSet rs;
    int i = 1;
    
    public QueryIn(String q){
	rs = Driver.bot.query();
    }

    boolean hasEntries(){
	i=1;
	return rs.next();
    }

    String getString() throws InputXcpt{
	try {
	    return rs.getString(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    int getInt() throws InputXcpt{
	try {
	    return rs.getInt(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    float getFloat() throws InputXcpt{
	try {
	    return rs.getFloat(i++);
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }

    LocalDate getDate() throws InputXcpt{
	try {
	    Date d =  rs.getDate(i++);
	    if (d == null) return null;
	    else return LocalDate.ofEpochDay(d.getTime()/86400000); //millis in day
	} catch (Exception e) {
	    throw new InputXcpt(e);
	}
    }
    
}
