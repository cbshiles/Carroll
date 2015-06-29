package sourceone.key;
import java.util.*;

public class Matrix{

    List<Object[]> data = new ArrayList<Object[]>();
    Key key;
    Output out = null;
    View view = null;
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

	if (! hasOut) throw new InputXcpt("Matrix cant be pushed without an out");
	
	if (toOut){
	    for (Object[] objs : data){
		key.putEntry(objs);
		out.endEntry();
	    }
	} else { //to a view
	    for (Object[] objs : data)
		view.receiveEntry(objs);
	}
    }

    public View addView(int[] remove, Cut[] gnu, Enterer ent){
	int rl = (remove==null)?0:remove.length;
	int gl = (gnu==null)?0:gnu.length;
	int cl = key.length - rl;
		
	Cut[] c = new Cut[cl + gl];

	if (remove != null){
	    int i,j,n;
	    for (i=j=n=0; i<key.cuts.length; i++){
		while (i > remove[j]) j++;
		if (i < remove[j]) c[n++] = key.cuts[i];
		i++;
	    }
	} else System.arraycopy(key.cuts, 0, c, 0, key.length);

	if (gnu != null)
	    System.arraycopy(gnu, 0, c, cl, gnu.length);
	
	View v = new View(new Key(c), remove, ent);
	addOut(v);
	return v;
    }
}
