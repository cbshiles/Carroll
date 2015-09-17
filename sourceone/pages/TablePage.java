package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class TablePage extends Page {

    protected JPanel jp = new JPanel(new BorderLayout());
    protected JScrollPane jsp = new JScrollPane();
    protected Grid g;
    protected JTable jt;
    
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

    public void pushTable(){
	pushTable(true);
    }

    protected void wrap(){
	tablePlace();
	setVisible(true);
    }

    protected void addEmpties(int n, JPanel pj){
	for (int i=0; i<n; i++)
	    pj.add(new JPanel());
    }
}
