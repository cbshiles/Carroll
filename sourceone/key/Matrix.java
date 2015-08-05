package sourceone.key;
import java.util.*;
import javax.swing.JTable;

public class Matrix{

    public List<Object[]> data = new ArrayList<Object[]>();
    public Key key;
    Output out = null;
    public View view = null;
    boolean toOut, hasOut = false;

    public Matrix(Key k){key = k;}

    public void addOut(View v){
	view = v;
	out = null;
	toOut = false;
	hasOut = true;
    }

    public void addOut(Output o){
	out = o;
	key.setOutput(out);
	view = null;
	toOut = true;
	hasOut = true;
    }

    public int dexOf(String[] a, String s){
	for (int i=0; i<a.length; i++){
	    if (s == a[i]) return i;
	}
	return -1;
    }

    public void sort(int col, boolean asc){ //what column and order
	String[] tings = new String[data.size()];
	for (int i=0; i<tings.length; i++)
	    tings[i] = data.get(i)[col].toString();
	String[] sorted = Arrays.copyOf(tings, tings.length);
	Arrays.sort(sorted);

	List<Object[]> newData = new ArrayList<Object[]>();

	for (int j=0; j<tings.length; j++){
	    newData.add(data.get(dexOf(tings, sorted[j])));
	}
	data = newData;
    }

    public void push1() throws InputXcpt{ //assumes pushing to a view
	for (Object[] objs : data)
	    view.receiveEntry(objs);
    }

    public Object push() throws InputXcpt{

	if (! hasOut) throw new Error("Matrix cant be pushed without an out");

	if (toOut){
	    for (Object[] objs : data){
		key.putEntry(objs);
		out.endEntry();
	    }
	    return out.close();

	} else { //to a view
	    view.freshen();
	    for (Object[] objs : data)
		view.receiveEntry(objs);
	    return view.push();
	}
    }

    public View clearView(Cut[] gnu, Enterer ent){
	View v = new View(new Key(gnu), ent);
	addOut(v);
	return v;
    }
//# make it to where the ent has a key of its own, which replaces gnu
    public View addView(String[] remove, Cut[] gnu, Enterer ent){
	int[] rem = key.except(remove);
	View v = new View(key.except(rem).add(gnu), rem, ent);
	addOut(v);
	return v;
    }

    public void addTable(){//throws InputXcpt{
	addOut(new BasicFormatter(new TableDest(key)));
    }

    public Object get(String name, int dx){
	return data.get(dx)[key.dex(name)];
    }

    public String toString(){
	String str = key.names()+'\n';
	for (Object[] arr : data)
	    str += games(arr)+'\n';
	return str;
    }

    private String games(Object[] o){
	String keys = "";
	boolean first = true;
	for (Object oo : o){
	    if (! first) keys += ", ";
	    else first = false;
	    keys += oo;
	}
	return keys;
    }
}
