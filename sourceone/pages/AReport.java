package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class AReport extends FullnessPage{

    JButton jb;

    boolean firstTable = true;

    @Override
    public void getTable(){
	super.getTable();
//	jt.setRowSelectionAllowed(false);
	g.view.floatSum("Remaining Balance");

	if (firstTable){
	    balSum.setText(""+(thing1 = g.view.floatSum("Remaining Balance")));
	    firstTable = false;
	}
	totSum.setText(""+(thing2 = g.view.floatSum("Total Amount Due")));
    }
    
    public AReport(Page p)throws InputXcpt{
	super("Create AR Report", p);

	if (ded) return;

	reportDate = LocalDate.now();
	reload();

	sourceone.fields.TextField payDay;
	payDay = new sourceone.fields.TextField("Report for:", BasicFormatter.cinvert(reportDate));

	JPanel cPan = new JPanel();
	cPan.setLayout(new GridLayout(2, 0));
	
	JPanel bPan = new JPanel();
	bPan.setBorder(new javax.swing.border.EmptyBorder(10,10,10,20));
	bPan.setLayout(new GridLayout(0, 8));
	addEmpties(5, bPan);
	bPan.add(balSum = new JTextField());
	addEmpties(1, bPan);
	bPan.add(totSum = new JTextField());
	cPan.add(bPan);

	JPanel aPan = new JPanel();
	aPan.add(payDay.getJP());
	aPan.add(jb = new JButton("Create Report"));
	cPan.add(aPan);
	
	jp.add(cPan, BorderLayout.SOUTH);
	

	payDay.addListener(new FieldListener() {
		public void dew() {
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
			reportDate = StringIn.parseDate(payDay.text());
			View pView = g.view.addView(new String[]{"Payments Made"}, null, null);

			g.view.push1();

			pView.addOut(new CustReport(pView.key, SQLBot.bot.path+"AR_Report_"+sel+'_'+reportDate+".csv", "\nTotals:~~~~"+thing1+"~~"+thing2));

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
		    {new XcptDialog(AReport.this, x); x.printStackTrace(); }
		}});
	
	getTable();
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
