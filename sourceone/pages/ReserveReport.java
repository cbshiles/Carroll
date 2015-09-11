package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
//import sourceone.csv.*;
import java.time.*;

import sourceone.pages.blobs.*;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class ReserveReport extends CenterFile{

    Grid go;
    View v;
    
    public ReserveReport(Page p) throws Exception{
	super("Reserve", p, new Account[]{new ResAccount()});
	dew();
    }

    public static class ResAccount extends Account{

	public ResAccount(){super("Reserve Account", new Blob[]{new ResBlob(true), new ResBlob(false)});}

	public float getStart(LocalDate ld) throws Exception{//get beginning balance, starting at ld
	    Key r = Key.contractKey.just("Reserve");
	    Grid g;
	    g = new Grid(r, new QueryIn(r,
					"WHERE Date_Bought < '"+ld+"'")); //AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld+"' );"));
	    g.pull();
	    return resSum()-g.floatSum("Reserve");
	}

	private float resSum(){
	    try{
	    return SQLBot.bot.query1Float("SELECT SUM(Amount) FROM reserve");
	    } catch (Exception e){System.out.println("#$%: "+e); return 0;}
	}
    }
    
	@Override
	public Key sendKey(){ return new Key(new Cut[]{new StringCut("Date"), new StringCut("Transaction"), new StringCut("Debit"), new StringCut("Credit"), new StringCut("Balance")});}

    
}
