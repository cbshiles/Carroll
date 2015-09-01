package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class MonthlyReport extends CenterFile{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{new ContractAccount(false), new ContractAccount(true), new FloorAccount(), new ReserveReport.ResAccount()});

	 PayInFact pif = new PayInFact();
	 doPif(0, pif); doPif(1, pif);
	dew(LocalDate.of(2015,1,1), LocalDate.now());
    }

    public void doPif(int i, PayInFact pif){
	((ContractAccount)accounts[i]).addPif(pif);
    }

    public class PurchaseAccount extends Account{
//starting balance 0
	//includes floorplan & contract purchases
	    public float getStart(LocalDate ld) throws Exception{
		return 0f;
	    }
    }
    
 }
