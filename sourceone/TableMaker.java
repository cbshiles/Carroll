package sourceone;

import sourceone.key.*;
import sourceone.sql.*;

public class TableMaker{

    static int drop(Key key)throws Exception{
	return SQLBot.bot.update("DROP TABLE "+key.name+';');
    }

    //A assumes all tables have an ID field (or rather assumes they want one)
    static int create(Key key)throws Exception{
	String cmd = "CREATE TABLE "+key.name+" ( ID int NOT NULL AUTO_INCREMENT, ";
	for (int i=1; i<key.cuts.length; i++){
	    cmd += key.cuts[i].fullSQL+", ";
	}
	return SQLBot.bot.update(cmd + "PRIMARY KEY (ID) )");
    }

    static void recreate(Key key)throws Exception{
	drop(key);
	create(key);
    }
    
    public static void main(String[] args)throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");
	create(Key.customerKey);
	create(Key.contractKey);
	create(Key.floorKey);
	create(Key.paymentKey);
	meta();
    }

    public static void meta() throws Exception{
	create(Key.metaKey); //change back to re
	SQLBot.bot.update("INSERT INTO Meta (Full_Report_Date, Partial_Report_Date) VALUES(NULL, NULL)");
    }
}