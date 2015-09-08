package sourceone.pages.blobs;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurBlob extends Blob implements Enterer{

    int ln, fn, sm, dt;
    Key r;
    String summer, date, clz;
    public PurBlob(boolean full){
	summer = "Net Amount";
	date = "Date Bought";
	r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	k = custKey.add(r.cuts);
	ln = k.dex("Last Name");
	fn = k.dex("First Name");
	sm = k.dex(summer);
	dt = k.dex(date);

	String op = full?">":"<";
	clz = "Total_Contract "+op+" 0.01";
    }

    public Enterer ent(){return this;}

    public Input in(LocalDate a, LocalDate z)throws Exception{
	String x = sql(date);
	return new QueryIn
	    (custKey, r,
	     "WHERE Contracts."+x+" >= '"+a+
	     "' AND Contracts."+x+" <= '"+z+
	     "' AND Contracts.Customer_ID = Customers.ID AND "+clz+" ORDER BY "+x);
    }

    public Object[] editEntry(Object[] o){
	    
	return new Object[] {
	    o[dt],
	    "New contract: "+o[ln]+", "+o[fn],
	    o[sm],
	    0f,
	    0f
	};
    }
}
