package sourceone.csv;

import sourceone.key.*;
import java.io.*;

public class CSVOutput implements Output{

    FileWriter fw;
    String line = "", text;
    boolean first = true;
    String addon = "";

    public CSVOutput(Key k, String fileName) throws IOException{
	this (k, fileName, "");
    }

    public CSVOutput(String str, String fileName) throws IOException{
	text = str;
	fw = new FileWriter(fileName); //append optionx
	close();
    }    
    
    public CSVOutput(Key k, String fileName, String addon) throws IOException{
	this.addon = addon;
	fw = new FileWriter(fileName); //append option

	text = k.csvnames()+'\n';
//can make a new one or append to a template

    }
    
    public void put(String x){
	pat(x);
    }
    
    public void put(int x){
	pat(""+x);
    }

    private java.text.DecimalFormat myFormatter = new java.text.DecimalFormat("#0.00");
    private String frm(float ff) {return myFormatter.format(ff);}
    
    public void put(float x){
	pat(frm(x));
    }
    
    public void put(java.time.LocalDate x){
	pat(BasicFormatter.cinvert(x));
    }

    private void pat(String s){
	if (! first) line += "~ "; //! changed delimeter to tilde
	else first = false;
	line += s;

    }

    public void endEntry(){
	text += line + '\n';
	line = "";
	first = true;
    }

    public Object close(){
	try { fw.write(text+addon); fw.close();
	} catch (IOException ie){
	    System.err.println(ie);
	    throw new Error("Error on printing to CSV");
	}
	return null;
    }
}
