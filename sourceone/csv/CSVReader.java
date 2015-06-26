import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

public class CSVReader {

    public static List<List<String>> read(String f) throws IOException{
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(f));
	} catch (FileNotFoundException e){
	    System.err.println("Can't find "+f);
	    return null;
	}
	
	String s;
	List<List<String>> lzt = new ArrayList<List<String>>();
	ArrayList<String> slzt;
		
	while ((s = br.readLine()) != null){
	    String [] strs = s.split(",");
	    for (int i=0; i<strs.length; i++)
		strs[i] = strs[i].trim();
	    lzt.add(new ArrayList<String>(Arrays.asList(strs)));
	}
	return lzt;
    }
}
