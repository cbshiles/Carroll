package sourceone.pages;
import sourceone.fields.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.pages.blobs.*;
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
    private void datem(){
	boolean changed = false; 
	try{ startD = StringIn.parseDate(aDate.text()); changed = true;}
	catch (InputXcpt ix) {;}
	try{ endD = StringIn.parseDate(bDate.text()); changed = true;}
	catch (InputXcpt ix) {;}
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
    	dew(startD, endD, sendKey(), sendEnt());
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
    
    public void dew(LocalDate a, LocalDate z, Key yek, Enterer e){//yek & accounts must align
	vend = new View(yek, e); //view going to csv
	try{
	    for (Account act: accounts){
		View v = act.span(a, z);
		if (v == null) break;
		vend.chunk(obber(yek.length, act.name));
		vend.chunk(yek.chunky());
		v.addOut(vend);
		v.push1();
		vend.chunk(obber(yek.length, null));
	    }
	    vend.addTable2();
	    jsp.setViewportView(jt = (javax.swing.JTable)vend.push());
	}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception ex){ex.printStackTrace();}
    }

    public static abstract class Account {

	protected PayInFact pif;
	protected Blob[] blobs;
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
