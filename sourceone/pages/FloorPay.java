package sourceone.pages;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class FloorPay extends Page {

    public FloorPay(){
	super("Discotech");
	setSize(600,600);
	try {
	    Key key = Key.floorKey.except(new int[]{0,2,5,6});

	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title=1");

	    Grid g = new Grid(key, in);
	    g.pull();

	    View v = g.addView(null, new Cut[]{new FloatCut("Daily Rate"), new IntCut("Days Active"),
					       new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		new Enterer(){
		    public Object[] editEntry(Object[] o){
			LocalDate bot = (LocalDate)o[0];
			float cost = (float)o[2];

			float dRate = cost*.0007f; 
			int days = (int)ChronoUnit.DAYS.between(bot, LocalDate.now());

			float interest = dRate*days;
			float fees = 25;
			return new Object[]{
			    dRate,
			    days,
			    interest,
			    fees,
			    cost+interest+fees
			};
		    }
		});

	    g.push();
	    JTable jt = v.getTable();
	    JPanel jp = new JPanel(new BorderLayout());
	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);
	    setContentPane(jp);
	} catch (Exception e)
	{System.err.println("FloorPay error: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }
}

				       //This is for FloorPlan
				       //float cost = (float)o[9];
				       //float dRate = cost*.0007f;
				       //ans[3] = dRate;
//				       LocalDate last;
// new FloatCut("Daily rate"), new IntCut("Days Active"), new FloatCut("Accrued Interest")},
				       // if (o[14] != null)
				       // 	   last = (LocalDate)o[14];
				       // else
				       // 	   last = today;

				       // System.out.println("last: "+last);
				       // 
				       // ans[4] = days;

				       // ans[5] = dRate*days;
// \public class FloorTable extends Table{

//     static final Column[] columns = new Column[] {
// 	new Column("ID", Type.INT),
// 	new Column("Purchase Date", Type.DATE),
// 	new Column("VIN", Type.STRING),
// 	new Column("Vehicle", Type.STRING),
// 	new Column("Cost", Type.FLOAT),
// 	new Column("Title", Type.INT),
// 	new Column("Date Paid", Type.DATE)};

//     public FloorTable(){
// 	super("Cars", columns);
//     }

//     public JTable makeFloorTable() throws Exception {
// 	List<String> heads = new ArrayList<String>(Arrays.asList(getHeads()));
	
// 	heads.remove(0); //ID

// 	heads.add(3, "Daily Rate");
// 	heads.add(4, "Title");
// 	heads.add(6, "Days Active");
// 	heads.set(7, "Accrued intrest");
// 	heads.set(8, "Fees");
// 	heads.add("Subtotal");

// 	List<List<Object>> lzt = readAll();

// 	Object[][] objs = new Object[lzt.size()][heads.size()];

// 	int i = 0;
// 	float totalInventory=0f, total=0f;
// 	for (List<Object> slzt : lzt){
// 	    slzt.remove(0); //ID
// 	    LocalDate bought = (LocalDate)slzt.get(0);
// 	    slzt.set(0, princ(bought));
// 	    float cost = (float)slzt.get(3);
// 	    slzt.set(3, princ(cost));
// 	    float rate = cost*.0007f;
// 	    slzt.add(3, princ(rate));
	    
// 	    int title = (int)slzt.get(5);
// 	    String status = (title == 0)?"Pending":"Burritos";
// 	    slzt.add(4, status);

// 	    LocalDate paidDate;
// 	    Object d = slzt.get(7);

// 	    if (d != null)
// 		paidDate = (LocalDate)d;
// 	    else
// 		paidDate = LocalDate.now();



// 	    System.out.println(princ(bought)+"\n"+princ(paidDate)+"\n"+paidDate.compareTo(bought)+"\n");

// 	    int elapsed = (int) bought.until(paidDate, ChronoUnit.DAYS);
// 	    slzt.add(6, elapsed);

// 	    float intrest = rate*elapsed;
// 	    slzt.set(7, princ(intrest));

// 	    float fees = 25f;
// 	    slzt.set(8, princ(fees));

// 	    float sub = cost+intrest+fees;
// 	    slzt.add(princ(sub));
	    
// 	    objs[i] = slzt.toArray();

// 	    totalInventory += cost;
// 	    total += sub;
// 	    i++;
// 	}
// 	System.out.println(""+totalInventory+"\n"+total);
// 	return new JTable(objs, heads.toArray());
//     }

// }
