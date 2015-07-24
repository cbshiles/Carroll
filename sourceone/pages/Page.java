package sourceone.pages;

import sourceone.Driver;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.*;

public class Page extends JFrame {

    public static final Color bkgd = new Color(190, 190, 240);
    private boolean busy = false;
    Page parent;
    ArrayList<Page> children = new ArrayList<Page>();

    public Page(String name, Page p) {
	super(name);
	parent = p;
	if (parent != null) parent.addChild(this);
	Driver.addPage();
	
	addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
		    Driver.removePage();
		    if (parent != null) parent.setBusy(false);
		    removeChildren();
		}
	    });

	getContentPane().setBackground(bkgd);
    }

    public void addChild(Page p){
	children.add(p);
    }

    public void removeChildren(){
	for (Page p : children){
	    p.dispatchEvent(new WindowEvent(p, WindowEvent.WINDOW_CLOSING));
	}
	children = new ArrayList<Page>();
    }

    protected <T extends Page> void addRed(String n, Class<T> t){ //add a redirect, for menus
	JButton jb = new JButton(n);
	add(jb);
	jb.addActionListener(new PageMaker(this, t));
    }

    protected void kill(){
	dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public boolean isBusy(){
	return busy;
    }

    public void setBusy(boolean b){
	busy = b;
    }
}
