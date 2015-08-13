package sourceone;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

public class HelloIText {
    public static void main(String[] args) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document,
                new FileOutputStream("HelloWorld-Table.pdf"));

            document.open();

            PdfPTable table = new PdfPTable(3); // 3 columns.

            PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
            PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
            PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            document.add(table);

            document.close();
        } catch(Exception e){

        }
    }
}
