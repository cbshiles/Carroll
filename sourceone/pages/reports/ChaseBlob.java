package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class ChaseBlob extends Blob implements Enterer{
    String dater;
    int tc, ga, na, db, res, ln, fn;
    Key cokey = Key.contractKey.just(new String[]{"Total Contract", "Gross Amount", "Net Amount", "Date Bought", "Reserve"});
    Key cukey= Key.customerKey.just(new String[]{"First Name", "Last Name"});
    public ChaseBlob(){
	
	k = cukey.add(cokey.cuts);
	dater = "contracts.Date_Bought";
	System.out.println(k.sqlNames());
	tc = k.dex("Total Contract");
	ga = k.dex("Gross Amount");
	na = k.dex("Net Amount");
	db = k.dex("Date Bought");
	res = k.dex("Reserve");
	ln = k.dex("Last Name");
	fn = k.dex("First Name");
    }

    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn(cukey, cokey, "WHERE "+dater+" >= '"+a+
			   "' AND "+dater+" <= '"+z+"' AND Contracts.Customer_ID = Customers.ID");
    }
    
    public Enterer ent(){return this;}

    public Object[] editEntry(Object[] o){
	float tot = (float)o[tc];
	float tep = (tot > .01)?tot:(float)o[ga];
	float Ores =  (float)o[res];
	float Ona = (float)o[na];
	return new Object[]{
	    o[db],
	    ""+o[ln]+", "+o[fn],
	    Ona,
	    Ores,
	    tep - (Ores+Ona),
	    tep,
	    0f
	};
    }
}
