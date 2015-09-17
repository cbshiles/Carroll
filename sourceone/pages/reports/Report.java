package sourceone.pages.reports;

import sourceone.fields.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.pages.*;

import java.time.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public abstract class Report extends TablePage{

    View vend;
    protected Account[] accounts;
    LocalDate startD, endD;
    TextField aDate, bDate;
    Key rKey, sKey; //account key, string key
    Enterer sent;
    boolean grand;
    float total;
    
    public Report(String name, Page p, Account[] acts) {
	this(name, p, acts, false);
    }
    public Report(String name, Page p, Account[] acts, boolean gr) {
	super(name, p);
	grand = gr;
	accounts = acts;

	JPanel cPan = new JPanel();

	LocalDate tday = LocalDate.now();
	
	aDate = new TextField("Start Date");
	aDate.set(BasicFormatter.cinvert(tday.withDayOfMonth(1)));
	bDate = new TextField("End Date");
	bDate.set(BasicFormatter.cinvert(tday.withDayOfMonth(tday.getMonth().maxLength())));

	cPan.add(aDate.getJP());
	cPan.add(bDate.getJP());

	FieldListener fl = new FieldListener() {
		public void dew() {
		    datem();
//		    catch (Exception e) {new XcptDialog(Report.this, e);}
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

    protected void init(){//should be called in subclass constructor
	sKey = ski(rKey);
	sent = new StrEnt(rKey.length);
	datem();
    }

    protected Key ski(Key accountKey){//put in rKey, get sKey (for subclasses)
	int ln = accountKey.length;
	Cut[] cs = new Cut[ln];
	int i;
	for (i=0; i<ln; i++)
	    cs[i] = new StringCut(accountKey.cuts[i].name);
	return new Key(cs);
    }

    public void dew(){
    	dew(startD, endD);
    }

    protected Object[] tutle(){//puts grand total on jtable
	int ln = rKey.length;
	Object[] arr = new Object[ln];
	java.util.Arrays.fill(arr, "");
	arr[ln-1] = total;
	return arr;

    }

    private Object[] robber(int len, String name){
	//report's object array factory (ask me)
	//produces either a header or a blank line
	Object[] arr = new Object[len];
	java.util.Arrays.fill(arr, "");
	if (name != null){
	    arr[0]=name;
	}
	return arr;
    }
    
    public void dew(LocalDate a, LocalDate z){//rKey & accounts must align
	vend = new View(sKey, sent); //view going to csv
	try{
	    total = 0f;
	    for (Account act: accounts){
		Key tKey = act.aKey;
		View v = act.span(a, z);
		if (v == null) {System.err.println("WARNING: Account "+act.name+" returned null"); break;}
		vend.chunk(robber(rKey.length, act.name));
		vend.chunk(tKey.chunky());
		v.addOut(vend);
		v.push1();
		vend.chunk(robber(rKey.length, null));
		total += act.total;
	    }
	    vend.addTable2();
	    if (grand) vend.chunk(tutle());
	    jsp.setViewportView(jt = (javax.swing.JTable)vend.push());
	}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception ex){ex.printStackTrace();}
    }

    private void datem(){ //doesnt change table unless both valid dates, and one has changed
	boolean changed = false; 
	try{ 
	    LocalDate newSt = StringIn.parseDate(aDate.text());
	    if (! newSt.equals(startD)){startD = newSt; changed = true;}
	    LocalDate newNd = StringIn.parseDate(bDate.text());
	    if (! newNd.equals(endD)){endD = newNd; changed = true;}
	} catch (InputXcpt ix) {changed = false;}
	if (changed) dew();
    }

    
    private class StrEnt implements Enterer{
	int len, des;
	public StrEnt(int l, int d){
	    //d is normally 1, must be at least 2 smaller than l
	    len = l; des = d;
	}

	public StrEnt(int l){
	    this(l, 1);
	}
	
	public Object[] editEntry(Object[] o){
	    Object[] ret = new Object[len];

	    ret[0] = ((o[0] == null) ? "":BasicFormatter.cinvert((LocalDate)o[0]));
	    
	    int i;
	    for (i=1; i<des+1; i++)
		ret[i] = ((o[i] == null) ? "":o[i]);

	    for(; i<len; i++){
		float z = (float)o[i];
		ret[i] = (Math.abs(z)>.01)?""+z:"";
	    }
	    return ret;
	}
    }	

}
