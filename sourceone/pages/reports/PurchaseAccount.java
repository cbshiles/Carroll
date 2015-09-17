package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurchaseAccount extends Account{

    public PurchaseAccount(){super("Purchases", new Blob[]{/*new FloorBlob(true),*/ new PurBlob(true), new PurBlob(false)});} 

    public float getStart(LocalDate ld)throws Exception{
	return 0f;
    }
}
