package sourceone.key;

import static sourceone.key.Kind.*;
import java.util.HashMap;

public class Key{
    public Cut[] cuts;
    public int length;
    public String name = null;
    
    public Key(Cut[] c){
	cuts = new Cut[c.length];
	length = c.length;
	int i = 0;
	for (Cut cut : c){
	    cuts[i++] = (Cut)cut.clone();
	}
	    
	fillMap();
    }

    public Key(String[] names, Kind[] types){
	cuts = new Cut[names.length];
	
	for (int i=0; i<names.length; i++){
	    switch(types[i]){
	     case STRING:
		 cuts[i] = new StringCut(names[i]); break;
	     case INT:
		 cuts[i] = new IntCut(names[i]); break;
	     case FLOAT:
		 cuts[i] = new FloatCut(names[i]); break;
	     case DATE:
		 cuts[i] = new DateCut(names[i]); break;
	    }
	}
	length = cuts.length;
	fillMap();
    }

    public Key(String n, String[] names, Kind[] types){
	this(names, types);
	name = n;
    }

    private Key(String n, Cut[] c){
	cuts = c;
	length = c.length;
	fillMap();
	name = n;
    }

    public Object[] getEntry() throws InputXcpt{
	Object[] arr = new Object[cuts.length];
	for (int i=0; i<cuts.length; i++)
	    arr[i] = cuts[i].in();
	return arr;
    }

    public void putEntry(Object[] arr){
	for (int i=0; i<cuts.length; i++)
	    cuts[i].out(arr[i]);
    }

    public void setInput(Input i){
	for (Cut c : cuts)
	    c.ip = i;
    }

    public Object[] chunky(){
	Object[] ret = new Object[cuts.length];
	int i;
	for (i=0; i<cuts.length; i++)
	    ret[i] = cuts[i].name;
	return ret;
    }

    public void setOutput(Output o){
	for (Cut c : cuts)
	    c.op = o;
    }

    public String[] tableNames(){
	String[] ret = new String[cuts.length];
	for (int i=0; i<cuts.length; i++)
	    ret[i] = cuts[i].name;
	return ret;
    }

    public String names(){
	String keys = "";
	boolean first = true;
	for (Cut c : cuts){
	    if (! first) keys += ", ";
	    else first = false;
	    keys += c.name;
	}
	return keys;
    }

    public String csvnames(){
	String keys = "";
	boolean first = true;
	for (Cut c : cuts){
	    if (! first) keys += "~";
	    else first = false;
	    keys += c.name;
	}
	return keys;
    }

    public String sqlNames(){return sqlNames(false);}

    public String sqlNames(boolean lng){
	String keys = "";
	boolean first = true;
	String add = (lng?name+".":"");
	for (Cut c : cuts){
	    if (! first) keys += ", ";
	    else first = false;
	    keys += add+c.sqlName;
	}
	return keys;
    }

    public Key just(int[] dex){
	Cut[] cs = new Cut[dex.length];
	for (int i=0; i<dex.length; i++)
	    cs[i] = cuts[dex[i]];
	return new Key(name, cs);
    }

    public Key accept(String[] dex){ //# make private
	return except(except(dex));
    }

    public Key except(int[] dex){
	if (dex==null) return new Key(cuts);
	//assumes indices to be in order
	Cut[] cs = new Cut[cuts.length - dex.length];
	int i, j, n;
	for (i=j=n=0; i<cuts.length; i++){
	    while (j+1 < dex.length && i > dex[j])
		j++;
	    if (dex.length == 0 || i != dex[j])
		cs[n++] = cuts[i];
	}

	return new Key(name, cs);
    }

    private HashMap<String, Integer> map;
    
    
    private void fillMap(){
	map = new HashMap<String, Integer>();
	for (int i=0; i<cuts.length; i++){
	    map.put(cuts[i].name, i);
	}
    }

    public int dex(String cName){
	Integer i = map.get(cName);
	if (i == null){
	    System.err.println(cName+" is not found in the key mapping!");
	    System.exit(0);
	}
	return i;
    }

    public Key add(Key ok){
	return add(ok.cuts);
    }
    
    public Key add(Cut[] gnu){
	if (gnu == null) return new Key(cuts);
	else {
	Cut[] crew = new Cut[cuts.length + gnu.length];
	System.arraycopy(cuts, 0, crew, 0, cuts.length);
	System.arraycopy(gnu, 0, crew, cuts.length, gnu.length);
	return new Key(crew);
	}
    }

    public int[] except(String[] names){
	if (names == null) return null;
	int[] r = populate(names);
	java.util.Arrays.sort(r);
	return r;
    }

    public Key just(String name){
	return just(new String[]{name});
    }

    public Key just(String[] names){
	return just(populate(names));
    }

    private int[] populate(String[] names){
	int[] ix = new int[names.length];
	for (int i=0; i<names.length; i++){
	    ix[i] = dex(names[i]);
	}
	return ix;
    }

    //All keys must have an ID column (cut).

    public static final Key customerKey = new Key("customers",
						  new Cut[]{
						      new IntCut("ID"),
						      new StringCut(63, "First Name", "NOT NULL"),
						      new StringCut(63, "Last Name", "NOT NULL"),
						      new StringCut(127, "Address"),
						      new StringCut(31, "Phone Number"),
						      new StringCut(63, "email")
						  });
    
