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
	contNKey = Key.contractKey.just(new String[] {"Reserve", "Start Date"});
	
	ko = custKey.add(contOKey.cuts);
	kn = custKey.add(contNKey.cuts);

	//from beginning of month A to end of month B (A might == B)
	LocalDate lda  = LocalDate.of(2015, 6, 1);
	LocalDate ldb  = LocalDate.of(2015, 8, 1);

	v = new View(reportKey);
		
//BasicFormatter.cinvert(lda)
	v.chunk(new Object[]{lda, "Beginning Balance", 0f, 0f, getStart(lda)});
	
	while (lda.compareTo(ldb) < 0){
	    LocalDate lde = lda.plusDays(lda.getMonth().maxLength() - lda.getDayOfMonth());

	    go = new Grid(ko, new QueryIn(custKey, contOKey, "WHERE Contracts.Paid_Off >= '"+lda+"' AND Contracts.Paid_Off <= '"+lde+"' AND Contracts.Customer_ID = Customers.ID;"));
	    g = new Grid(kn, new QueryIn(custKey, contNKey, "WHERE Contracts.Start_Date >= '"+lda+"' AND Contracts.Start_Date <= '"+lde+"' AND Contracts.Customer_ID = Customers.ID;"));
	
	    go.addOut(v);
	    g.addOut(v);

	    v.switchEnts(new Nnt(kn));
	    System.err.println("AA");
	    g.pull(); System.err.println("BB"); g.push1();

	    v.switchEnts(new Ont(ko));
	    System.err.println("CC");
	    go.pull(); 	    System.err.println(go.numRows()); go.push1();
	    
	    //wrapIt(); sorting and vertyhing
	    //after everything
	    lda = lda.plusMonths(1);
	}
	pushTable();
	tablePlace();
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

/*
  custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

  contKey = Key.contractKey.just(new String[] {"ID", "Reserve"});
  Input in;
  try { //Contracts.Next_Due IS NOT NULL
  in = new QueryIn(custKey, contKey, "WHERE Contracts.Paid_Off IS NULL AND Contracts.Customer_ID = Customers.ID;");

  k = custKey.add(contKey.cuts);
  g = new Grid(k, in);
  g.pull();
  } catch (Exception e) {new XcptDialog(this, e); return;}
  pushTable(false);
  System.out.println("1Reserve balance: "+g.floatSum("Reserve"));
  System.out.println(g.numRows());
  LocalDate dt = LocalDate.of(2015, 8, 6);//LocalDate.now()
  System.out.println("2Reserve balance: "+getStart(dt));
  tablePlace();
  setVisible(true);
*/
