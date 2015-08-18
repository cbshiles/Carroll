package sourceone.sql;

import sourceone.key.*;

public class InsertDest implements StringDest{
    String stmt, vals="";
    boolean ID_check, first = true;
    int id=-1;

    public InsertDest(Key k){
	stmt = "INSERT INTO "+k.name+" ("+k.sqlNames()+") VALUES (";
	ID_check = false;
    }
    
    public InsertDest(Key k, String tableName){
	stmt = "INSERT INTO "+tableName+" (";
	stmt += k.sqlNames()+") VALUES (";
    }

    public InsertDest(Key k, String tableName, boolean ID_check){
	this(k, tableName);
	this.ID_check = ID_check;
    }

    public void put(String s) {
	if (! first) vals += ", ";
	else first = false;
	vals += s;
    }

    public void endEntry(){
	String cmd = stmt+vals+");";
	try {
	    if (ID_check) id=SQLBot.bot.updateGetID(cmd);
	    else SQLBot.bot.update(cmd);
	}
	catch (Exception e){System.err.println(e);}
	vals = "";
	first = true;
    }

    public Object close(){return id;}
}
