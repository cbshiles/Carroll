import javax.swing.*;
import java.util.*;
import java.time.*;

public class ContractTable extends Table{

    static final Column[] columns = new Column[] {
	new Column("ID", 1),
	new Column("First Name", 0),
	new Column("Last Name", 0),
	new Column("Address", 0),
	new Column("Phone Number", 0),
	new Column("Number of Payments", 1),
	new Column("Amount", 2),
	new Column("Final Payment", 2),
	new Column("Payment Frequency", 1),
	new Column("Total of Payments", 2),
	new Column("Start Date", 3),
	new Column("Vehicle", 0),
	new Column("VIN", 0)};


    public ContractTable(SQLBot bot){
	super("Contracts", columns, bot);
    }

    public JTable makePayTable() throws Exception{
	List<String> heads = Arrays.asList(getHeads());

	String[] str = new String[]{"Customer Name", "Terms", "Start Date", "Due Date"};

	List<List<Object>> lzt = readDB();

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
