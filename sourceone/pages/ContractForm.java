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
    public ContractForm() throws Exception{
	super("Contract");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new java.awt.GridLayout(0, 1));

	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Phone Number"));

	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount"));
	addF(new OptionField("Final Payment", "0", true));
	
	addF(new RadioField("Payment Frequency",
			    new String[]{"Weekly", "Biweekly", "Monthly"},
			    new String[]{"7", "14", "30"}));

	addF(new OptionField("Total Contract", "0", true));

	addF(new TextField("Start Date"));

	addF(new TextField("Vehicle"));
	addF(new TextField("VIN"));

	addF(new TextField("Reserve"));
	addF(new TextField("Gross Amount"));
	addF(new TextField("Net Amount"));

	JButton submit = new JButton("Submit");

	Key custKey = Key.customerKey.accept(new String[]{"ID", "email"});
	Grid custGrid = new Grid(custKey, new StringIn(this));
	custGrid.addView(null, null, null);
	custGrid.view.addOut(new SQLFormatter(new InsertDest(custGrid.view.key, "Customers", true)));

	
	Key contKey = Key.contractKey.accept(new String[]{"ID",  "Next Due", "Paid Off", "Other Payments", "Customer ID", "Payments Made"});
	Ent ent = new Ent(contKey);
	Grid contGrid = new Grid(contKey, new StringIn(this));
	contGrid.addView(null, new Cut[]{new DateCut("Next Due"), new FloatCut("Other Payments"), new IntCut("Customer ID")},
			 ent);
	contGrid.view.addOut(new SQLFormatter(new InsertDest(contGrid.view.key, "Contracts", true)));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			ContractForm.this.refresh();
			ent.set_id((int)custGrid.go());
			if ((int)contGrid.go() == -1)
			    throw new InputXcpt("SQL insertion unsuccessful");

		    } catch (InputXcpt ix) {
			new XcptDialog(ContractForm.this, ix);
		    }}});

	add(submit);
	pack();
	setVisible(true);
    }


    private class Ent implements Enterer{
	
	int sd, aop, nop, fpa, tc, grs;
	int cust_id, vin;
	//"Reserve", "Gross Amount", "Net Amount",
	
	public Ent(Key k){
	    sd = k.dex("Start Date");
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    fpa = k.dex("Final Payment Amount");
	    tc = k.dex("Total Contract");
	    grs = k.dex("Gross Amount");
	    vin = k.dex("VIN");
	}

	public void set_id(int i){cust_id=i;}
    
	public Object[] editEntry(Object[] o)throws InputXcpt{

	    float tep; //total expected to pay
	    float tcO = (float)o[tc];
	    if (tcO < .001)
		tep = (float)o[grs];
	    else
		tep = tcO;
	    
	    float  sum;
	    sum = (int)o[nop] * (float)o[aop] + (float)o[fpa];
	    if (Math.abs(sum - tep) > 0.001f)
		throw new InputXcpt(""+sum+" != "+tep+"\nPayment summation does not equal total");

	    try {
		System.err.println("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		ResultSet rs = SQLBot.bot.query("SELECT ID FROM Cars WHERE VIN LIKE '"+o[vin]+"' AND Date_Paid IS NULL;");
		if (rs.next()){
		    int id = rs.getInt(1);
		    System.out.println("It made it!!");
		    //# move this to verification area
		    if (rs.next()) throw new InputXcpt("WARNING: Multiple cars match that VIN number");  
		    new FloorPayDialog(id);
		}} catch (SQLException e){throw new InputXcpt(e);}

	    return new Object[]{
		o[sd], //Next Due
		0f, //Other payments
		cust_id
	    };
	}
    }

    public class FloorPayDialog extends JDialog{
	public FloorPayDialog(int id){
	    Key key = Key.floorKey.accept(new String[]{"VIN","Date Paid"});
	    JTable jt;
	    Grid g;
	    JPanel jp = new JPanel();
	    try {
		Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE ID="+id+';');
		g = new Grid(key, in);

		View v = g.addView(new String[]{"Title"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
								    new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		    new FloorPay.Ent(key));
		v.addTable();

		jt = (JTable)g.go();
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    } catch (Exception e){ new XcptDialog(ContractForm.this, e); return;}

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    JPanel cPan = new JPanel();
	    jp.add(cPan, BorderLayout.SOUTH);
	    
	    JButton jb = new JButton("Pay Car Off");
	    cPan.add(jb);//, BorderLayout.SOUTH);
	    jb.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			int tl = key.dex("Title");
			int id = key.dex("ID");
			try {
			    LocalDate d = LocalDate.now(); //# Should this be today?
			    
			    Object[] o = g.data.get(0);
			    
			    SQLBot.bot.update("UPDATE Cars SET Title="+((int)o[tl]+2)+", Date_Paid='"+d+"' WHERE ID="+o[id]);
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
	    setBounds(500,500,600,420);
	    setVisible(true);
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
