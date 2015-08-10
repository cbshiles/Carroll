package sourceone.key;
import java.util.*;

public class Grid extends Matrix{

    public Input in;

    public Grid(Key k, Input i){
	super(k);
	in = i;
	key.setInput(in);
    }

    void switchInput(Input i){ key.setInput(in = i);}

    public Grid(Key k){ // the skeezy one; only useful for manual loading
	super(k);
    }

    public void pull() throws InputXcpt{
	data = new ArrayList<Object[]>();
	while(in.hasEntries())
	    data.add(key.getEntry());
	in.done();
    }

    public Object go() throws InputXcpt{
	pull();
	return push();
    }

    public void go1() throws InputXcpt{ //assumes going to a view, also note lack of return value
	pull();
	push1();
    }

    
}
