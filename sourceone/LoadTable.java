package sourceone;

import java.util.HashMap;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.key.*;

public class LoadTable{
    static HashMap<String, Model> map = new HashMap<String, Model>();
    public static void main(String[] args) throws Exception{
	if (args.length != 2)
	    throw new InputXcpt("All I need is the key and the file name");

	SQLBot.bot = new SQLBot("res/db.properties");
	load();

	String mod = args[0];
	String fpath = args[1];
	Model m = map.get(mod);

	if (m == null) throw new InputXcpt("Key name not found");
	
	Grid g = new Grid(m.ik, new StringIn(new CSVInput(fpath)));
	View v = g.addView(m.remove, m.gnu, m.ent);
	v.addOut(new SQLFormatter(new InsertDest(v.key, mod)));
	g.go();
    }

    private static void add(Model m){
	map.put(m.name(), m);
    }

    private static void load(){
	add(new Model(Key.floorKey.except(new int[]{0,5,6}), null, new Cut[]{new IntCut("Title")},
		      new Enterer(){
			  public Object[] editEntry(Object[] objs){
			      return new Object[] {0};
			  }
		      }));

	//# Assumes we're adding these before anything else (because of customer id names)
	add(new Model(Key.contractKey.except(new String[]{"ID", "Customer ID"}), null, new Cut[]{new IntCut("Customer ID")},
		      new Enterer(){
			  int i = 1;
			  public Object[] editEntry(Object[] objs){
			      return new Object[] {i++};
			  }
		      }));

	add(new Model(Key.customerKey.except(new String[]{"ID", "Customer ID"}), null, new Cut[]{new IntCut("Customer ID")},
		      new Enterer(){
			  int i = 1;
			  public Object[] editEntry(Object[] objs){
			      return new Object[] {i++};
			  }
		      }));	
    }

    private static class Model{
	Key ik;
	String[] remove;
	Cut[] gnu;
	Enterer ent;

	public Model(Key ik, String[] remove, Cut[] gnu, Enterer ent){
	    this.ik = ik;
	    this.remove = remove;
	    this.gnu = gnu;
	    this.ent = ent;
	}

	public String name()
	{return ik.name;}
    }
}
