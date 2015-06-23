import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.swing.*;
//import java.awt.*;

public class ATable {
    String[] heads;
    SQLBot bot;
    List<List<Object>> objs = new ArrayList<List<Object>>();
    JTable jt;
    
    public ATable(){
	heads = new String [] {"Date Bought", "Item ID", "Vehicle", "Item Cost"};

//	for (int i=0; i<heads.length; i++)
//	    objs.add(new ArrayList<Object>());

	try {
	    bot = new SQLBot("../../db.properties");
	    ResultSet rs = bot.query("SELECT * FROM Cars");

	    //bot.printSet(rs);

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int nc = rsmd.getColumnCount();
	    int ii = 0;
	    while (rs.next()) {
		int j = 2;
		List<Object> lzt = new ArrayList<Object>();
		    //objs.get(ii);

		System.out.println(rs.getString(1));
		
		lzt.add(rs.getDate(j++));
		lzt.add(rs.getString(j++));
		lzt.add(rs.getString(j++));
		lzt.add(rs.getFloat(j++));
//		lzt.add(rs.getDate(j++));

		for (Object o : lzt)
		    System.out.println(o);
		
		objs.add(lzt);
	    }
	} catch (Exception e){
	    System.err.println("YO: "+e.getMessage());
	}

	Object[][] array = new Object[objs.size()][];
	for (int i = 0; i < objs.size(); i++) {
	    List<Object> row = objs.get(i);
	    array[i] = row.toArray(new Object[row.size()]);
	}

	jt = new JTable(array, heads);
    }

    public JTable getTable() {return jt;}
}
