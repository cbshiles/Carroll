package sourceone.csv;

import sourceone.key.*;

public class CSVDest implements StringDest{

    boolean first = true;
    String fname, text;

    public CSVDest(String f){this(f, "");}
    
    public CSVDest(String f, String add){fname = f; text = add;}
    
    public void put(String s){
	if (! first) text += "~";
	else first = false;
	text += s;
    }

    public void endEntry(){
	text += '\n';
	first = true;
    }

    public Object close(){try {new CSVOutput(text, fname);}
	catch(Exception e){System.err.println("FDFD "+e);}
	return null;}
}
