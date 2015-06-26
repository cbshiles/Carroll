import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Table {
    String name;
    final Column[] cols;
    DataMap map;
    SQLBot bot;
    Object[][] objs;

    public Table(String n, Column[] c){
	name = n;
	cols = c;
	bot = Driver.bot;
    }
    
    public Table(String n, Column[] c, SQLBot b){
    	name = n;
    	cols = c;
    	bot = b;
    	System.err.println("Don't call this constructor");
    	System.exit(1);
    }

    String[] getHeads(){
	String[] sarr = new String[cols.length];
	for (int i=0; i<cols.length; i++)
	    sarr[i] = cols[i].name;
	return sarr;
    }

    Object[][] d2Converter(List<List<Object>> bigLzt){
	int sz = bigLzt.size();
	Object[][] arr = new Object[sz][];

	for (int i=0; i<sz; i++){
	    arr[i] = bigLzt.get(i).toArray();
	}

	return arr;
    }

    //Dangerous: only works if table object has all the columns the actual SQL table has
    List<List<Object>> readAll() throws SQLException {
	return readDB("SELECT * FROM "+name);
    }
    
    List<List<Object>> readCols(String rest) throws SQLException {
	boolean first = true;
	String fields = "";
	for (Column c : cols){
	    if (! first) fields += ", ";
	    else first = false;
	    fields += c.sqlName;
	}
	String q = "SELECT "+fields+" FROM "+name+" ";
	return readDB(q+rest);
    }

    List<List<Object>> readCols() throws SQLException {
	return readCols("");}
    
    List<List<Object>> readDB(String q) throws SQLException { //Read all from the DB
	
	ResultSet rs = bot.query(q);
	List<List<Object>> bigLzt = new ArrayList<List<Object>>();

	while (rs.next()){
	    int j = 1;
	    List<Object> lzt = new ArrayList<Object>();
	    for (Column c : cols){
		switch(c.type) {

		 case STRING:
		    lzt.add(rs.getString(j++)); break;
		 case INT:
		     lzt.add(rs.getInt(j++)); break;
		 case FLOAT:
		     lzt.add(rs.getFloat(j++)); break;
		 case DATE:
		     lzt.add(dater(rs.getDate(j++))); break;
		 default:
		     throw new SQLException("Invalid column type");
		}
	    }
	    bigLzt.add(lzt);
	}
	return bigLzt;
    }

    LocalDate dater(java.sql.Date d){
	if (d == null) return null;
	else return LocalDate.ofEpochDay(d.getTime()/86400000); //millis in day
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    String princ(LocalDate d){
	return d.format(dtf);
    }

    String princ(float f){
	return String.format("%.02f", f);
    }
    
    // List<Object> readRow() throws InputXcpt{ //Read from a form
    // 	List<Object> lzt = new ArrayList<Object>();
    // 	for (Column c : cols){
    // 	    if (c.type == 0) //String
    // 		lzt.add(map.getStr(c.name));
    // 	    else if (c.type == 1) //Integer
    // 		lzt.add(map.getInt(c.name));
    // 	    else if (c.type == 2) //Float
    // 		lzt.add(map.getFloat(c.name));
    // 	    else if (c.type == 3) //Date
    // 		lzt.add(map.getDate(c.name));
    // 	    else if (c.type == 4) //Command
    // 		;
    // 	    else
    // 		throw InputXcpt(c.name, "Invalid column type");
    // 	}
    // 	return lzt;
    // }

    // void insertRow(List<Object> lzt){
    // 	String qry = "INSERT INTO "+name+" ( ";
    // 	boolean first = true;
    // 	String names = "", values = "";
    // 	int j = 0;	
    // 	for (int i=0; i<cols.length; i++){
    // 	    if (! first){
    // 		names += ", ";
    // 		values += ", ";
    // 	    }
    // 	    else first = false;

    // 	    Column c = cols[i];
    // 	    names += cols[i].sqlName;
    // 	    if (c.type == 0) //String
    // 		values += toSQL((String) lzt.get(j));
    // 	    else if (c.type == 1) //Integer
    // 		values += toSQL((Integer) lzt.get(j));
    // 	    else if (c.type == 2) //Float
    // 		values += toSQL((Float) lzt.get(j));
    // 	    else if (c.type == 3) //Date
    // 		values += toSQL((Date) lzt.get(j));
    // 	    else if (c.type == 4) //Literal
    // 	    {values += c.txt; j--;}

    // 	    j++;
    // 	}
    // }
    
}
