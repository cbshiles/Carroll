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

    @Override
    public void getTable(){
	super.getTable();
	jt.setRowSelectionAllowed(false);
    }
    
    public AReport(){
	super("Create AR Report");

	if (ded) return;

	reportDate = LocalDate.now();
	getTable();

	sourceone.fields.TextField payDay;
	payDay = new sourceone.fields.TextField("Report for:", BasicFormatter.cinvert(reportDate));

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
			reportDate = StringIn.parseDate(payDay.text());
			reload();
			getTable();
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		    catch (Exception e) {new XcptDialog(AReport.this, e);}
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
			pView.addOut(new CustReport(pKey, SQLBot.bot.path+"AR_Report_"+sel+'_'+reportDate+".csv", ",,,,,"+thing1+",,"+thing2));

			boolean doit = true;
			if (prd != null){
			    ConfirmDialog pop = new ConfirmDialog(AReport.this, "Report Overwrite", ""+prd);
			    doit = pop.confirmed();
			}

			if (doit){
			    pView.push();
			    SQLBot.bot.update("UPDATE Meta SET "+sel+"_Report_Date='"+reportDate+"' WHERE ID=1;");
			}
			kill();
		    }catch (Exception x)
		    {x.printStackTrace(); 
			System.err.println("~?~ "+x);}
		}});
	wrap();
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
