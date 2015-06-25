import java.io.*;
import java.util.*;

public class CSVTest {

    public static void main(String[] args){
	List<List<String>> lzt;
	try {
	    lzt = CSVReader.read("good.csv");
	    System.err.println(lzt.size());
	    for (List<String> i : lzt){
		for (String j : i){
		    System.out.println(j+"~");
		}
		System.out.println("--------------------");
	    }
	}
	catch (IOException ie){
	    System.err.println("I/O ERR: "+ie.getMessage());
	}
    }
}
