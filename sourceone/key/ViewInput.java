package sourceone.key;

public class ViewInput implements Input{
    private View v;
    private int x,y;
    
    public ViewInput(View vw){v=vw; y=-1;}

    public boolean hasEntries(){
	x=0;
	return ++y < v.data.size();
    }

    public String getString() throws InputXcpt{
	return (String)v.data.get(y)[x++];
    }


    public int getInt() throws InputXcpt{
	return (int)v.data.get(y)[x++];
    }


    public float getFloat() throws InputXcpt{
	return (float)v.data.get(y)[x++];
    }


    public java.time.LocalDate getDate() throws InputXcpt{
	return (java.time.LocalDate)v.data.get(y)[x++];
    }


    public void done(){y=-1;}
    
}
