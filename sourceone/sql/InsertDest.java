package sourceone.sql;

import sourceone.key.*;

public class InsertDest implements StringDest{
    String stmt, vals="";
    boolean first = true;

    public InsertDest(Key k, String tableName){
	stmt = "INSERT INTO "+tableName+" (";
	stmt += k.sqlNames()+") VALUES (";
	first = true;
    }

    public void put(String s) {
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
	first = true;
    }

    public Object close(){return null;}
}
