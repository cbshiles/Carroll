package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;

public class BotTrax extends Blob{//bought contracts
    
    public BotTrax(){super(kn);}
    public Enterer ent(){return new Nnt(kn);}
    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn
	    (custKey, contNKey,
	     "WHERE Contracts.Date_Bought >= '"+a+
	     "' AND Contracts.Date_Bought <= '"+z+
	     "' AND Contracts.Customer_ID = Customers.ID AND Reserve > 0.01;");
    }

    public float getStart(LocalDate ld){return 0f;}

    public static class Nnt implements Enterer{
	int ln, fn, res, sd;
	
	public Nnt(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    sd = k.dex("Date Bought");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[sd],
		""+o[ln]+", "+o[fn],
		0f,
		o[res],
		0f
	    };
	}
    }
}
