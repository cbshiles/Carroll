package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.pages.*;

import java.time.*;

public abstract class Account {

    //  protected PayInFact pif;
    protected Blob[] blobs;
    public String name;
    public Key aKey;
    float total;
	
    public Account(String name, Blob[] bs, Key k){this.name = name; blobs = bs; aKey = k;}

    public void loadBlobs(Blob[] bs){blobs = bs;} //~enter a new set of blobs

    public abstract float getStart(LocalDate ld)throws Exception;

//    public void addPif(PayInFact pif){this.pif = pif;} //~ dun like it

    public View span(LocalDate a, LocalDate z) throws Exception{//can return null
	if (a.isAfter(z)) return null;

	View v = new View(aKey, null, null);

	v.chunk(makeChunk(a));
	
	for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
	    LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
	    nz = nz.isBefore(z)?nz:z;

	    View vi = new View(aKey);
	    vi.addOut(v);
	    for (Blob b: blobs){// tip: you need to use an input before making a new one
		Grid g = b.grid(n, nz); 
		g.addOut(vi);
		vi.switchEnts(b.ent());
		g.go1();
	    }
	    vi.sort("Date", true); //might be the only place we need in-house sort
	    vi.push1();

	    Object[] chnk = makeChunk(vi);
	    if (chnk != null)
		v.chunk(chnk);
	    }
	
	v.chunk(makeChunk(z, v));
	return v;
     }
//    protected String sql(String b4){return b4.trim().replaceAll("\\s", "_");}

//	v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, getStart(a)});
    //many similarities to report's robber
    Object[] makeChunk(LocalDate a) throws Exception{
	Object[] arr = new Object[aKey.length];
	java.util.Arrays.fill(arr, 0f);
	arr[0] = a;
	arr[1] = "Beginning Balance";
	arr[aKey.length-1] = getStart(a);
	return arr;
    }
    abstract Object[] makeChunk(View tv); //might return null

 //v.chunk(new Object[]{z, "Ending Balance", 0f, 0f, v.floatSum("Balance")});   
    Object[] makeChunk(LocalDate z, View tv){
	Object[] arr = new Object[aKey.length];
	java.util.Arrays.fill(arr, 0f);
	arr[0] = z;
	arr[1] = "Ending Balance";
	arr[aKey.length-1] = total = tv.floatSum(aKey.cuts[aKey.length-1].name);
	return arr;
    }
}
