package sourceone.sql;

import sourceone.key.*;

public class SQLOut implements Output{
    String stmt, vals="";
    boolean first = true;
    
    public SQLOut(Key k, String tableName){
	stmt = "INSERT INTO "+tableName+" (";
	
	String keys = "";
	for (Cut c : k.cuts){
	    if (! first) keys += ", ";
	    else first = false;
	    keys += c.sqlName;
	}

	stmt += keys+") VALUES (";
	first = true;
    }

    public void put(String s)
    {
	if (s == null) add(null);
	else add("'"+s+"'");
    }

    public void put(java.time.LocalDate d)
    {
	if (d == null) add(null);
	else add("'"+d.toString()+"'");
    }
    
    public void put(int n)
    {add(((Integer)n).toString());}

    public void put(float n)
    {add(((Float)n).toString());}

    private void add(String s) {
	if (! first) vals += ", ";
	else first = false;
	vals += s;
    }

    public void endEntry(){

	System.out.println(stmt+vals+");");
	try {
	SQLBot.bot.update(stmt+vals+");");
	}
	catch (Exception e){System.err.println(e.getMessage());}
	vals = "";
    }
}
