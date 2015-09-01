package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.pages.blobs.*;
import java.time.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class CenterFile extends TablePage{

    //hackathon
    Key strKey = new Key(new Cut[]{new StringCut("A"), new StringCut("B"), new StringCut("C"), new StringCut("D"), new StringCut("E")});

    private class StrEnt implements Enterer{
	public Object[] editEntry(Object[] o){
	    return new Object[]{
		""+o[0], ""+o[1], ""+o[2], ""+o[3], ""+o[4]
	    };
	}
    }
    
    View vend = new View(strKey, new StrEnt()); //view going to csv
    protected Account[] accounts;

    public CenterFile(String name, Page p, Account[] acts) {
	super(name, p);
	accounts = acts;

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
	    for (Account act: accounts){
		View v = act.span(a, z);
		if (v == null) break;
		vend.chunk(new Object[]{"~~~", act.name, "", "", ""});
		v.addOut(vend);
		v.push1();
		vend.chunk(new Object[]{"", "", "", "", ""});
	    }
	    vend.addTable();
	    jsp.setViewportView(jt = (javax.swing.JTable)vend.push());
	}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
    }

    public static abstract class Account {

	protected PayInFact pif;
	private Blob[] blobs;
	public String name;
	
	public Account(String name, Blob[] bs){this.name = name; blobs = bs;}

	public void loadBlobs(Blob[] bs){blobs = bs;} //enter a new set of blobs

	public abstract float getStart(LocalDate ld)throws Exception;

	public void addPif(PayInFact pif){this.pif = pif;}

	public View span(LocalDate a, LocalDate z) throws Exception{//can return null
	    if (a.isAfter(z)) return null;

	    View v = new View(Key.sumKey, null, null);

	    v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, getStart(a)});
	
	    for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
		LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
		nz = nz.isBefore(z)?nz:z;

		View vi = new View(Key.sumKey);
		vi.addOut(v);
		for (Blob b: blobs){// tip: you need to use an input before making a new one
		    Grid g = new Grid(b.k, b.in(n, nz));
		    g.addOut(vi);
		    vi.switchEnts(b.ent());
		    g.go1();
		}
		vi.sort("Date", true);
		vi.push1();
		
		float deb = vi.floatSum("Debit Amt");
		float cred = vi.floatSum("Credit Amt");
		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});
	    }
	    v.chunk(new Object[]{z, "Ending Balance", 0f, 0f, v.floatSum("Balance")});

	    return v;
	}
	protected String sql(String b4){return b4.trim().replaceAll("\\s", "_");}
    }
}
