package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import static sourceone.pages.blobs.ContractAccount.*;
import java.time.*;
import java.util.ArrayList;
public class PayInFact{ //returns different inputs, but using same data
	
    ArrayList<BuildIn[]> bins = new ArrayList();
    BuildIn fullIn, partIn;
	
    public PayInFact(){
	fullIn = partIn = null;
    }

    BuildIn which(int cid)throws Exception{
	float tc = SQLBot.bot.query1Float("SELECT Total_Contract FROM Contracts WHERE ID="+cid);
	return (tc > .01)?fullIn:partIn;
    }

    public BuildIn find(LocalDate a, LocalDate z, boolean full){
	for(int x=bins.size()-1; x>=0; x--){
	    if (bins.get(x)[0].sameTime(a, z)){
		return bins.get(x)[full?0:1];
	    }
	}
	return null;
    }

    public Input in(LocalDate ao, LocalDate zo, boolean full)throws Exception{//no nulls
	BuildIn b = find(ao, zo, full);
	if (b != null) return new ViewInput(b);
	else {
	    Grid g = new Grid(Key.paymentKey, new QueryIn
			      (Key.paymentKey, "WHERE Day >= '"+ao+"' AND Day <= '"+zo+"' ORDER BY Day"));
	    g.pull();

	    fullIn = new BuildIn(true,ao,zo);
	    partIn = new BuildIn(false,ao,zo);
		
	    for (Object[] i: g.data){

		//#should never be a problem, but batch cant be someone's name
		//bckwrds search quicker
		if (i[4] != null){
		    Quail qq = new Quail((LocalDate)i[2], (float)i[3], (String)i[4]);

		    if (! fullIn.add(qq)){
			if (! partIn.add(qq)){
			    which((int)i[1]).gnu(qq);
			}
		    }
		} else {
		    String zename = SQLBot.bot.query1Name("SELECT Customers.First_Name, Customers.Last_Name FROM Customers, Contracts WHERE Contracts.ID="+(int)i[1]+" AND Customers.ID=Contracts.Customer_ID");
		    which((int)i[1]).chunk(new Object[]{(LocalDate)i[2], zename+" - Payoff", 0f, (float)i[3], 0f});
		}

	    }
	    fullIn.load(); partIn.load();
	    bins.add(new BuildIn[]{fullIn, partIn});
	}
	return new ViewInput(full?fullIn:partIn);		
    }
	
}
