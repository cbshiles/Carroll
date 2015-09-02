package sourceone;

import java.util.HashMap;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.key.*;

import java.sql.*;
import java.util.*;

public class ARLoad{

    static Key k;

    static boolean fll;
    static String col, sqlCol;
    
    public static void du(boolean f)throws Exception{
	String fName = "spreadsheets/"+(f?'F':'N')+"arnum.csv";
	fll=f;
	col = fll?"Total Contract":"Gross Amount";
	sqlCol =col.trim().replaceAll("\\s", "_");;
	k = new Key(new Cut[]{new StringCut("AR Num"), new StringCut("Last Name"), new StringCut("First Name"), new FloatCut(col), new DateCut("Start Date")});
	Grid g = new Grid(k, new StringIn(new CSVInput(fName)));
	View v = new View(Key.contractKey.just(new String[]{"ID", "AR Num"}), new ARent());
	g.addOut(v);
	g.pull();
	g.push1();
	int z=0;
	int ars=0, nars=0;
	for (Object[] e: v.data){
	    if (e == null) {Object[] q = g.data.get(z++); System.out.println("ERROR: cant find "+q[1]+q[2]+q[4]);}
	    else {SQLBot.bot.update("UPDATE Contracts SET AR_Num='"+e[1]+"' WHERE ID="+e[0]);
		if((""+e[1]).contains("AR")) ars++;
		else nars++;
//update ar numbas

	    }}
	System.out.println("AR #'s added:"+ars+" -- Non-AR #'s added: "+nars);	    
    }

    static private class ARent implements Enterer{
	int arn, tc, sd, fn, ln;
	public ARent(){
	
	    arn = k.dex("AR Num");
	    tc = k.dex(col);
	    sd=k.dex("Start Date");
	    fn=k.dex("First Name");
	    ln=k.dex("Last Name");
	}

	public Object[] editEntry(Object[] o){
//	    System.out.println("does it? "+(o[sd]==null));
	    Integer i = find(o);
		if (i == null) {System.out.println("there"); return null;}
	    else return new Object[]{
		    i, o[arn]
		};
	}

	boolean nameMatch(int cid, String fn, String ln){
	    System.out.println("match");
	    try {
		  // 	System.out.println("SELECT ID FROM Customers WHERE ID="+cid+
		  // " AND First_Name LIKE '%"+fn.trim()+"%' AND Last_Name LIKE '%"+ln.trim()+"%'");
		SQLBot.bot.query1Int("SELECT ID FROM Customers WHERE ID="+cid+
			   " AND First_Name LIKE '%"+fn.trim()+"%' AND Last_Name LIKE '%"+ln.trim()+"%'");

		return true;}
	    catch (SQLException e){System.out.println("FINE:"+e); return false;}
	}

	Integer find(Object[] o){
//get id w/ same contract & start date
	    try {
		
	    ResultSet rs = SQLBot.bot.query("SELECT ID FROM Contracts WHERE Start_Date='"+o[sd]+
					    "' AND abs("+sqlCol+"-"+o[tc]+") <= 0.001");
	    //System.out.println("SELECT Customer_ID FROM Contracts WHERE Start_Date='"+o[sd]+
	    //		    "' AND abs("+sqlCol+"-"+o[tc]+") <= 0.001");
	    //check if it matches name
	    ArrayList<Integer> acc = new  ArrayList<Integer>();
	    while(rs.next()){
		acc.add(rs.getInt(1));}

	    for (int val: acc) if (nameMatch(val, (String)o[fn], (String)o[ln])) return val;

	    } catch (Exception e){e.printStackTrace();}
	    return null;
	}
    }

    public static void main(String[] args) throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");
	du(true);
	du(false);

		

    }
}
