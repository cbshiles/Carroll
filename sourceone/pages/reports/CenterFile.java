package sourceone.pages.reports;

import sourceone.fields.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;

import java.time.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public abstract class CenterFile extends TablePage{

    //hackathon
    Key strKey = new Key(new Cut[]{new StringCut("A"), new StringCut("B"), new StringCut("C"), new StringCut("D"), new StringCut("E")});

    private class StrEnt implements Enterer{
	public Object[] editEntry(Object[] o){

	    String[] f = new String[3];
	    String d= BasicFormatter.cinvert((LocalDate)o[0]);

	    for (int x=0; x<3;x++){
		float z = (float)o[x+2];
		f[x] = (Math.abs(z)>.01)?""+z:"";
	    }	
	    return new Object[]{
		d, ""+o[1], ""+f[0], ""+f[1], ""+f[2]
	    };
	}
    }
    
    View vend;
    protected Account[] accounts;
    LocalDate startD, endD;
    TextField aDate, bDate;
    private void datem(){ //doesnt change table unless both valid dates, and one has changed
	boolean changed = false; 
	try{ 
	    LocalDate newSt = StringIn.parseDate(aDate.text());
	    if (! startD.equals(newSt)){startD = newSt; changed = true;}
	    LocalDate newNd = StringIn.parseDate(bDate.text());
	    if (! endD.equals(newNd)){endD = newNd; changed = true;}
	} catch (InputXcpt ix) {changed = false;}
	if (changed) dew();
    }

    public CenterFile(String name, Page p, Account[] acts) {
	super(name, p);
	accounts = acts;

	JPanel cPan = new JPanel();

	LocalDate tday = LocalDate.now();
	
	aDate = new TextField("Start Date");
	aDate.set(BasicFormatter.cinvert(tday.withDayOfMonth(1)));
	bDate = new TextField("End Date");
	bDate.set(BasicFormatter.cinvert(tday.withDayOfMonth(tday.getMonth().maxLength())));
	datem();

	cPan.add(aDate.getJP());
	cPan.add(bDate.getJP());

	FieldListener fl = new FieldListener() {
		public void dew() {
		    datem();
//		    catch (Exception e) {new XcptDialog(CenterFile.this, e);}
		}};

	aDate.addListener(fl);
	bDate.addListener(fl);

	jp.add(cPan, BorderLayout.CENTER);

	JButton jb = new JButton("Create Report");
	jp.add(jb, BorderLayout.SOUTH);
	jb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent a) {
		    try {
			vend.addOut(new ReserveFormatter(new CSVDest(SQLBot.bot.path+name+"Report_"+LocalDate.now()+".csv",
								     /*Key.sumKey.csvnames()+*/"\n")));
			vend.push();
		    } catch (Exception e){
			System.err.println("RORRE: "+e);
		    }
		    kill();
		}});
	wrap();
    }

    public Key sendKey(){return strKey;}
    protected Enterer sendEnt(){return new StrEnt();}

    public void dew(){
    	dew(startD, endD, sendEnt());
    }

    private Object[] obber(int len, String name){
	//either header or blank line
	Object[] arr = new Object[len];
	java.util.Arrays.fill(arr, "");
	if (name != null){
	    arr[0]=name;
	}
	return arr;
    }
    
    public void dew(LocalDate a, LocalDate z, Enterer e){//yek & accounts must align
	Key yek = sendKey();//~ dun like it
	vend = new View(yek, e); //view going to csv
	try{
	    for (Account act: accounts){
		View v = act.span(a, z);
		if (v == null) {System.err.println("WARNING: Account "+act.name+" returned null"; break;}
		vend.chunk(obber(yek.length, act.name));
		vend.chunk(yek.chunky());
		v.addOut(vend);
		v.push1();
		vend.chunk(obber(yek.length, null));
		yek = sendKey();	
	    }
	    vend.addTable2();
	    jsp.setViewportView(jt = (javax.swing.JTable)vend.push());
	}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception ex){ex.printStackTrace();}
    }
}
