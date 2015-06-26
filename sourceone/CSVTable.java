import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.swing.*;

public class CSVTable extends Table {

    Object[][] objs;
    String[] heads;

    static final Column[] columns = new Column[] {
//	new Column("ID", Type.INT), //automatic, can be ignored on incoming
	new Column("Date Bought", Type.DATE),
	new Column("Item ID", Type.STRING),
	new Column("Vehicle", Type.STRING),
	new Column("Item Cost", Type.FLOAT)};

        public CSVTable(){
	super("Jars", columns);
    }

    public JTable makeTable() throws Exception{
	dew();
	System.out.println("A");
	List<List<Object>> lzt = readAll();
	List<String> names = new ArrayList<String>(Arrays.asList(getHeads()));
		System.out.println("B");
	names.add(0, "ID");
		System.out.println("C");
	return new JTable(d2Converter(lzt), names.toArray());
    }

    public void dew() throws Exception{
	List<List<String>> lzt = CSVReader.read("../../csv/good.csv");

	for(int i=0; i<lzt.size(); i++){
	    List<String> slzt = lzt.get(i);
	    bot.insertInit(name);
	    for (int j=0; j<columns.length; j++){
		Column col = columns[j];
		String s = slzt.get(j);
		switch(col.type) {

		 case STRING:
		     bot.insertAdd(col.sqlName, bot.toSQL(getStr(s))); break;
		 case INT:
		     bot.insertAdd(col.sqlName, bot.toSQL(getInt(s))); break; 
		 case FLOAT:
		     bot.insertAdd(col.sqlName, bot.toSQL(getFloat(s))); break;
		 case DATE:
		     bot.insertAdd(col.sqlName, bot.toSQL(getDate(s))); break;
		 default:
		     throw new InputXcpt("Invalid column type");
		}
	    }
	    bot.insertSend();
	}
	for (List<String> zt : lzt){
	    for (String str : zt){
		System.out.print(str+" ");
	    }
	    System.out.println();
	}
    }
    
    public String getStr(String raw){
	return raw;
    }

    public int getInt(String raw)throws InputXcpt{
	try {return Integer.parseInt(raw);}
	catch (NumberFormatException e) {throw new InputXcpt(raw, e.getMessage()+"\nCouldn't parse as int");}
    }

    public int getInt(String raw, int l, int h)throws InputXcpt{
	int x = getInt(raw);
	if (x < l || x > h) throw new InputXcpt(raw, x+" is not between "+l+" and "+h);
	return x;
    }

    public float getFloat(String raw)throws InputXcpt{
	try {return Float.parseFloat(raw);}
	catch (NumberFormatException e) {throw new InputXcpt(raw, e.getMessage()+"\nCouldn't parse as float");}
    }

    public float getFloat(String raw, int l, int h)throws InputXcpt{
	float x = getFloat(raw);
	if (x < l || x > h) throw new InputXcpt(raw, x+" is not between "+l+" and "+h);
	return x;
    }

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");

    public LocalDate getDate(String raw) throws InputXcpt{
	String dStr = raw;
	dStr = dStr.replaceAll("/", "-");
	try {
	return LocalDate.parse(dStr, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(raw, dStr, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }


}
