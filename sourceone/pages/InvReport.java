package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class InvReport extends CenterFile{

    public static Key cba = new Key(new Cut[]{ //general key for any credit/debit report
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut("Principle"),
	    new FloatCut("Interest"),
	    new FloatCut("Total Amount"),
	    new FloatCut("A"),
	    new FloatCut("Balance")
	});
    
    public static Key abc = new Key(new Cut[]{new StringCut("A"), new StringCut("B"), new StringCut("C"), new StringCut("D"), new StringCut("E"), new StringCut("F"), new StringCut("G")});
    
    public InvReport (Page p){
	super("Inventory Report", p, new Account[]{
		new PayAccount(true), new PayAccount(false), new PurCatchAccount()}); 
    }

    @Override
    public Key sendKey(){ return abc;}

    @Override
    protected Enterer sendEnt(){return new StrZnt();}
    
    private class StrZnt implements Enterer{

	public Object[] editEntry(Object[] o){

	    String[] f = new String[5];
	    String d= BasicFormatter.cinvert((LocalDate)o[0]);

	    for (int x=0; x<5;x++){
		float z = (float)o[x+2];
		f[x] = (Math.abs(z)>.01)?""+z:"";
	    }	
	    return new Object[]{
		d, ""+o[1], ""+f[0], ""+f[1], ""+f[2], ""+f[3], ""+f[4]
	    };
	}
    }
    
}
