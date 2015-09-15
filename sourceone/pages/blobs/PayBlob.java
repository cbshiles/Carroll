package sourceone.pages.blobs;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PayBlob extends Blob implements Enterer{
    boolean full;
    String summer, date, clz;
    int ln, fn, sm, dt;
    static PayInFact pif = new PayInFact();
    
    public PayBlob(boolean f){
	full = f;
	// summer = "Net Amount";
	// date = "Date Bought";
	// // Key r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	// // k = custKey.add(r.cuts);
	// k = Key.sumKey;
	// ln = k.dex("Last Name");
	// fn = k.dex("First Name");
	// sm = k.dex(summer);
	// dt = k.dex(date);
	k = Key.sumKey;

	// String op = full?">":"<";
	// clz = "Total_Contract "+op+" 0.01";
    }

    public Enterer ent(){return this;}
	
    public Input in(LocalDate a, LocalDate z)throws Exception{
	return pif.in(a, z, full);
    }

    public Object[] editEntry(Object[] o){
	int x = 0;
	return new Object[]{
	    o[x++],
	    o[x++],
	    o[x++],
	    o[x++],
	    o[x++],
	    0f,
	    0f
	};
    }
}

