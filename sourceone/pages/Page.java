package sourceone.pages;

import sourceone.Driver;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class Page extends JFrame {

    public static Rectangle dim; 
    public static final Color bkgd = new Color(190, 190, 240);
    private boolean busy = false, isParent = false;
    Page parent;
    ArrayList<Page> children = new ArrayList<Page>();
//    String name;

    public Page(String name, Page p) {
	super(name); //this.name = name;
	parent = p;
	if (parent != null) parent.addChild(this);
	Driver.addPage();
	
	addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
		    removeChildren();

		    if (parent != null) {
			parent.setBusy(false);
			parent.dropChild(Page.this);
		    }
		    Driver.removePage();
		}
	    });

	getContentPane().setBackground(bkgd);
    }

    public void addChild(Page p){
	children.add(p);
	setParent(true);
    }

    public void dropChild(Page p){
	children.remove(p);
    }

    public void removeChildren(){
	for (int i = children.size()-1; i>=0; i--)
	    children.get(i).kill();
	children = new ArrayList<Page>();
	setParent(false);
    }

    protected <T extends Page> void addRed(String n, Class<T> t, boolean lk){ //add a redirect, for menus
	JButton jb = new JButton(n);
	add(jb);
	jb.addActionListener(new PageMaker(this, t, lk));
    }
    
    protected <T extends Page> void addRed(String n, Class<T> t){ //add a redirect, for menus
	addRed(n, t, true);
    }

    protected void kill(){
	dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public boolean isBusy(){
	return busy;
    }

    public boolean isParent(){
	return isParent;
    }

    public void setBusy(boolean b){
	busy = b;
	if (parent != null) parent.setBusy(b);
    }

    public void setParent(boolean b){
	isParent = b;
//	if (parent != null) parent.setParent(b);
    }

    public void place(float x, float y, float w, float h){
	setBounds((int)(x*dim.width+dim.x), (int)(y*dim.height+dim.y), (int)(w*dim.width), (int)(h*dim.height));
    }

    public void tablePlace(){
	place(.05f, .15f, .7f, .7f);
    }
}
