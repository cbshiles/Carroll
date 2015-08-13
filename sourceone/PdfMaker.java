package sourceone;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.util.*;

public class PdfMaker{
    static BufferedReader br;
    static String line;
    static Document document = null;
    
    public static void main(String[] args){
	String fName = args[0];

	try{
	    br = new BufferedReader(new FileReader(fName+".csv"));
        document = new Document();
	PdfWriter.getInstance(document,
			      new FileOutputStream(fName+".pdf"));
	} catch (Exception e) {System.err.println(e);}

	boolean first = true; //# do funky with first line
	String[] words;
	PdfPTable table = null;

	while (readLine()){
	    int cols;
	    words = split(line);
	    if (words.length == 0) {cols = line.length() - line.replace("~", "").length();}
	    else cols = words.length;
	    if (first) {
		table = new PdfPTable(cols);
		table.setWidthPercentage(100);
		first = false;
	    }

	    for (String word : words){
		table.addCell(new PdfPCell(new Paragraph(word)));
	    }
	}

	try{
	document.open();
	document.add(table);
	document.close();
	} catch (DocumentException e) {System.err.println(e);}
    }

    static boolean readLine(){
	try {
	return (line = br.readLine()) != null;
	} catch (Exception e) {System.err.println("Line read error\n"+e);}
	return false;
    }

    static String[] split(String str){
	int i = 0, c = 0, n;
	ArrayList<String> lzt = new ArrayList<String>();
	while(-1 != (n = str.indexOf('~', i))){
	    lzt.add(str.substring(i, n));
	    i = n+1;
	}
	lzt.add(str.substring(i));
	return lzt.toArray(new String[lzt.size()]);
    }
}
