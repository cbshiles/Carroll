package sourceone.csv;

import sourceone.key.*;
import java.io.*;

public class CSVOutput implements Output{

    FileWriter fw;
    String line = "", text = "";
    boolean first = true;

    public CSVOutput(Key k, String fileName) throws IOException{

	fw = new FileWriter(fileName); //append option
	//make a page
	//do something with headers
	//at the end, load the text into the file
	//can make a new one or append to a template
	//i know when an entry is finished, but how do i know when the whole throughput is??
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
	first = true;
    }

    public void load() throws IOException{
	fw.write(text);
    }
}
