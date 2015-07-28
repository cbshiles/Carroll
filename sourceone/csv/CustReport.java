package sourceone.csv;

import sourceone.key.*;
import java.io.*;
import java.util.*;

public class CustReport extends CSVOutput {
    ArrayList<String> lzt = new ArrayList<String>();

    public CustReport(Key k, String fileName, String addon) throws IOException{
	super (k, fileName, addon);
    }

    //works by having last name as the first thing on the line
    public void endEntry(){
	lzt.add(line);
	line = "";
	first = true;
    }

    public Object close(){
	Collections.sort(lzt);
	for (String s : lzt)
	    text += s+'\n';
	text += addon;
	try { fw.write(text); fw.close();
	} catch (IOException ie){
	    System.err.println(ie);
	    throw new Error("Error on printing to CSV");
	}
	return null;
    }

}
