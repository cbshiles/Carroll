package sourceone.key;

//Any new cuts are to be added to the key before the reach a view constructor
//This feature is in Matrix's makeView

public class View extends Matrix{

    Enterer ent;
    int[] remove;
    
    public View(Key k, int[] r, Enterer e){
	super(k);
	remove = r;
	ent = e;
    }

    public void receiveEntry(Object[] objs){
	
	Object[] arr = new Object[key.length];

	int n;
	if (remove != null){
	    int i,j;
	    for (i=j=n=0; i<objs.length; i++){
		while (i > remove[j] && j<remove.length-1) j++;
		if (i != remove[j]) arr[n++] = objs[i];
	    }
	} else System.arraycopy(objs, 0, arr, 0, n = objs.length);

	System.out.println("yadda");
	if (ent != null){
	    Object[] gnu = ent.editEntry(objs);
	    System.arraycopy(gnu, 0, arr, n, gnu.length);
	}

	data.add(arr);
    }

//remove selected, or something (takes an int[])
}
