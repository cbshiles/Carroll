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
	System.out.println("recieve entry eas pcalled");
	
	Object[] arr = new Object[key.length];

	if (remove != null){
	    int i,j,n;
	    for (i=j=n=0; i<objs.length; i++){
		while (i > remove[j]) j++;
		if (i < remove[j]) arr[n++] = objs[i];
		i++;
	    }
	} else System.arraycopy(objs, 0, arr, 0, objs.length);


	if (ent != null){
	    Object[] gnu = ent.editEntry(objs);
	}
	else data.add(objs);

	NOT FINISHED, RIGHT HERE
    }

//remove selected, or something (takes an int[])
}
