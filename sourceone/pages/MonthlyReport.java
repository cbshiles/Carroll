package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class MonthlyReport extends CenterFile{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{new ContractAccount(true), new FloorAccount(), new ReserveReport.ResAccount()});
	dew(LocalDate.of(2015,3,1), LocalDate.now());
    }

 }
