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
    protected ArrayList<Blob> blobs = new ArrayList<Blob>();

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

    public void dew(int months, int year, int x){//do x months
	if (x < 1) return;
	LocalDate dz = null, di = LocalDate.of(year, months, 1);

	for (Blob b : blobs){// tip: you need to use a result set ( probably input) before making a new one
	    View v = new View(Key.sumKey);
	    v.addOut(vend);

	    v.chunk(new Object[]{di, "Beginning Balance", 0f, 0f, b.getStart(di)});
	    
	    try {
	    for(int i=0; i<x; i++){
		
		dz = di.plusDays(di.getMonth().maxLength() - 1);

		Grid g = new Grid(b.key, b.in(di, dz));
		g.addOut(v);
		v.switchEnts(b.ent()); //yehhh (cuz each in is for a certqain time period
		g.go1();

		float deb = v.floatSum("Debit Amt");
		float cred = v.floatSum("Credit Amt");
		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});

		di = di.plusMonths(1);
	    }
	    v.chunk(new Object[]{dz, "Ending Balance", 0f, 0f, vend.floatSum("Balance")});
	    
	    v.push1();
	    } catch (Exception e) {System.out.println("delete thsis stupid try block");}
	}

	 vend.addTable();
	try{ jsp.setViewportView(jt = (javax.swing.JTable)vend.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }
}



	// vend.chunk(new Object[]{di, "Beginning Balance", 0f, 0f, getStart(di)});
	
	// for(int i=0; i<x; i++){
	//     View v = new View(Key.sumKey);
	//     v.addOut(vend);

	//     dz = di.plusDays(di.getMonth().maxLength() - 1);

	//     try {
	// 	for (Blob b : blobs){// tip: you need to use a result set ( probably input) before making a new one
	// 	    Grid g = new Grid(b.key, b.in(di, dz));
	// 	    g.addOut(v);
	// 	    v.switchEnts(b.ent());
	// 	    g.go1();
	// 	}

	// 	v.sort("Date", true);

	// 	float deb = v.floatSum("Debit Amt");
	// 	float cred = v.floatSum("Credit Amt");
	// 	v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});

	// 	v.push1();
	//     } catch (Exception e) {System.out.println("ERROR in blobbing:\n"+e); return;}
	//     di = di.plusMonths(1);
	// }
	// vend.chunk(new Object[]{dz, "Ending Balance", 0f, 0f, vend.floatSum("Balance")});
