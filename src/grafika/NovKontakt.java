package grafika;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import baza.Baza;

@SuppressWarnings("serial")
public class NovKontakt extends JDialog {
	private Dimension velikost = new Dimension(300, 180);
	protected JTextField[] textfields = new JTextField[6]; 
	private Baza baza;
	protected int id;  // id kontakta v bazi, ki ga posodabljamo; -1, ce dodajamo nov kontakt
	protected JButton shrani;
	
	static protected final String SHRANI = "shrani";

	public NovKontakt(JFrame parentFrame, String title, Baza danaBaza, int daniId) {
		super(parentFrame, title, true);
		baza = danaBaza;
		id = daniId;
		
		JPanel vsebina = new JPanel();
		add(vsebina);
		
		vsebina.setPreferredSize(velikost);
		vsebina.setLayout(new GridBagLayout());
		vsebina.setBackground(new Color(231, 219, 255));
		
		GridBagConstraints c = new GridBagConstraints();
		JLabel imeLabel = new JLabel("Ime: *");
		JLabel priimekLabel = new JLabel("Priimek: *");
		JLabel stevilkaLabel = new JLabel("Telefonska številka: *");
		JLabel naslovLabel = new JLabel("Naslov: ");
		JLabel krajLabel = new JLabel("Kraj: ");
		JLabel postaLabel = new JLabel("Poštna številka: ");
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		vsebina.add(imeLabel, c);
		c.gridy = 1;
		vsebina.add(priimekLabel, c);
		c.gridy = 2;
		vsebina.add(stevilkaLabel, c);
		c.gridy = 3;
		vsebina.add(naslovLabel, c);
		c.gridy = 4;
		vsebina.add(krajLabel, c);
		c.gridy = 5;
		vsebina.add(postaLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		
		for (int i = 0; i < textfields.length; i++) {
			textfields[i] = new JTextField();
			if (id > 0) {
				String[] danKontakt = baza.pridobiKontakt(id);
				textfields[i].setText(danKontakt[i]);
			}
			c.gridy = i;
			vsebina.add(textfields[i], c);
		}
		
		c = new GridBagConstraints();
		JLabel obvezna_polja = new JLabel("Polja z * so obvezna!");
		System.out.println(obvezna_polja.getFont().getFontName());
		obvezna_polja.setFont(new Font("Dialog", Font.PLAIN, 10));
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 6;
		vsebina.add(obvezna_polja, c);
		
		c = new GridBagConstraints();
		shrani = new JButton("Shrani");
		shrani.setActionCommand(SHRANI);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		vsebina.add(shrani, c);		
	}
}