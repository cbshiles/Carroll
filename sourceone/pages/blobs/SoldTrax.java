package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;

public class SoldTrax extends Blob{//bought contracts
    
    public SoldTrax(){super(ko);}
    public Enterer ent(){return new Ont(ko);}
    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn
	    (custKey, contOKey,
	     "WHERE Contracts.Paid_Off >= '"+a+
	     "' AND Contracts.Paid_Off <= '"+z+
	     "' AND Contracts.Customer_ID = Customers.ID AND Reserve > 0.01;");
    }

    public float getStart(LocalDate ld){return 0f;}
	
    public static class Ont implements Enterer{
	int ln, fn, res, po;
	
	public Ont(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    po = k.dex("Paid Off");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[po],
		""+o[ln]+", "+o[fn],
		o[res],
		0f,
		0f
	    };
	}
    }
}
