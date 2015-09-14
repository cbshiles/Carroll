package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class PurchaseReport extends CenterFile{

    public static Key protKey = new Key(new Cut[]{new StringCut("Date"), new StringCut("Name"), new StringCut("Total Contract"), new StringCut("Reserve"), new StringCut("Purchase Amount"), new StringCut("Gross Profit")});

    @Override
    public Key sendKey(){return protKey;}

    @Override
    public Enterer sendEnt(){ return new Enterer(){
	    public Object[] editEntry(Object[] o){
		return o;
	    }
	};}

    public PurchaseReport (Page p){
	super("Purchase Report", p, new Account[]{new PurCatchAccount()}); 
    }
}
