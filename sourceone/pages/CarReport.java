package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

public class CarReport extends TablePage{

    public CarReport(){
	super("Create Car Report");

	Key inKey = Key.floorKey.accept(new String[]{"ID", "Date Paid"}); //# make an overloaded version for scalars
	try {
	    Input in = new QueryIn(inKey, "WHERE Title<2;");
	    g = new Grid(inKey, in);
	    g.pull();
	    Key entKey = new Key("Cars", new String[]{"Date bought", "VIN", "Vehicle", "Daily Rate", "Title", "Item Cost", "Days active",	"Accrued Interest", "Fees", "Subtotal"}, new Kind[]{DATE, INT, STRING, FLOAT, STRING, FLOAT, INT, FLOAT, FLOAT, FLOAT});
	    g.clearView(entKey.cuts, new Ent(entKey));
	    pushTable();
	} catch (Exception e){System.err.println("***"+e); return;}

	setSize(1000, 600);
	setVisible(true);
    }

    private class Ent implements Enterer{
	Key k;

	int db, vin, veh, ic, ttl;

	public Ent(Key kk){
	    k = kk;
	    db = k.dex("Date Bought");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    ic = k.dex("Item Cost");
	    ttl = k.dex("Title");

	}
	
	public Object[] editEntry(Object[] o){
	    return new Object[]{
		o[db],
		o[vin],
		o[veh],
		0,//Daily rate
		0,//Title
		o[ic],
		0,//Days active
		0, //accrued intrest
		0, //Fees
		0 //Subtotal
	    };
	}
    }

    // 
}
