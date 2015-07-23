package sourceone.key;
import java.util.*;

public class Grid extends Matrix{

    public Input in;

    public Grid(Key k, Input i){
	super(k);
	in = i;
	key.setInput(in);
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

}
