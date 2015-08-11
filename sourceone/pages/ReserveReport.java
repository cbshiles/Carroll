package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import java.time.*;

public class ReserveReport extends TablePage{

    Key custKey, contOKey, contNKey;
    Grid go;
    View v;
    Key ko, kn;
    
    Key reportKey = new Key( new Cut[]{
	    new DateCut("Date"),
	    new StringCut("Trans Description"),
	    new FloatCut("Debit Amt"),
	    new FloatCut("Credit Amt"),
	    new FloatCut("Balance")
	});

    public ReserveReport(Page p) throws Exception{
	super("Reserve Sum", p);

	custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	contOKey = Key.contractKey.just(new String[] {"Reserve", "Paid Off"});
	contNKey = Key.contractKey.just(new String[] {"Reserve", "Date Bought"});
	
	ko = custKey.add(contOKey.cuts);
	kn = custKey.add(contNKey.cuts);

	//from beginning of month A to end of month B (A might == B) //itll have to be slightly greater for the while loop atm
	LocalDate lda, ldb, lde = null;
	lda  = LocalDate.of(2015, 5, 1);
	ldb  = LocalDate.of(2015, 8, 2);


	View vend = new View(reportKey, null, null);
//BasicFormatter.cinvert(lda)
	vend.chunk(new Object[]{lda, "Beginning Balance", 0f, 0f, getStart(lda)});
	
	while (lda.compareTo(ldb) < 0){
	    lde = lda.plusDays(lda.getMonth().maxLength() - lda.getDayOfMonth());

	    v = new View(reportKey);
	    v.addOut(vend);	

// tip: you need to use a result set ( probably input) before making a new one

	    g = new Grid(kn, new QueryIn(custKey, contNKey, "WHERE Contracts.Date_Bought >= '"+lda+"' AND Contracts.Date_Bought <= '"+lde+"' AND Contracts.Customer_ID = Customers.ID;"));
	    g.addOut(v);
	    v.switchEnts(new Nnt(kn));
	    g.go1();


	    go = new Grid(ko, new QueryIn(custKey, contOKey, "WHERE Contracts.Paid_Off >= '"+lda+"' AND Contracts.Paid_Off <= '"+lde+"' AND Contracts.Customer_ID = Customers.ID;"));
	    go.addOut(v);	    
	    v.switchEnts(new Ont(ko));
	    go.go1();

	    v.sort("Date", true);

	    float deb = v.floatSum("Debit Amt");
	    float cred = v.floatSum("Credit Amt");
	    v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb-cred});

	    v.push1();
	    
	    lda = lda.plusMonths(1);
	}


	vend.chunk(new Object[]{lde, "Ending Balance", 0f, 0f, vend.floatSum("Balance")});
	vend.addTable();
	try{ jsp.setViewportView(jt = (javax.swing.JTable)vend.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}
	tablePlace(); //location - size settings
	setVisible(true);
    }

    float getStart(LocalDate ld) {//get beginning balance, starting at ld
	Key r = Key.contractKey.just("Reserve");
	Grid g;
	try {
	    g = new Grid(r, new QueryIn(r,
					"WHERE Start_Date < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off > '"+ld
					+"' );"));
	    g.pull();
	} catch (Exception e) {new XcptDialog(this, e); return .1337f;}
	return g.floatSum("Reserve");
    }

    public static class Ont implements Enterer{
	int ln, fn, res, po;
	
	public Ont(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    po = k.dex("Paid Off");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[po],
		""+o[ln]+", "+o[fn],
		o[res],
		0f,
		0f
	    };
	}
    }
    public static class Nnt implements Enterer{
	int ln, fn, res, sd;
	
	public Nnt(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    sd = k.dex("Start Date");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[sd],
		""+o[ln]+", "+o[fn],
		0f,
		o[res],
		0f
	    };
	}
    }
}
