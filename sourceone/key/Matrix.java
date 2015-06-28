package sourceone.key;
import java.util.*;

public class Matrix{

    List<Object[]> data = new ArrayList<Object[]>();
    Key key;
    Output out = null;
    View view = null;
    boolean toOut;

    public Matrix(Key k, Output o){
	key = k;
	out = o;
	key.setOutput(out);
	toOut = true;
    }

    public Matrix(Key k, View v){
	key = k;
	view = v;
	toOut = false;
    }

    public void push() throws InputXcpt{
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
}
