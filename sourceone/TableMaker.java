package sourceone;

import sourceone.key.*;
import sourceone.sql.*;

public class TableMaker{

    static int drop(Key key)throws Exception{
	return SQLBot.bot.update("DROP TABLE "+key.name+';');
    }

    static int create(Key key)throws Exception{
	String cmd = "CREATE TABLE "+key.name+" ( ID int NOT NULL AUTO_INCREMENT, ";
	for (int i=1; i<key.cuts.length; i++){
		cmd += key.cuts[i].fullSQL+", ";
	}
//	System.out.println(cmd + "PRIMARY KEY (ID) )");
	return SQLBot.bot.update(cmd + "PRIMARY KEY (ID) )");
    }

    static void recreate(Key key)throws Exception{
	drop(key);
	create(key);
    }
    
    public static void main(String[] args)throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");
	 // recreate(Key.customerKey);
	 // recreate(Key.contractKey);
	 // recreate(Key.floorKey);
	//recreate(Key.paymentKey);
	recreate(Key.metaKey);
    }
}
