package sourceone.key;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PdfDest implements StringDest{

    String fName;
    PdfPTable table;
    
    public PdfDest(Key k, String fn){
	fName = fn;
	table = new PdfPTable(k.cuts.length);
    }

    public void put(String s){
	table.addCell(new PdfPCell(new Paragraph(s)));
    }

    public void endEntry(){
	;
    }

    public Object close(){
        Document document = new Document();
	PdfWriter.getInstance(document,
			      new java.io.FileOutputStream(fName));
	document.open();
	document.add(table);
	document.close();
    }
}
