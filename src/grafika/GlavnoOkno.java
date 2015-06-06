package grafika;

import javax.swing.JFrame;
import javax.swing.JPanel;

import baza.Baza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame {
	public GlavnoOkno() {
		super("Telefonski imenik");
		final Baza baza = new Baza();
		baza.poveziSeZBazo();
		JPanel vsebina = new VsebinskoPodrocje((JFrame) this, baza);
		add(vsebina);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	baza.zapriPovezavo();
		    	System.exit(0);
		    }
		});
	}
}
