package sourceone.pages;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.*;
import java.time.*;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.fields.*;

public class ContractForm extends Form {
    private String csv, csvFile, csvTail = "";
    private boolean stuck;
    private float floorAmount, net;
    FloorPayDialog fpd; // = null;
    String today = BasicFormatter.cinvert(LocalDate.now());
    RadioField payFreq; TextField botDate;

    @Override public void refresh() {
	super.refresh();
	fillFields();
    }

    public void fillFields(){ //fill in fields w. default values
	botDate.set(today);
	payFreq.select(0);
	SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    revalidate();
                }
            });
    }
    
    public ContractForm(Page p) throws Exception{
	super("Contract", p);

	place(.15f, .1f, .35f, .75f);
	
	setLayout(new java.awt.GridLayout(0, 1));

	TextField tot, res;


	botDate = new TextField("Date Bought");

	add(botDate.getJP());
	
	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Address 2"));
	addF(new TextField("Phone Number"));

	addF(tot = new TextField("Total Amount"));
	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount"));
	addF(payFreq = new RadioField("Payment Frequency",
			    new String[]{"Weekly", "Biweekly", "Monthly"},
				      new String[]{"7", "14", "30"}, 0));
	addF(new OptionField("Final Payment", "0", true));
	addF(new TextField("Start Date"));

	TextField r1, r2;
	NetField net;
	addF(net = new NetField(tot));
	addF(r1 = new ReserveField(tot, "SourceOne Reserve", 5));
	addF(r2 = new ReserveField(tot, "L&K Reserve", 10));			      

	TextField newt = new NewtField("Net:", net, r1, r2);
	add(newt.getJP());
	
	addF(new TextField("VIN"));

	fields.add(botDate);

	fillFields();
	JButton submit = new JButton("Submit");

	Key custKey = Key.customerKey.just(new String[]{"First Name", "Last Name", "Address"}).add(new Cut[]{new StringCut("Address 2"), new StringCut("Phone Number")});
	Grid custGrid = new Grid(custKey, new StringIn(this));
	custGrid.addView(new String[]{"Address", "Address 2"}, new Cut[]{new StringCut("Address")},
			 new Unt(custKey));
	custGrid.view.addOut(new SQLFormatter(new InsertDest(custGrid.view.key, "Customers", true)));

	/*
	  Called Total Contract here, this is actually TEP, or total expected pay.
	  Called total contract here due to compatibility issues
	*/
	Key contKey = Key.contractKey.just(new String[]{"Total Contract", "Number of Payments", "Amount of Payment", "Payment Frequency", "Final Payment Amount", "Start Date", "Net Amount", "srcreserve", "lnkreserve", "VIN", "Date Bought"});

	Ent ent = new Ent(contKey);
	Grid contGrid = new Grid(contKey, new StringIn(this));
	contGrid.addView(new String[]{"Total Contract"}, new Cut[]{new DateCut("Next Due"), new FloatCut("Other Payments"), new IntCut("Customer ID"), new FloatCut("Gross Amount"), new FloatCut("Total Contract")/*, new FloatCut("srcreserve"), new FloatCut("lnkreserve")*/},
			 ent);

	// for (Field f: fields)
	//     System.out.println(f.name);
		 
	
	contGrid.view.addOut(new SQLFormatter(new InsertDest(contGrid.view.key, "Contracts", true)));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			ent.set_id((int)custGrid.go()); //# causes crap entries into the customers table
			int cid; //contract id
			if ( (cid = (int)contGrid.go()) == -1)
			    throw new InputXcpt("SQL insertion unsuccessful");
			if (fpd != null) fpd.set_id(cid);
			makeCSV(custGrid, contGrid);
			if (!stuck) sendReport();
			freshen();
			ContractForm.this.refresh();
		    } catch (InputXcpt ix) {
			ix.printStackTrace();
			new XcptDialog(getName(), ContractForm.this, ix);
			ContractForm.this.refresh();
			//ix.printStackTrace();
		    }}});

	add(submit);
	setVisible(true);
    }

    private void makeCSV(Grid custGrid, Grid contGrid){
	int sd, aop, nop, fpa, tc;
	int cust_id, vin, res, pf, nt, db, r1, r2;

	Object [] co = contGrid.data.get(0);
	sd = contGrid.key.dex("Start Date");
	aop = contGrid.key.dex("Amount of Payment");
	nop = contGrid.key.dex("Number of Payments");
	fpa = contGrid.key.dex("Final Payment Amount");
	tc = contGrid.key.dex("Total Contract");
	vin = contGrid.key.dex("VIN");
	pf = contGrid.key.dex("Payment Frequency");
	nt = contGrid.key.dex("Net Amount");
	db = contGrid.key.dex("Date Bought");
	r1 = contGrid.key.dex("srcreserve");
	r2 = contGrid.key.dex("lnkreserve");
	
	int add, add2, fn, ln, pn;

	Object [] cu = custGrid.data.get(0);
	add = custGrid.key.dex("Address");
	add2 = custGrid.key.dex("Address 2");
	fn = custGrid.key.dex("First Name");
	ln = custGrid.key.dex("Last Name");
	pn = custGrid.key.dex("Phone Number");

	
	
	csv = "";
	csv += addLine(3);
	csv += addLine(BasicFormatter.cinvert((LocalDate)co[db]), 0);
	csv += 	addLine();
	csv += addLine(new String[] {"A/R Purchase:", "", "Gross", "%", "Net"});
	csv += addLine();
	csv += addLine("Name/Address/Phone #:", 0);
	csv += addLine();
	float gross = (float)co[tc]; net = (float)co[nt]; //private instance varibal
	float perc = net/gross;
	csv += addLine(new String[] {""+cu[fn]+' '+cu[ln], "", frm(gross), frm(perc*100), frm(net)});
	csv += addLine(""+cu[pn], 0); //# format phone number?
	csv += addLine(""+cu[add], 0);
	csv += addLine(""+cu[add2], 0);
	csv += addLine(terms((int)co[nop], (float)co[aop], (int)co[pf], (float)co[fpa])+
		       " b"+BasicFormatter.cinvert((LocalDate)co[sd]), 0);
	csv += addLine();

	float res1 = (float)co[r1];
	csv += addLine(new String[]{"Sourceone Reserve", "","","", frm(-res1)});
	float res2 = (float)co[r2];
	csv += addLine(new String[]{"L&K Reserve", "","","", frm(-res2)});
	csv += addLine();
	net -= (res1+res2);
	csv += addLine(new String[]{"", "", "", "", frm(net)});
	csvTail += addLine();

	csvFile = SQLBot.bot.path+"L&K T "+cu[fn]+'_'+cu[ln]+'_'+BasicFormatter.finvert(LocalDate.now())+".csv";
    }

    private java.text.DecimalFormat myFormatter = new java.text.DecimalFormat("#0.00");
    private String frm(float ff) {return myFormatter.format(ff);}

    private void sendReport(){
	csv += csvTail;
	csvTail = "";
	csv += addLine();	csv += addLine(); //	csv += addLine();
	csv += addLine(new String[]{"Total"," ","","",frm(net-floorAmount)});
	try {
	    new CSVOutput(csv, csvFile);
	} catch (Exception e){System.err.println("Error csving"); System.err.println(e);}
    }

    public String terms(int num, float amt, int freq, float fin){
	char c;
	if (freq==7) c='W';
	else if (freq ==14) c='B';
	else c='M';
	String trms = ""+num+" "+c+" @ "+amt;
	if (fin - .01f > 0f)
	    trms += " & 1 @ "+fin;
	return trms;
    }


    String addLine(String[] arr){
	String a=""; boolean first = true;
	for (String s : arr){
	    if (! first) a += "~";
	    else first = false;
	    a += s;
	}
	return a+'\n';
    }

    String addLine(String s, int x){
	String a ="";
	for (int i=0; i<5; i++){
	    if (i==x) a += s;
	    a += '~';
	}
	return a+'\n';
    }

    String addLine(){return addLine(1);}

    String addLine(int n){
	String a = "";
	for (int j= 0; j<n; j++){
	    for (int i=0; i<5; i++){
		a += '~';
	    }
	    a += '\n';
	}
	return a;
    }
	    
    private class Unt implements Enterer{

	int add, add2;
	
	public Unt(Key k){
	    add = k.dex("Address");
	    add2 = k.dex("Address 2");
	}

	public Object[] editEntry(Object[] o){
	    return new Object[]{
		""+o[add]+"~ "+o[add2]
	    };
	}
    }
    private class Ent implements Enterer{

	int sd, aop, nop, fpa, tc;
	int cust_id, vin, db, r1, r2;
	
	public Ent(Key k){
	    sd = k.dex("Start Date");
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    fpa = k.dex("Final Payment Amount");
	    tc = k.dex("Total Contract");
	    vin = k.dex("VIN");
	    db = k.dex("Date Bought");
	    r1 = k.dex("srcreserve");
	     r2 = k.dex("lnkreserve");
	    
	}

	public void set_id(int i){cust_id=i;}
    
	public Object[] editEntry(Object[] o)throws InputXcpt{

	    float tep = (float)o[tc]; //total expected to pay

	    float tc, grs;

	    tc = tep;
	    grs = tep - (float)o[r1] - (float)o[r2];
	    
	    float  sum;
	    sum = (int)o[nop] * (float)o[aop] + (float)o[fpa];
	    if (Math.abs(sum - tep) > 0.001f)
		throw new InputXcpt(""+sum+" != "+tep+"\nPayment summation does not equal total");

	    try {
		//System.err.println("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		ResultSet rs = SQLBot.bot.query("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		if (stuck = rs.next()){
		    int id = rs.getInt(1);
		    //# move this to verification area
		    if (rs.next()) throw new InputXcpt("WARNING: Multiple cars match that VIN number");  
		    fpd = new FloorPayDialog(id, (LocalDate)o[db]);
		} else {floorAmount = 0f;}

		
	    } catch (SQLException e){throw new InputXcpt(e);}

	    return new Object[]{
		o[sd], //Next Due
		0f, //Other payments
		cust_id,
		grs,
		tc
	    };
	}
    }

    public class FloorPayDialog extends JDialog{

//so, if the car says that we have its floor plan, then we say that the contract has the title now (easy peasy??)
	//itd be nice if we could say Contract title = floor title, just like that

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});
	JTable jt;
	Grid g;
	JPanel jp = new JPanel(new BorderLayout());
	View v;
	FloorCalc ent;
	JScrollPane jsp = new JScrollPane();
	int cont_id = 0;

	public int set_id(int i){return cont_id=i;}

	public void getTable(LocalDate ld){
	    ent.setDay(ld);
	    v = g.addView(new String[]{"Title", "Curtailed"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
							   new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		ent);

	    v.addTable();
	    
	    try{ jsp.setViewportView(jt = (JTable)g.push());}
	    catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	    catch (Exception e){e.printStackTrace();}

	    jt.setRowSelectionAllowed(false);
	}
	
	public FloorPayDialog(int id, LocalDate date){


	    jp.add(jsp, BorderLayout.NORTH);
	    setContentPane(jp);
	    
	    try {
		Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE ID="+id+';');
		g = new Grid(key, in);
		g.pull();
		ent = new FloorCalc(key, date, true);
		getTable(date);
		
	    } catch (Exception e){ new XcptDialog(ContractForm.this, e); return;}

	    JPanel cPan = new JPanel();
	    jp.add(cPan, BorderLayout.SOUTH);

	    sourceone.fields.TextField dateOf;
	    dateOf = new sourceone.fields.TextField("Date paid:", BasicFormatter.cinvert(date));
	    cPan.add(dateOf.getJP(), BorderLayout.EAST);

	    dateOf.addListener(new FieldListener() {
		    public void dew() {
			try {
			    LocalDate d = StringIn.parseDate(dateOf.text());
			    getTable(d);
			} catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		    }});
	    
	    JButton jb = new JButton("Pay Car Off");
	    cPan.add(jb, BorderLayout.SOUTH);
	    jb.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			int tl = key.dex("Title");
			int id = key.dex("ID");
			int vin = key.dex("VIN");
			int veh = key.dex("Vehicle");
			float st = (float)v.get("Sub total", 0);
			try {
			    LocalDate d = date; 
			    
			    Object[] o = g.data.get(0); // getting the onlt possible row (ideally)

			    //{"ID", "Date Bought", "Vehicle", "Item Cost", "Title"});
			    csvTail += addLine("Floorplan Payoff:", 0);
			    csvTail += addLine(new String[] {""+o[veh],""+o[vin],"","",frm(st)});
			    csvTail += addLine();
			    floorAmount = st;
			    sendReport();
			    
			    //! removed Title="+((int)o[tl]+2)+",
			    SQLBot.bot.update("UPDATE Cars SET Date_Paid='"+d+"', Pay_Off_Amount="+st+" WHERE ID="+o[id]);
				
			    if ((int)o[tl] == 1) // all right here
				SQLBot.bot.update("UPDATE Contracts SET Title=1 WHERE ID="+cont_id);
			    dispose();
			} catch (Exception ix) {new XcptDialog(FloorPayDialog.this, ix);}
		    }
		});

	    JButton jq = new JButton("Cancel");
	    cPan.add(jq);//, BorderLayout.SOUTH);
	    jq.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent ae) {
			FloorPayDialog.this.dispose();
		    }
		});

	    setBounds(300,300,1000,600);
	    setVisible(true);
	}
    }

    private class NewtField extends TextField {
	FieldListener fl;
	public NewtField(String name, NetField t, TextField r1, TextField r2){
	    super(name);
	    tf.setEditable(false);
	    fl = new FieldListener(){
		    public void dew(){
			try {
			    tf.setText(""+View.rnd(StringIn.parseFloat(t.amt.getText()) - git(r1) - git(r2)));
			} catch (InputXcpt ix) {;}
		    }
		};
	    t.addListener(fl);
	    r1.addListener(fl);
	    r2.addListener(fl);
	}

	float git(TextField txfl) throws InputXcpt{
	    return StringIn.parseFloat(txfl.text());
	}
    }

    private class ReserveField extends TextField {
	float percent;
	public ReserveField(TextField tot, String title, int p){
	    super(title+" ("+p+"%)");

	    percent = p/100f;
	    
	    tot.addListener(new FieldListener(){
		    public void dew(){
			try {
			    float f = StringIn.parseFloat(tot.text())*percent;
			    tf.setText(""+View.rnd(f));
			} catch (InputXcpt ix) {;}
		    }
		});
	}
    }

}

/**
   These are the old calculations for figuring reserve, gross and net programmatically.
   Do not remove.
*/
/*
  public Object[] editEntry(Object[] o)throws InputXcpt{
  float total, sum;
  total = (float)o[tc];
  sum = (int)o[nop] * (float)o[aop] + (float)o[fpa];
  if (Math.abs(sum - total) > 0.001f)
  throw new InputXcpt(""+sum+" != "+total+"\nPayment summation does not equal total");

  float p = .1f;
  float z = .72f;
  float gross = (1-p)*total;
  return new Object[]{
  o[sd], //Next Due
  0f, //Other payments
  cust_id,
  p*total, //reserve
  gross,
  gross*z //net
  };
  }
*/
