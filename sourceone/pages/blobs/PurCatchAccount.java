package sourceone.pages.blobs;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurCatchAccount extends CenterFile.Account{

    public PurCatchAccount(){super("Purchases", new Blob[]{/*new FloorBlob(true),*/ new ChaseBlob()});} 

    public float getStart(LocalDate ld)throws Exception{
	return 0f;
    }

    @Override
    	public View span(LocalDate a, LocalDate z) throws Exception{//can return null
	    if (a.isAfter(z)) return null;

	    View v = new View(PurchaseReport.protKey, null, null); // will need to change if ++ columns

//	    v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, 0f});
	
	    for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
		LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
		nz = nz.isBefore(z)?nz:z;

		View vi = new View(PurchaseReport.protKey);
		vi.addOut(v);
		for (Blob b: blobs){// tip: you need to use an input before making a new one
		    Grid g = new Grid(b.k, b.in(n, nz));
		    g.addOut(vi);
		    vi.switchEnts(b.ent());
		    g.go1();
		}
		vi.sort("Date", true);
		vi.push1();

		//	public Key sendKey(){ return new Key(new Cut[]{new StringCut("Date"), new StringCut("Name"), new StringCut("Total Contract"), new StringCut("Reserve"), new StringCut("Purchase Amount")});}
		

//		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});
	    }
		// float deb = v.floatSum("Total Contract");
		// float cred = v.floatSum("Reserve");
		v.chunk(new Object[]{z, "Ending Balance", v.floatSum("Purchase Amount"), v.floatSum("Reserve"), v.floatSum("Gross Profit"), v.floatSum("Total Contract"), 0f});

	    return v;
	}

}
