package grafika;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame {
	public GlavnoOkno() {
		super("Telefonski imenik");
		JPanel vsebina = new VsebinskoPodrocje();
		add(vsebina);
	}
}