    public static final Key contractKey = new Key("contracts",
						  new Cut[]{
						      new IntCut("ID"),
						      new IntCut("Number of Payments", "NOT NULL"),
						      new FloatCut("Amount of Payment", "NOT NULL"),
						      new FloatCut("Final Payment Amount"),
						      new IntCut("Payment Frequency", "NOT NULL"),
						      new FloatCut("Total Contract", "NOT NULL"),
						      new DateCut("Start Date", "NOT NULL"),
//						      new StringCut(127, "Vehicle"),
						      new StringCut(31, "VIN", "NOT NULL"),

						      new FloatCut("Reserve", "NOT NULL"),
						      new FloatCut("Gross Amount", "NOT NULL"),
						      new FloatCut("Net Amount", "NOT NULL"),
						      new FloatCut("Other Payments"),
						      new DateCut("Next Due"),
						      new DateCut("Paid Off"),
						      new IntCut("Payments Made"),
						      new IntCut("Customer ID", "NOT NULL"),
						      new DateCut("Date Bought"),
						      new IntCut("Title"),
						      new StringCut(31, "AR Num")
						      //Date bought is assumed to be the start date, if not explicitly given
						  });

    /*just (						      "ID",
						      "Number of Payments",
						      "Amount of Payment",
						      "Final Payment Amount",
						      "Payment Frequency",
						      "Total Contract", 
						      "Start Date", 
						      "Vehicle",
						      "VIN",

						      "Reserve",
						      "Gross Amount",
						      "Net Amount",
						      "Other Payments",
						      "Next Due",
						      "Paid Off",
						      "Payments Made",
						      "Customer ID",
    */

    public static final Key floorKey =  new Key("cars",
						  new Cut[]{
						    new IntCut("ID"),
						    new DateCut("Date Bought", "NOT NULL"),
						    new StringCut(31, "VIN", "NOT NULL"),
						    new StringCut(127, "Vehicle", "NOT NULL"),
						    new FloatCut("Item Cost", "NOT NULL"),
						    new IntCut("Title", "NOT NULL"),
						    new DateCut("Date Paid"),
						    new FloatCut("Pay Off Amount"),
						    new IntCut("Curtailed"),
						    new StringCut(63, "Auction")
						});
//    .just(new String[]{"ID", "Date Bought","VIN", "Vehicle", "Item Cost", "Title", "Date Paid", "Pay Off Amount"});
    
    public static final Key paymentKey = new Key("payments",
						 new Cut[]{
						     new IntCut("ID"),
						     new IntCut("Contract ID", "NOT NULL"),
						     new DateCut("Day", "NOT NULL"),
						     new FloatCut("Amount", "NOT NULL"),
						     new StringCut(63, "Batch ID")
						 });

    public static final Key metaKey = new Key("meta",
					      new Cut[]{
						  new IntCut("ID"),
						  new DateCut("Full Report Date"),
						  new DateCut("Partial Report Date")
					      });
    public static final Key fKey = Key.contractKey.just(new String[]{"Total Contract", "Payment Frequency", "Reserve",
								     "Net Amount", "Gross Amount", "Payments Made", "Start Date",
								     "Next Due", "Amount of Payment", "Number of Payments"});
    
    public static final Key pKey = Key.contractKey.just(new String[]{"Payment Frequency", "Net Amount", "Gross Amount",
								     "Payments Made", "Start Date",
								     "Next Due", "Amount of Payment", "Number of Payments"});
    public static final Key sumKey = new Key(new Cut[]{ //general key for any credit/debit report
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut("Debit Amt"),
	    new FloatCut("Credit Amt"),
	    new FloatCut("Balance")
	});

    public static final Key zumKey = new Key(new Cut[]{ //general key for any credit/debit report
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut("Debit Amt"),
	    new FloatCut("Credit Amt"),
	    new FloatCut("Balance"),
	    new FloatCut("Discount")
	});

    
    public static final Key reserveKey = new Key("reserve", new Cut[]{
	    new IntCut("ID"),
	    new StringCut(63, "Trans Description"),
	    new DateCut("Date", "NOT NULL"),
	    new FloatCut("Amount", "NOT NULL")
	});

    public static final Key resKey  =  new Key(new Cut[]{
	    new StringCut("Date"), new StringCut("Customer"), new StringCut("Debit"),
	    new StringCut("Credit"), new StringCut("Balance")});   

    public static final Key purKey  =  new Key(new Cut[]{
	    new StringCut("Date"), new StringCut("Customer"), new StringCut("Debit (Purchase Amount)"),
	    new StringCut("Credit"), new StringCut("Balance")});

    public static final Key flKey  =  new Key(new Cut[]{
	    new StringCut("Date"), new StringCut("Customer"), new StringCut("Purchase Amount"),
	    new StringCut("Sale Amount"), new StringCut("")});

    public static final Key payKey = new Key(new Cut[]{ 
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut("Principle"),
	    new FloatCut("Interest"),
	    new FloatCut("Total Amount"),
	    new FloatCut("Inventory Reduction"),
	    new FloatCut("Inventory Balance")
	});

    public static final Key protKey = new Key(new Cut[]{new StringCut("Date"), new StringCut("Name"), new StringCut("Purchase Amount"),  new StringCut("Reserve"), new StringCut("Gross Profit"), new StringCut("Total Contract"), new StringCut("Balance")});

    public static final Key invKey = new Key(new Cut[]{ 
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut(""),
	    new FloatCut(""),
	    new FloatCut(""),
	    new FloatCut(""),
	    new FloatCut("Inventory Balance")
	});

}

