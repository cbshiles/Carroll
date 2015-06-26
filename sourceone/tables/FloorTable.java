package sourceone.tables;
import javax.swing.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class FloorTable extends Table{

    static final Column[] columns = new Column[] {
	new Column("ID", Type.INT),
	new Column("Purchase Date", Type.DATE),
	new Column("VIN", Type.STRING),
	new Column("Vehicle", Type.STRING),
	new Column("Cost", Type.FLOAT),
	new Column("Title", Type.INT),
	new Column("Date Paid", Type.DATE)};

    public FloorTable(){
	super("Cars", columns);
    }

    public JTable makeFloorTable() throws Exception {
	List<String> heads = new ArrayList<String>(Arrays.asList(getHeads()));
	
	heads.remove(0); //ID

	heads.add(3, "Daily Rate");
	heads.add(4, "Title");
	heads.add(6, "Days Active");
	heads.set(7, "Accrued intrest");
	heads.set(8, "Fees");
	heads.add("Subtotal");

	List<List<Object>> lzt = readAll();

	Object[][] objs = new Object[lzt.size()][heads.size()];

	int i = 0;
	float totalInventory=0f, total=0f;
	for (List<Object> slzt : lzt){
	    slzt.remove(0); //ID
	    LocalDate bought = (LocalDate)slzt.get(0);
	    slzt.set(0, princ(bought));
	    float cost = (float)slzt.get(3);
	    slzt.set(3, princ(cost));
	    float rate = cost*.0007f;
	    slzt.add(3, princ(rate));
	    
	    int title = (int)slzt.get(5);
	    String status = (title == 0)?"Pending":"Burritos";
	    slzt.add(4, status);

	    LocalDate paidDate;
	    Object d = slzt.get(7);

	    if (d != null)
		paidDate = (LocalDate)d;
	    else
		paidDate = LocalDate.now();



	    System.out.println(princ(bought)+"\n"+princ(paidDate)+"\n"+paidDate.compareTo(bought)+"\n");

	    int elapsed = (int) bought.until(paidDate, ChronoUnit.DAYS);
	    slzt.add(6, elapsed);

	    float intrest = rate*elapsed;
	    slzt.set(7, princ(intrest));

	    float fees = 25f;
	    slzt.set(8, princ(fees));

	    float sub = cost+intrest+fees;
	    slzt.add(princ(sub));
	    
	    objs[i] = slzt.toArray();

	    totalInventory += cost;
	    total += sub;
	    i++;
	}
	System.out.println(""+totalInventory+"\n"+total);
	return new JTable(objs, heads.toArray());
    }

}
