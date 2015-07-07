package sourceone.csv;

import sourceone.key.*;
import java.io.*;

public class CSVOutput implements Output{

    FileWriter fw;
    String line = "", text;
    boolean first = true;

    public CSVOutput(Key k, String fileName) throws IOException{

	fw = new FileWriter(fileName); //append option

	text = k.names()+'\n';
//can make a new one or append to a template

    }
    
    public void put(String x){
	pat(x);
    }
    
    public void put(int x){
	pat(""+x);
    }
    
    public void put(float x){
	pat(""+x);
    }
    
    public void put(java.time.LocalDate x){
	pat(x.toString());
    }

    private void pat(String s){
	if (! first) line += ", ";
	else first = false;
	line += s;

    }

    public void endEntry(){
	text += line + '\n';
	line = "";
	first = true;
    }

    public Object close(){
	try { 		System.err.println(text); fw.write(text); fw.close();
	} catch (IOException ie){
	    System.err.println(ie);
	    throw new Error("Error on printing to CSV");
	}
	return null;
    }
}
