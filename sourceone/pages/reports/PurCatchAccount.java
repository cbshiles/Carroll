package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurCatchAccount extends Account{

    public PurCatchAccount(){super("Purchases", new Blob[]{/*new FloorBlob(true),*/ new ChaseBlob()}, Key.protKey, false, true);} 

    public float getStart(LocalDate ld)throws Exception{
	return 0f;
    }

    @Override
    public Object[] makeChunk(View tv, String m){return null;}

    @Override
    public Object[] makeChunk(LocalDate z, View v ){
	total = v.floatSum("Total Contract");
	return new Object[]{z, "Ending Balance", v.floatSum("Purchase Amount"), v.floatSum("lnkreserve"), v.floatSum("srcreserve"), v.floatSum("Gross Profit"), total, total};
    }    
}
