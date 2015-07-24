package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class TablePage extends Page {

    JPanel jp = new JPanel(new BorderLayout());
    JScrollPane jsp = new JScrollPane();
    Grid g;
    JTable jt;
    
    public TablePage(String title, Page p){
	super(title, p);
	jp.add(jsp, BorderLayout.NORTH);
	setContentPane(jp);
    }

    public void pushTable(){ //# assumes you have a view on your grid, should just ask for a matrix
	g.view.addTable();
	try{ jsp.setViewportView(jt = (JTable)g.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }
}
