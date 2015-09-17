package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
//import sourceone.csv.*;
import java.time.*;

import sourceone.pages.*;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class ReserveReport extends Report{

    Grid go;
    View v;

    //# could use a blob for re3serve transactions, right now its aclawys taken from the top

    public ReserveReport(Page p) throws Exception{
	super("Reserve", p, new Account[]{new ResAccount()});
	rKey =  Key.resKey;
	init();
    }

    public static class ResAccount extends Account{

	public ResAccount(){super("Reserve Account", new Blob[]{new ResBlob(true), new ResBlob(false)}, Key.resKey);}

	public float getStart(LocalDate ld) throws Exception{//get beginning balance, starting at ld
	    Key r = Key.contractKey.just("Reserve");
	    Grid g;
	    g = new Grid(r, new QueryIn(r,
					"WHERE Date_Bought < '"+ld+"'")); 
	    g.pull();
	    return resSum()-g.floatSum("Reserve");
	}

	private float resSum(){
	    try{
		return SQLBot.bot.query1Float("SELECT SUM(Amount) FROM reserve");
	    } catch (Exception e){System.out.println("#$%: "+e); return 0;}
	}

	@Override
	public Object[] makeChunk(View tv, String m){
	    float deb = tv.floatSum("Debit");
	    float cred = tv.floatSum("Credit");
	    return new Object[]{null, "Current Period Change: "+m, deb, cred, deb-cred};
	}
    }
}
