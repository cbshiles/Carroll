package sourceone;

import sourceone.key.*;
import sourceone.sql.*;

public class ResHack{

        public static void main(String[] args)throws Exception{
	    Key key = Key.reserveKey;
	    String cmd = "CREATE TABLE "+key.name+" ( ID int NOT NULL AUTO_INCREMENT, ";
	    for (int i=1; i<key.cuts.length; i++){
		cmd += key.cuts[i].fullSQL+", ";
	    }
	    System.out.println(cmd + "PRIMARY KEY (ID) )");

	}
}
