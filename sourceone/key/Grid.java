package sourceone.key;
import java.util.*;

public class Grid extends Matrix{

    Input in;

    public Grid(Key k, Input i){
	super(k);
	in = i;
	key.setInput(in);
    }

    public void pull() throws InputXcpt{
	while(in.hasEntries())
	    data.add(key.getEntry());
    }

}
