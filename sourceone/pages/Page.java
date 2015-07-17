package sourceone.pages;

import sourceone.Driver;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;

public class Page extends JFrame {

    public static final Color bkgd = new Color(190, 190, 240);

    public Page(String name) {
	super(name);
	Driver.addPage();
	
	addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
		    Driver.removePage();
		}
	    });

	getContentPane().setBackground(bkgd);
    }

    protected <T extends Page> void addRed(String n, Class<T> t){
	JButton jb = new JButton(n);
	add(jb);
	jb.addActionListener(new PageMaker(this, t));
    }
}
