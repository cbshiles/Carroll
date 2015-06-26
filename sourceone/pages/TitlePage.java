package sourceone.pages;
import com.sourceone.fields.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitlePage extends Page {

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    //TitleTable tt = new TitleTable();
    CSVReadTable tt = new CSVReadTable();
    
    public TitlePage(){
	super("Pending Titles");
	setSize(400, 600);
	try{
	    jt = tt.makeTable();

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    
	    jp.add(jb = new JButton("tietle emmm"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    // jb.addActionListener(new ActionListener() {
	    // 	    public void actionPerformed(ActionEvent e) {
	    // 		tt.giveTitle(jt.getSelectedRows());
	    // 	    }
	    // 	});
	    
	} catch (Exception e)
	{System.err.println("YO!: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }
}
