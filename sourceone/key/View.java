package sourceone.key;

import java.util.ArrayList;

//Any new cuts are to be added to the key before the reach a view constructor
//This feature is in Matrix's makeView

public class View extends Matrix{

    Enterer ent = null; //# new change (aug 18)
    int[] remove;
    boolean clear;
    
    public View(Key k, int[] r, Enterer e){
	super(k);
	remove = r;
	ent = e;
	clear = false;
    }

    public View(Key k, Enterer e){//clear one
	super(k);
	ent = e;
	clear = true;
    }

    public View(Key k){//skeez one
	super(k);
	clear = true;
    }

    public void switchEnts(Enterer e){ent=e;}

    private void rownd(int i){//i is index
	for (Object[] larr : data)
	    larr[i] = rnd((float)larr[i]);
    }

    public static float rnd(float f){
	return Math.round(f*100)/100f;
    }


    public void freshen(){data = new ArrayList<Object[]>();}

    // public void pint(Object[] objs){
    // 	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    // 	for (Object o : objs)
    // 	    System.out.print(o+" ");
    // 	System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    // }

    public void receiveEntry(Object[] objs) throws InputXcpt{

	Object[] arr = new Object[key.length];

	int n;
	if (! clear){
	    if (remove != null){
		int i,j;
		for (i=j=n=0; i<objs.length; i++){
		    while (i > remove[j] && j<remove.length-1) j++;
		    if (i != remove[j]) arr[n++] = objs[i];
		}
	    } else System.arraycopy(objs, 0, arr, 0, n = objs.length);
	} else n=0;

	Object[] gnu = null;

	if (ent != null){
	    gnu = ent.editEntry(objs);
	    System.arraycopy(gnu, 0, arr, n, gnu.length);
	}

	assert arr.length == n + ((ent != null)?gnu.length:0) : "Row isn't fitting into view";
	
	data.add(arr);
    }
}
