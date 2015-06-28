package sourceone.key;

public abstract class View extends Matrix{

    public View(Key k, Output o){
	super(k, o);
    }

    public View(Key k, View v){
	super(k, v);
    }

    public abstract void receiveEntry(Object[] objs) throws InputXcpt;
}
