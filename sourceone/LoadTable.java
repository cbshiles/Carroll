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
