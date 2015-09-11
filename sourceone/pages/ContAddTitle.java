package sourceone.pages;
import sourceone.key.*;
public class ContAddTitle extends ContTitle {

    public ContAddTitle(Page p)throws Exception{
	super(p, "Pending Contract Titles", "Title=0 OR Title IS NULL ORDER BY VIN", "Add Title", 1);
    }
}
