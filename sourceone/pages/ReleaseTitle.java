package sourceone.pages;

public class ReleaseTitle extends TitlePage {

    public ReleaseTitle(Page p){
	super(p, "Titles We Have:", "Title=1 AND Pay_Off_Amount IS NOT NULL", "Release Title", 3);
    }
}