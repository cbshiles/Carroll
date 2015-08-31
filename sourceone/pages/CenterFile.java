package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.pages.blobs.*;
import java.time.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public abstract class CenterFile extends TablePage{

    View vend = new View(Key.sumKey, null, null); //view going to csv
    protected Account[] accounts;

    public CenterFile(String name, Page p) {
	super(name, p);

	JButton jb = new JButton("Create Report");
	jp.add(jb, BorderLayout.SOUTH);
	jb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent a) {
		    try {
			vend.addOut(new ReserveFormatter(new CSVDest(SQLBot.bot.path+name+"Report_"+LocalDate.now()+".csv",
								     Key.sumKey.csvnames()+'\n')));
			vend.push();
		    } catch (Exception e){
			System.err.println("RORRE: "+e);
		    }
		    kill();
		}});
	wrap();
    }

    public void dew(LocalDate a, LocalDate z){
	try{
	    for (Account act: accounts)
		act.span(a, z);
	    vend.addTable();
	    jsp.setViewportView(jt = (javax.swing.JTable)vend.push());
	}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }

    public abstract class Account {

	private Blob[] blobs;

	public Account(Blob[] bs){blobs = bs;}

	public abstract float getStart(LocalDate ld);

	public void span(LocalDate a, LocalDate z) throws Exception{//can return null
	    if (z.isAfter(a)) return;

	    View v = new View(Key.sumKey);
	    v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, getStart(a)});
	
	    for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
		LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
		nz = nz.isBefore(z)?nz:z;

		View vi = new View(Key.sumKey);
		vi.addOut(v);
		for (Blob b: blobs){// tip: you need to use an input before making a new one
		    Grid g = new Grid(b.k, b.in(n, nz));
		    g.addOut(vi);
		    v.switchEnts(b.ent());
		    g.go1();
		}
		vi.push1();
	    
		float deb = vi.floatSum("Debit Amt");
		float cred = vi.floatSum("Credit Amt");
		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});
	    }
	    v.chunk(new Object[]{z, "Ending Balance", 0f, 0f, v.floatSum("Balance")});

	    v.addOut(vend);
	    v.push1();
	}
    }
}
