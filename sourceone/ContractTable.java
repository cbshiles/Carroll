import javax.swing.*;
import java.util.*;
import java.time.*;

public class ContractTable extends Table{

    static final Column[] columns = new Column[] {
	new Column("ID", Type.INT),
	new Column("First Name", Type.STRING),
	new Column("Last Name", Type.STRING),
	new Column("Address", Type.STRING),
	new Column("Phone Number", Type.STRING),
	new Column("Number of Payments", Type.INT),
	new Column("Amount", Type.FLOAT),
	new Column("Final Payment", Type.FLOAT),
	new Column("Payment Frequency", Type.INT),
	new Column("Total of Payments", Type.FLOAT),
	new Column("Start Date", Type.DATE),
	new Column("Vehicle", Type.STRING),
	new Column("VIN", Type.STRING)};


    public ContractTable(){
	super("Contracts", columns);
    }

    public JTable makePayTable() throws Exception{
	List<String> heads = Arrays.asList(getHeads());

	String[] str = new String[]{"Customer Name", "Terms", "Start Date", "Due Date"};

	List<List<Object>> lzt = readAll();

	Object[][] objs = new Object[lzt.size()][str.length];

	int i = 0;
	for (List<Object> slzt : lzt){
	    objs[i][0] = slzt.get(1) + ", " + slzt.get(2);
	    LocalDate start = (LocalDate)slzt.get(10);
	    
	    char ch;
	    int freq = (Integer)slzt.get(8);
	    LocalDate due;
	    
	    if (freq == 30){
		ch = 'M';
		due = start.plusMonths(1);
	    } else {
		if (freq == 7) ch = 'W';
		else if (freq == 14) ch = 'B';
		else throw new Exception("Invalid payment frequency");
		due = start.plusDays(freq);
	    }

	    String terms = slzt.get(5).toString();
	    terms += " "+ch+" @ "+princ((float)slzt.get(6));

	    objs[i][1] = terms;
	    objs[i][2] = princ(start);
	    objs[i][3] = princ(due);

	    i++;
	}
	
	return new JTable(objs, str);
    }

}
