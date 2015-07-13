package sourceone.pages;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import static sourceone.key.Kind.*;
import java.time.*;

public class PayOff extends Page {
    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    JScrollPane jsp = new JScrollPane();

    public PayOff(){
	super("Pay Off Contract");
	setSize(1000, 600);
	jp.add(jb = new JButton("Select Contract"), BorderLayout.SOUTH);
	
	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	Key contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Gross Amount", "Net Amount"});

	try{
	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due IS NOT NULL AND Contracts.Customer_ID = Customers.ID");

	    Key inKey = custKey.add(contKey.cuts);
		    
	    Grid g = new Grid(inKey, in);
	    Key tableKey = new Key(
		new String[]{"Customer Name", "Start Date", "Terms", "Payments Made", "Remaining Balance"},
		new Kind[]{STRING, DATE, STRING, INT, FLOAT});

	    View tableView = g.clearView(tableKey.cuts, new Ent(inKey));

	    tableView.addTable();

	    	jsp.setViewportView(jt = (JTable)g.go());
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    jb.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			int[] dx = jt.getSelectedRows();
			if (dx.length > 0){
			    new PayOffDialog(PayOff.this, g.data.get(dx[0]), inKey);
			}
		    }
		});
		    
		    
	} catch (Exception ix) {
	    new XcptDialog(PayOff.this, ix);
	}

	jp.add(jsp, BorderLayout.NORTH);	

	setContentPane(jp);
	setVisible(true);
    }

    private class PayOffDialog extends JDialog implements ActionListener{

	Object[] o;
	Container pane;
	LocalDate sdO, payDate;
	int nopO, pfO, pmO, idO;
	float  aopO, fpaO, grsO, netO, tcO, fees, payoff=0;
	JTextArea jta;
/*	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	Key contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Gross Amount", "Net Amount"});
*/
	public void figure(){
	    float tep; //total expected to pay
	    if (tcO < .001)
		tep = grsO;
	    else
		tep = tcO;

	    float balance = tep - pmO*aopO;
	    String ret = "Balance: "+balance+'\n';

	    LocalDate endDate = nexti(sdO, pfO, nopO + ((fpaO > .001)?1:0));
	    long days = Period.between(sdO, endDate).getDays();
	    float dailyInt =  (grsO-netO)/days;
	    float discount = dailyInt * Period.between(payDate, endDate).getDays();

	    ret += "Discount: "+discount+'\n';

	    ret += "Fees: "+fees+'\n';

	    ret += "Pay Off: "+(payoff = balance-discount+fees);
	    jta.setText(ret);
	}

	public LocalDate nexti(LocalDate st, int freq, int i){
	    if (freq == 30) 
		return st.plusMonths(i);
	    else
		return st.plusDays(freq*i);
	}
	
	public PayOffDialog(Frame f, Object[] o, Key k){
	    super(f, "Pay Off for "+o[k.dex("First Name")]+o[k.dex("Last Name")], Dialog.ModalityType.DOCUMENT_MODAL);
	    this.o = o;

	    nopO = (int)o[k.dex("Number of Payments")];
	    aopO = (float)o[k.dex("Amount of Payment")];
	    fpaO = (float)o[k.dex("Final Payment Amount")];
	    
	    pfO = (int)o[k.dex("Payment Frequency")];
	    pmO = (int)o[k.dex("Payments Made")];
	    sdO = (LocalDate)o[k.dex("Start Date")];

	    grsO = (float)o[k.dex("Gross Amount")];
	    netO = (float)o[k.dex("Net Amount")];
	    tcO = (float)o[k.dex("Total Contract")];
	    idO = (int) o[k.dex("ID")];

	    pane = getContentPane();
	    GridBagLayout c = new GridBagLayout();
	    pane.setLayout(c);

	    cadd(new JLabel("Fees"), 0, 0, 1, 1);
	    JTextField feeField = new JTextField("50.00",10);
	    fees = 50f;
	    cadd(feeField, 1, 0, 1, 1);
	    feeField.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
			//System.out.println("changedUpdate");
			warn();
		    }
		    public void removeUpdate(DocumentEvent e) {
			//System.out.println("removeUpdate");
			warn();
		    }
		    public void insertUpdate(DocumentEvent e) {
			//System.out.println("insertUpdate");
			warn();
		    }

		    public void warn() {
			try {
			    float d = StringIn.parseFloat(feeField.getText());
			    fees = d;
			    figure();
			} catch (InputXcpt ix) {;}
		    }
		});
	    
	    cadd(new JLabel("Payoff Date"), 0, 1, 1, 1);
	    payDate = LocalDate.now();
	    JTextField dateField = new JTextField(BasicFormatter.cinvert(payDate), 10);

	    cadd(dateField, 1, 1, 1, 1);
	    dateField.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
			//System.out.println("changedUpdate");
			warn();
		    }
		    public void removeUpdate(DocumentEvent e) {
			//System.out.println("removeUpdate");
			warn();
		    }
		    public void insertUpdate(DocumentEvent e) {
			//System.out.println("insertUpdate");
			warn();
		    }

		    public void warn() {
			try {
			    LocalDate d = StringIn.parseDate(dateField.getText());
			    payDate = d;
			    figure();
			} catch (InputXcpt ix) {;}
		    }
		});

	    jta = new JTextArea(4,20);
	    jta.setEditable(false);
	    cadd(jta, 0, 2, 2, 2);

	    JButton payButton = new JButton("Pay Off");
	    cadd(payButton, 0, 4, 1, 1);
	    payButton.addActionListener(this);
	    payButton.setActionCommand("pay");

	    JButton backButton = new JButton("Go Back");
	    cadd(backButton, 1, 4, 1, 1);
	    backButton.addActionListener(this);
	    backButton.setActionCommand("back");
	    
	    setBounds(500,300,300,600);

	    figure();

	    setVisible(true);
	}

	private void cadd(JComponent jc, int x, int y, int width, int  height){
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = x;
	    gbc.gridy = y;
	    gbc.gridwidth = width;
	    gbc.gridheight = height;
	    pane.add(jc, gbc);
	}

	    @Override
    public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if (cmd.equals("back"))
		    dispose();
		else if (cmd.equals("pay")){
		    try {
			//System.err.println(
			//Next_Due
		    SQLBot.bot.update("UPDATE Contracts SET Paid_Off='"+payDate+"', Other_Payments="+payoff+", Next_Due=NULL WHERE ID="+idO+';');

		    SQLBot.bot.update("INSERT INTO Payments (Contract_ID, Day, Amount) VALUES ("+idO+", '"+payDate+"', "+payoff+");");
		    } catch (Exception e){System.err.println("~!~"+e);}
		}
		else {System.err.println("Kentucky derby");
		    System.exit(1);}
    }

    }

    private class Ent implements Enterer{
	int ln, fn, nop, aop, fpa, pf, pm, sd, grs, net, tc;

	public Ent(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");

	    nop = k.dex("Number of Payments");
	    aop = k.dex("Amount of Payment");
	    fpa = k.dex("Final Payment Amount");
	    
	    pf = k.dex("Payment Frequency");
	    pm = k.dex("Payments Made");
	    sd = k.dex("Start Date");

	    grs = k.dex("Gross Amount");
	    net = k.dex("Net Amount");
	    tc = k.dex("Total Contract");
	}
//			new String[]{"Customer Name", "Start Date", "Terms", "Payments Made", "Remaining Balance"},	
	public Object[] editEntry(Object[] o){

	    int pfO = (int)o[pf];
	    int nopO = (int)o[nop];
	    float fpaO = (float)o[fpa];
	    LocalDate sdO = (LocalDate)o[sd];
	    float aopO = (float)o[aop];

	    int pmO = (int)o[pm];

	    float tep; //Total expected payment 
	    float tcO = (float)o[tc];
	    if (tcO > 0.01)
		tep = tcO;
	    else
		tep = (float)o[grs];

	    return new Object[]{
		""+o[ln]+", "+o[fn],
		o[sd],
		terms(nopO, aopO, pfO, fpaO),
		pmO,
		tep - pmO*aopO
	    };
	}
	public String terms(int num, float amt, int freq, float fin){
	    char c;
	    if (freq==7) c='W';
	    else if (freq ==14) c='B';
	    else c='M';
	    String trms = ""+num+" "+c+" @ "+amt;
	    if (fin > 0.01)
		trms += " & 1 @ "+fin;
	    return trms;
	}
    }
}
