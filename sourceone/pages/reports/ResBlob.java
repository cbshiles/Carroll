package sourceone.pages.reports;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

public class ResBlob extends Blob{//reserve blob

    private boolean incoming; //true if blob is dealing with new entries to reserve
    private String date, sqlDate;
    private Key contKey;
    
    public ResBlob(boolean inc){
	incoming = inc;
	date = inc?"Date Bought":"Paid Off";
	sqlDate = date.trim().replaceAll("\\s", "_");
	contKey = Key.contractKey.just(new String[] {"Reserve", date});
	k = custKey.add(contKey.cuts);
    }

    public Enterer ent(){
	return new REnt();
    }

    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn
	    (custKey, contKey,
	     "WHERE Contracts."+sqlDate+" >= '"+a+
	     "' AND Contracts."+sqlDate+" <= '"+z+
	     "' AND Contracts.Customer_ID = Customers.ID AND Reserve > 0.01;");
    }

    private class REnt implements Enterer{
	int ln, fn, res, dt;
	
	public REnt(){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    dt = k.dex(date);
	}
	
	public Object[] editEntry(Object[] o){
	    
	    return new Object[] {
		o[dt],
		""+o[ln]+", "+o[fn],
		incoming?0f:o[res],
		incoming?o[res]:0f,
		0f
	    };
	}
    }
}
    
