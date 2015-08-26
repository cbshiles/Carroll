package sourceone.pages;

import sourceone.key.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class FloorCalc implements Enterer {
    int tit, cd, db, ic;
    boolean total;
    LocalDate theDay;
    
    public FloorCalc(Key k, LocalDate ld, boolean total){ //are we looking for total cost or just curtailment price?
	this.total = total;
	theDay = ld;
	tit = k.dex("Title");
	db = k.dex("Date Bought");
	ic = k.dex("Item Cost");
	cd = k.dex("Curtailed");
    }
    public Object[] editEntry(Object[] o){
	LocalDate bot = (LocalDate)o[db];
	float cost = (float)o[ic];

	float dRate = View.rnd(cost*.0007f); 
	int days = (int)ChronoUnit.DAYS.between(bot, theDay);

	float tmp = dRate*days;
	float interest, fees;
	if ((int)o[cd] == 1){ //curtailed entry
	    interest = tmp;
	    fees = 0f;
	} else {
	    int min = (cost >= 5000)?65:35;
	    interest = tmp>min?tmp:min;
	    fees = 25f;
	}
	return new Object[]{
	    titleStatus((int)o[tit]),
	    dRate,
	    days,
	    interest,
	    fees,
	    interest+fees+(total?cost:cost*.1f) //# hard coded 10%
	};
    }
    public String titleStatus(int t){
	switch(t){
	 case 0: return "Pending";
	 case 1: return "Yes";
	 case 2: return "Never";
	 case 3: return "Nope";
	}
	return "Invalid code";
    }

    public void setDay(LocalDate ld) {theDay= ld;}
}
