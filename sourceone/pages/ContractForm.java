package sourceone.pages;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.*;
import java.time.*;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;

public class ContractForm extends Form {
    public ContractForm(Page p) throws Exception{
	super("Contract", p);

	place(.15f, .15f, .25f, .8f);
	
	setLayout(new java.awt.GridLayout(0, 1));

	TextField tot, res; Field type = null;

	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Address 2"));
	addF(new TextField("Phone Number"));

	addF(tot = new TextField("Total Amount"));
	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount"));
	addF(new RadioField("Payment Frequency",
			    new String[]{"Weekly", "Biweekly", "Monthly"},
			    new String[]{"7", "14", "30"}));
	addF(new OptionField("Final Payment", "0", true));

	addF(new TextField("Start Date"));
	
	//addF(new TextField("Reserve"));
	addF(res = new ReserveField(tot, type));

	//addF(new TextField("Net Amount"));
	addF(new NetField(tot, res));
	
//	addF(new TextField("Vehicle"));

	addF(new TextField("VIN"));

	addF(type = new RadioField("Contract Type",
				   new String[]{"Full", "Partial"},
				   new String[]{"0", "1"}));

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
	Key contKey = Key.contractKey.just(new String[]{"Total Contract", "Number of Payments", "Amount of Payment", "Payment Frequency", "Final Payment Amount", "Start Date", "Reserve", "Net Amount", "VIN"}).add(new Cut[]{new IntCut("Fullness")});

	Ent ent = new Ent(contKey);
	Grid contGrid = new Grid(contKey, new StringIn(this));
	contGrid.addView(new String[]{"Total Contract", "Fullness"}, new Cut[]{new DateCut("Next Due"), new FloatCut("Other Payments"), new IntCut("Customer ID"), new FloatCut("Gross Amount"), new FloatCut("Total Contract")},
			 ent);
	
	contGrid.view.addOut(new SQLFormatter(new InsertDest(contGrid.view.key, "Contracts", true)));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			ContractForm.this.refresh();
			ent.set_id((int)custGrid.go()); //# causes crap entries into the customers table
			if ((int)contGrid.go() == -1)
			    throw new InputXcpt("SQL insertion unsuccessful");
			freshen();
		    } catch (InputXcpt ix) {
			new XcptDialog(getName(), ContractForm.this, ix);
			//ix.printStackTrace();
		    }}});

	add(submit);
	pack();
	setVisible(true);
    }
	    
    private class Unt implements Enterer{

	int add, add2;
	
	public Unt(Key k){
	    add = k.dex("Address");
	    add2 = k.dex("Address 2");
	}

	public Object[] editEntry(Object[] o){
	    return new Object[]{
		""+o[add]+"; "+o[add2]
	    };
	}
    }
    private class Ent implements Enterer{

	int sd, aop, nop, fpa, tc;
	int cust_id, vin, fll, res;
	
	public Ent(Key k){
	    sd = k.dex("Start Date");
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    fpa = k.dex("Final Payment Amount");
	    tc = k.dex("Total Contract");
	    fll = k.dex("Fullness");
	    vin = k.dex("VIN");
	    res = k.dex("Reserve");
	}

	public void set_id(int i){cust_id=i;}
    
	public Object[] editEntry(Object[] o)throws InputXcpt{

	    float tep = (float)o[tc]; //total expected to pay

	    float tc, grs;

	    if ((int)o[fll] == 0) {//full
		tc = tep;
		grs = tep - (float)o[res];
	    } else { //# dont let these have a reserve(ehh not sure about that)
		tc = 0f;
		grs = tep;
	    }
	    
	    float  sum;
	    sum = (int)o[nop] * (float)o[aop] + (float)o[fpa];
	    if (Math.abs(sum - tep) > 0.001f)
		throw new InputXcpt(""+sum+" != "+tep+"\nPayment summation does not equal total");

	    try {
		//System.err.println("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		ResultSet rs = SQLBot.bot.query("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		if (rs.next()){
		    int id = rs.getInt(1);
		    //# move this to verification area
		    if (rs.next()) throw new InputXcpt("WARNING: Multiple cars match that VIN number");  
		    new FloorPayDialog(id, (LocalDate)o[sd]);
		}} catch (SQLException e){throw new InputXcpt(e);}

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
	public FloorPayDialog(int id, LocalDate date){
	    Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "Vehicle", "Item Cost", "Title"});
	    JTable jt;
	    Grid g;
	    JPanel jp = new JPanel(new BorderLayout());
	    View v;
	    try {
		Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE ID="+id+';');
		g = new Grid(key, in);

		v = g.addView(new String[]{"Title"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
								    new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		    new FloorPay.Ent(key, LocalDate.now())); //# working here
		v.addTable();

		jt = (JTable)g.go();
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    } catch (Exception e){ new XcptDialog(ContractForm.this, e); return;}

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    JPanel cPan = new JPanel();
	    jp.add(cPan, BorderLayout.SOUTH);
	    
	    JButton jb = new JButton("Pay Car Off");
	    cPan.add(jb, BorderLayout.SOUTH);
	    jb.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			int tl = key.dex("Title");
			int id = key.dex("ID");
			float st = (float)v.get("Sub total", 0);
			try {
			    LocalDate d = date; 
			    
			    Object[] o = g.data.get(0); //# dangerouts buts its just getting the row
			    
			    SQLBot.bot.update("UPDATE Cars SET Title="+((int)o[tl]+2)+", Date_Paid='"+d+"', Pay_Off_Amount="+st+" WHERE ID="+o[id]);
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
		  
	    setContentPane(jp);
	    setBounds(300,300,1000,600);
	    setVisible(true);
	}
    }

    private class ReserveField extends TextField {
	public ReserveField(TextField tot, Field type){
	    //# we don't know how type will affect this, ignore for now
	    super("Reserve at 10%");
	    
	    tot.addListener(new FieldListener(){
		    public void dew(){
			try {
			    float f = StringIn.parseFloat(tot.text())*.1f;
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
