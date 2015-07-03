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

    public void push() throws InputXcpt{
	if (! hasOut) return;//throw new InputXcpt("Matrix cant be pushed without an out");

	if (toOut){
	    for (Object[] objs : data){
		key.putEntry(objs);
		out.endEntry();
	    }
	    out.close();

	} else { //to a view
	    for (Object[] objs : data)
		view.receiveEntry(objs);
	    view.push();
	}
    }

    public View clearView(Cut[] gnu, Enterer ent){
	int[] is = new int[key.cuts.length];
	for (int i=0; i<key.cuts.length; i++){
	    is[i] = i;
	}
	return addView(is, gnu, ent);
    }

    public View addView(int[] remove, Cut[] gnu, Enterer ent){
	int rl = (remove==null)?0:remove.length;
	int gl = (gnu==null)?0:gnu.length;
	int cl = key.length - rl;

	Cut[] c = new Cut[cl + gl];

	if (remove != null){
	    int i,j,n;
	    for (i=j=n=0; i<key.cuts.length; i++){
		while (i > remove[j] && j<rl-1) j++;
		if (i != remove[j]) c[n++] = key.cuts[i];
	    }
	} else System.arraycopy(key.cuts, 0, c, 0, key.length);

	if (gnu != null)
	    System.arraycopy(gnu, 0, c, cl, gnu.length);

	View v = new View(new Key(c), remove, ent);
	addOut(v);
	return v;
    }

    public JTable getTable()throws InputXcpt{
	JTable jt = new JTable();
	TableDest td = new TableDest(key, data.size());
	addOut(new Formatter(td));
	push();

	return td.getTable();
    }
}
