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

    public void pushTable(boolean hasView){

	if (hasView) { g.view.addTable(); }

	else { g.addTable(); }

	try{ jsp.setViewportView(jt = (JTable)g.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }

    public void pushTable(Matrix m, String sortBy, boolean asc){ //#garbage hack, not recommended for reuse
	try{
	    m.sort(sortBy, asc);
	    m.addTable();

	    jsp.setViewportView(jt = (JTable)g.push());} //thats a g
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }

    public void pushTable(boolean hasView, String sortBy, boolean asc){ //push w/ sort (this ones also pretty bad)

	Matrix m;
	try{
	    if (hasView) { m = g.view; g.push1();}
	    else { m = g; }

	    m.sort(sortBy, asc);
	    m.addTable();

	    jsp.setViewportView(jt = (JTable)m.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }

    public void pushTable(){
	pushTable(true);
    }

    protected void wrap(){
	tablePlace();
	setVisible(true);
    }

        void addEmpties(int n, JPanel pj){
	for (int i=0; i<n; i++)
	    pj.add(new JPanel());
    }
}
