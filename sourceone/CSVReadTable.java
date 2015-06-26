import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.swing.*;

public class CSVReadTable extends Table{
    Object[][] objs;
    String[] heads;

    static final Column[] columns = new Column[] {
	new Column("ID", Type.INT), //automatic, can be ignored on incoming
	new Column("Date Bought", Type.DATE),
	new Column("Item ID", Type.STRING),
	new Column("Vehicle", Type.STRING),
	new Column("Item Cost", Type.FLOAT)};

    public CSVReadTable(){
	super("Jars", columns);
    }
    public JTable makeTable() throws Exception{
	CSVTable v = new CSVTable();
	v.dew();
	System.out.println("A");
	List<List<Object>> lzt = readAll();
	List<String> names = new ArrayList<String>(Arrays.asList(getHeads()));
	System.out.println("B");
//	names.add(0, "ID");
	System.out.println("C");
	return new JTable(d2Converter(lzt), names.toArray());
    }

}
