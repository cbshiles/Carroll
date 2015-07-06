package sourceone;

import sourceone.key.*;
import sourceone.sql.*;

public class TableMaker{

    static void  shit(Key key, String[] post) throws Exception{
	assert (key.cuts.length == post.length);

//Do this first
	SQLBot.bot.update("DROP TABLE "+key.name+';');


	String max = "CREATE TABLE "+key.name+" ( ID int NOT NULL AUTO_INCREMENT, ";
	boolean first = true;
	for (int i=0; i<key.cuts.length; i++){
	    if (i > 0)
		max += key.cuts[i].sqlName + " " + post[i-1]+", ";
	}
	SQLBot.bot.update(max + "PRIMARY KEY (ID) )");
    }

    public static void main(String[] args)throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");
shit(Key.contractKey, new String[]{
		    "varchar(63) NOT NULL",
		    "varchar(63) NOT NULL",
		    "varchar(63)",
		    "varchar(63)",
		    "int NOT NULL",
		    "float NOT NULL",
		    "float NOT NULL",
		    "int NOT NULL",
		    "float NOT NULL",
		    "date NOT NULL",
		    "varchar(63)",
		    "varchar(31) NOT NULL",
		    "int NOT NULL",
		    "date",
		    "date NOT NULL"
    });
    }
}
