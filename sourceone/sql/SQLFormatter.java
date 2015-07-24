package sourceone.sql;

import sourceone.key.*;

/**
   Packages up Kinds with the appropriate quotations and whatnot.
   Allows NULL values for Date and String types.
 */

public class SQLFormatter extends Formatter {

    public SQLFormatter(StringDest s){
	super(s);
    }

    public String convert(String x){
	if (x == null) return "NULL";
	else return "'"+x.replaceAll("'", "''")+"'";
    }
    
    public String convert(int x){
	return ""+x;
    }
    
    public String convert(float x){
	return ""+x;
    }

    public String convert(java.time.LocalDate x){
	if (x == null) return "NULL";
	else return "'"+x+"'";
    }
}
