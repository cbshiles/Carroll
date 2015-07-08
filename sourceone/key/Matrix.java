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

    public View addView(String[] remove, Cut[] gnu, Enterer ent){
	int[] rem = key.except(remove);
	View v = new View(key.except(rem).add(gnu), rem, ent);
	addOut(v);
	return v;
    }

    public void addTable(){//throws InputXcpt{
	addOut(new BasicFormatter(new TableDest(key)));
    }
}
