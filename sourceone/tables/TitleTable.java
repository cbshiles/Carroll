package sourceone.tables;
import javax.swing.*;
import sourceone.*;

public class TitleTable extends Table {

    Object[][] objs;

    static final Column[] columns = new Column[] {
	new Column("ID", Type.INT),
	new Column("Date Bought", Type.DATE),
	new Column("Item ID", Type.STRING),
	new Column("Vehicle", Type.STRING),
	new Column("Item Cost", Type.FLOAT)};

    public TitleTable(){
	super("Cars", columns);
    }

    public JTable makeTable() throws Exception{
	objs = d2Converter(readCols("WHERE Title=0"));
	JTable jt = new JTable(objs, getHeads());
	return jt;
    }

    public void giveTitle(int[] rows) {
	try {
	    for (int i=0; i<rows.length; i++){
		int z = (int)objs[rows[i]][0];
		bot.update("UPDATE "+name+" SET Title=1 WHERE ID="+z);
	    }
	} catch (Exception e)
	{System.err.println("YO: "+e.getCause()+e.getClass().getName());}
    }

}
