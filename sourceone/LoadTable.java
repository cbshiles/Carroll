package sourceone;

import java.util.HashMap;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.key.*;

import java.sql.*;

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
	v.addOut(new SQLFormatter(new InsertDest(v.key, m.ik.name)));
	g.go();
    }

    private static void add(Model m){
	map.put(m.name, m);
    }

    private static void load(){
	add(new Model(Key.floorKey.except(new int[]{0,5,6}), null, new Cut[]{new IntCut("Title")},
		      new Enterer(){
			  public Object[] editEntry(Object[] objs){
			      return new Object[] {0};
			  }
		      }));

	Key fKey = Key.contractKey.just(new String[]{"Total Contract", "Payment Frequency", "Reserve",
						     "Net Amount", "Gross Amount", "Payments Made", "Start Date",
						     "Next Due", "Amount of Payment", "Number of Payments"});

	add(new Model("full", fKey, null, new Cut[]{new FloatCut("Final Payment Amount"), new StringCut("VIN"),
						    new IntCut("Customer ID")}, new FullEnt(fKey)));

	add(new Model(Key.customerKey.just(new String[] {"Last Name", "First Name"}), null, null, null));

	Key pKey = Key.contractKey.just(new String[]{"Payment Frequency", "Net Amount", "Gross Amount",
						     "Payments Made", "Start Date",
						     "Next Due", "Amount of Payment", "Number of Payments"});

	add(new Model("partial", pKey, null, new Cut[]{new FloatCut("Reserve"), new FloatCut("Final Payment Amount"), new StringCut("VIN"),
						       new IntCut("Customer ID"), new FloatCut("Total Contract")}, new PartEnt(pKey)));
    }

    private static class PartEnt implements Enterer{

	int sd, aop, nop, grs;
	int i;

	public PartEnt(Key k){
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    grs = k.dex("Gross Amount");

	    try {
		i = SQLBot.bot.query1Int("SELECT MAX(id) FROM Customers");
	    } catch (SQLException e){
		System.err.println("RRT: "+e);
		System.exit(1);
	    }
	}
	    
	public Object[] editEntry(Object[] o){
	    float fpa = (float)o[grs] - (float)o[aop]*(int)o[nop];
	    String vin = "";
	    int cid = ++i;
	    return new Object[]{
		0f, //reserve
		fpa, vin, cid,
		0f //total contract
	    };
	}
    }

    //# Assumes we're adding these before anything else (because of customer id names)
    private static class FullEnt implements Enterer{

	int sd, aop, nop, tc, res;
	int i;

	public FullEnt(Key k){
	    sd = k.dex("Start Date");
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    tc = k.dex("Total Contract");
	    res = k.dex("Reserve");

	    try {
		i = SQLBot.bot.query1Int("SELECT MAX(id) FROM Customers");
	    } catch (SQLException e){
		System.err.println("RRT: "+e);
		System.exit(1);
	    }
	}
	    
	public Object[] editEntry(Object[] o){
	    float fpa = (float)o[tc] - (float)o[aop]*(int)o[nop];
	    //System.out.println(""+fpa+" = "+(float)o[tc]+" - "+(float)o[aop]+"*"+(int)o[nop]);
	    String vin = "";
	    int cid = ++i;
	    return new Object[]{
		fpa, vin, cid
	    };
	}
    }




/*), new String[]{"First Name"},
  new Cut[]{new StringCut("First Name"), new StringCut("Last Name")},
  new Enterer(){
  public Object[] editEntry(Object[] o){
  String fullName = ""+o[0];
  return new Object[] {names[1].trim(), names[0].trim()};
  }
  }));	
  } */

    
    private static class Model{
	public Key ik;
	String[] remove;
	Cut[] gnu;
	Enterer ent;
	public String name;

	public Model(Key ik, String[] remove, Cut[] gnu, Enterer ent){
	    this.ik = ik;
	    name = ik.name;
	    this.remove = remove;
	    this.gnu = gnu;
	    this.ent = ent;
	}

	public Model(String name, Key ik, String[] remove, Cut[] gnu, Enterer ent){
	    this(ik, remove, gnu, ent);
	    this.name = name;
	}
    }
}
