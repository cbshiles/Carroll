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
import java.time.temporal.ChronoUnit;

public class PayContracts extends Page {

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    
    public PayContracts(){
	super("Pay Contracts");
	setSize(400, 600);
	try{
	    Key key = Key.contractKey.just(new String[] {
		    "ID", "Last Name", "First Name", "Number of Payments", "Amount", "Final Payment",
		    "Payment Frequency", "Total of Payments", "Start Date", "Payments made", "Paid off day", "Next Due"});
	    
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Contracts WHERE Next_Due < CURDATE()");

	    Grid g = new Grid(key, in);

	    g.clearView(new Key(
			    new String[]{"Customer Name", "Terms", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Payment"},
			    new Kind[]{STRING, STRING, DATE, DATE, FLOAT, INT, FLOAT}).cuts,
			new Ent(key));
	    g.view.addTable();
	    jp.add(new JScrollPane(jt = (JTable)g.go()), BorderLayout.NORTH);

	    JPanel cPan = new JPanel();

	    Field payDay = new sourceone.fields.TextField("Date paid:");

	    cPan.add(payDay.getJP());

	    cPan.add(jb = new JButton("pay emmm"));

	    jp.add(cPan, BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    Enterer ec = new Click(key, StringIn.parseDate(payDay.text()));
			    for (int i : jt.getSelectedRows())
				ec.editEntry(g.data.get(i));
			} catch (Exception x)
			{System.err.println("Buttons, YO: "+x.getCause()+x.getClass().getName());
			    System.err.println(x.getMessage());}
		    }});
	} catch (Exception e)
	{System.err.println("YO!: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }

    private LocalDate next(int freq, LocalDate ld){
	LocalDate due;
	if (freq == 30) due = ld.plusMonths(1);
	else due = ld.plusDays(freq);
	return due;
    }

    private class Click  implements Enterer{

	int id, fq, nd, pm;
	LocalDate paid;
	
	public Click(Key k, LocalDate p){
	    paid = p;
	    fq = k.dex("Payment Frequency");
	    nd = k.dex("Next Due");
	    pm = k.dex("Payments made");
	    
	}

	public Object[] editEntry(Object[] o){
	    int pays = (int)o[pm]+1;
	    LocalDate due = next((int)o[fq], (LocalDate)o[nd]);
	    try {
	    //System.out.println
	    SQLBot.bot.update("UPDATE Contracts SET Payments_made="+pays+", Next_Due='"+due+"' WHERE ID="+o[0]+';');
	    SQLBot.bot.update("INSERT INTO Payments (Contract_ID, Day) VALUES ("+o[0]+", '"+paid+"');");
	    } catch (Exception x)
	    {System.err.println("Tiresd of dese: "+x.getCause()+x.getClass().getName());
		System.err.println(x.getMessage());}
	    return null;
	}
    }
    
    private class Ent implements Enterer{

	int fq, nd, am, np, pm, ln, fn, sd, fp, tp;
	    
	public Ent(Key k){
	    fq = k.dex("Payment Frequency");
	    nd = k.dex("Next Due");
	    am = k.dex("Amount");
	    np = k.dex("Number of Payments");
	    pm = k.dex("Payments made");
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    sd = k.dex("Start Date");
	    fp = k.dex("Final Payment");
	    tp = k.dex("Total of Payments");
	}

	public Object[] editEntry(Object[] o){
	    int nPays, freq = (int)o[fq];
	    LocalDate due = (LocalDate)o[nd];
	    float amt = (float)o[am];

	    int tmp = numPays(freq, due);
	    int maxPays = (int)o[np] - (int)o[pm];
	    nPays = (tmp<maxPays)?tmp:maxPays;

	    return new Object[] {
		(String)o[ln]+", "+o[fn],
		terms((int)o[np], amt, freq),
		o[sd],
		due,
		(float)o[tp] - (int)o[pm]*amt,
		nPays,
		nPays*amt + (float)o[fp] //Final payment
	    };
	}
	
	public int numPays(int freq, LocalDate due){
	    int pays = 0;
	    LocalDate today = LocalDate.now();
	    while (today.compareTo(due) > 0){
		due = next(freq, due);
		pays++;
	    }
	    return pays;
	}


	public String terms(int num, float amt, int freq){
	    char c;
	    if (freq==7) c='W';
	    else if (freq ==14) c='B';
	    else c='M';
	    return ""+num+" "+c+" @ "+amt;
	}
    }

}
