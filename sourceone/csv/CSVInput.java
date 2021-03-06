package sourceone.csv;

import java.io.*;
import sourceone.key.*;

public class CSVInput implements StringSource{

    BufferedReader br;
    String[] strs;
    int n, i=0;
    String fName;
    
    public CSVInput(String fName) throws Exception{
	br = new BufferedReader(new FileReader(fName));
	this.fName = fName;
    }

    public boolean hasEntries(){
	String s;
	try {
	    boolean r = ((s = br.readLine()) != null);
	    if (r) strs = s.split("~"); //! changed delimeter to tilde
	    n=0;
	    return r;
	} catch (IOException e){
	    System.err.println("error reading csv: "+fName);
	    return false;
	}
    }

    void trim(){
	for (int i=0; i<strs.length; i++)
	    strs[i] = strs[i].trim();
    }

    public String get() throws InputXcpt{
	i++;
	if (n < strs.length)
	    return strs[n++];
	else throw new InputXcpt("CSV line "+(i/n)+" out of entries: "+fName);
    }
}
