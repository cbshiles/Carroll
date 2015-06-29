package sourceone.key;

public class Key{
    public Cut[] cuts;
    public int length;
    
    public Key(Cut[] c){cuts = c; length = cuts.length;}

    public Key(String[] names, Kind[] types){
	cuts = new Cut[names.length];
	
	for (int i=0; i<names.length; i++){
	    switch(types[i]){
	     case STRING:
		 cuts[i] = new StringCut(names[i]); break;
	     case INT:
		 cuts[i] = new IntCut(names[i]); break;
	     case FLOAT:
		 cuts[i] = new FloatCut(names[i]); break;
	     case DATE:
		 cuts[i] = new DateCut(names[i]); break;
	    }
	}
	length = cuts.length;
    }

    public Object[] getEntry() throws InputXcpt{
	Object[] arr = new Object[cuts.length];
	for (int i=0; i<cuts.length; i++)
	    arr[i] = cuts[i].in();
	return arr;
    }

    public void putEntry(Object[] arr){
	for (int i=0; i<cuts.length; i++)
	    cuts[i].out(arr[i]);
    }

    public void setInput(Input i){
	for (Cut c : cuts)
	    c.ip = i;
    }

    public void setOutput(Output o){
	for (Cut c : cuts)
	    c.op = o;
    }
}
