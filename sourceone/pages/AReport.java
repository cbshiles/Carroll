package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class AReport extends FullnessPage{

    JButton jb;
    
    public AReport(){
	super("Create AR Report");

	LocalDate today = LocalDate.now();
	getTable(today);
	sourceone.fields.TextField payDay;
	payDay = new sourceone.fields.TextField("Report for:", BasicFormatter.cinvert(today));

	JPanel cPan = new JPanel();
	cPan.add(payDay.getJP());
	cPan.add(jb = new JButton("Create Report"));
	jp.add(cPan, BorderLayout.SOUTH);

	payDay.addListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {warn();}

		public void removeUpdate(DocumentEvent e) {warn();}

		public void insertUpdate(DocumentEvent e) {warn();}

		public void warn() {
		    try {
			LocalDate d = StringIn.parseDate(payDay.text());
			getTable(d);
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		}});

	jb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try {
			View pView = g.view.addView(new String[]{"Payments Made"}, null, null);

			g.view.push1();
			float thing1 = pView.floatSum("Remaining Balance");
			float thing2 = pView.floatSum("Total Amount Due");
			    
			Key pKey = new Key(new Cut[]{new StringCut("Last name"), new StringCut("First name")});
			pKey = pKey.add(pView.key.accept(new String[]{"Customer Name"}).cuts);
			pView.addOut(new CustReport(pKey, "AR_Report_"+reportDate+".csv", ",,,,,"+thing1+",,"+thing2));
			pView.push();

			LocalDate prd = SQLBot.bot.query1Date("SELECT "+sel+"_Report_Date FROM Meta WHERE ID=1;");

			boolean doit = true;
			if (prd != null){
			    ConfirmDialog pop = new ConfirmDialog(AReport.this, "Report Overwrite", ""+prd);
			    doit = pop.confirmed();
			}

			if (doit)
			    SQLBot.bot.update("UPDATE Meta SET "+sel+"_Report_Date='"+reportDate+"' WHERE ID=1;");

		    }catch (Exception x)
		    {x.printStackTrace(); 
			System.err.println("~?~ "+x);}
		}});
		
    }
    private class ConfirmDialog extends JDialog implements ActionListener{

	boolean ret;
	String date;
	JPanel jp = new JPanel();
	
	public ConfirmDialog(Frame f, String name, String d){
	    super(f, name, Dialog.ModalityType.DOCUMENT_MODAL);
	    date = d;
	}

	void makeButton(String name, String cmd){
	    JButton jb = new JButton(name);
	    jb.setActionCommand(cmd);
	    jb.addActionListener(this);
	    jp.add(jb);
	}

	boolean confirmed(){
	    jp.add(new JLabel("This will be overwriting a previous report from "+date+". Is this ok?"));
	    makeButton("Overwrite", "T");
	    makeButton("Cancel", "F");

	    getContentPane().add(jp);
	    setBounds(500,500,500,150);
	    setVisible(true);

	    return ret;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
	    String action = ae.getActionCommand();
	    ret = action.equals("T");
	    dispose();
	}
    }
}
