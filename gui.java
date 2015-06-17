import javax.swing.*;

public class gui {
    public static void main(String[] args){
	JFrame f = new JFrame("Demo GUI");
	f.setSize(400,600);

	JButton bb = new JButton("Belly");
	f.add(bb);
	bb.addActionListener(new AListener());


	f.setLocation(200, 100);
	f.setVisible(true);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//will actually want DISPOSE_ON_CLOSE, i believe

    }
}
