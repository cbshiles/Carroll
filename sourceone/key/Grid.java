package sourceone.key;
import java.util.*;

public class Grid extends Matrix{

    Input in;

    public Grid(Key k, Input i, Output o){
	super(k, o);
	in = i;
	key.setInput(in);
    }

    public Grid(Key k, Input i, View v){
	super(k, v);
	in = i;
	key.setInput(in);
    }

    public void pull() throws InputXcpt{
	while(in.hasEntries())
	    data.add(key.getEntry());
    }
}
