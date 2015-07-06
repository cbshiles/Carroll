package sourceone.key;

import static sourceone.key.Kind.*;
import java.util.HashMap;

public class Key{
    public Cut[] cuts;
    public int length;
    public String name = null;
    
    public Key(Cut[] c){cuts = c; length = cuts.length; fillMap();}

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

    public String sqlNames(){
	String keys = "";
	boolean first = true;
	for (Cut c : cuts){
	    if (! first) keys += ", ";
	    else first = false;
	    keys += c.sqlName;
	}
	return keys;
    }

    public Key just(int[] dex){
	Cut[] cs = new Cut[dex.length];
	for (int i=0; i<dex.length; i++)
	    cs[i] = cuts[dex[i]];
	return new Key(cs);
    }

    public Key except(int[] dex){
	//assumes indices to be in order
	Cut[] cs = new Cut[cuts.length - dex.length];
	int i, j, n;
	for (i=j=n=0; i<cuts.length; i++){
	    while (i > dex[j] && j+1 < dex.length)
		j++;
	    if (i != dex[j])
		cs[n++] = cuts[i];
	}

	return new Key(cs);
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

    public Key except(String[] names){
	return except(populate(names));
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
    
    public static final Key contractKey = new Key("Contracts",
	new String[]{"ID", "First Name", "Last Name", "Address", "Phone Number",
		     "Number of Payments", "Amount", "Final Payment",
		     "Payment Frequency", "Total of Payments", "Start Date", "Vehicle",
		     "VIN", "Payments made", "Paid off day", "Next Due"},
	new Kind[]{INT, STRING, STRING, STRING, STRING,
		   INT, FLOAT, FLOAT,
		   INT, FLOAT, DATE, STRING,
		   STRING, INT, DATE, DATE});

    public static final Key floorKey =  new Key(
	new String[]{"ID", "Date Bought", "Item ID", "Vehicle", "Item Cost", "Title", "Date Paid"},
	new Kind[]{INT, DATE, STRING, STRING, FLOAT, INT, DATE});
}
