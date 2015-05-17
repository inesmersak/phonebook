package grafika;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import baza.Baza;

@SuppressWarnings("serial")
public class NovKontakt extends JFrame implements ActionListener {
	private Dimension velikost = new Dimension(300, 180);
	private JTextField[] textfields = new JTextField[6]; 
	private Baza baza;
	private boolean novKontakt;  // true pomeni, da dodajamo nov kontakt; false, da posodabljamo starega
	
	static private final String SHRANI = "shrani";

	public NovKontakt(String title, Baza danaBaza, boolean novo) {
		super(title);
		baza = danaBaza;
		novKontakt = novo;
		JPanel vsebina = new JPanel();
		add(vsebina);
		
		vsebina.setPreferredSize(velikost);
		vsebina.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		JLabel imeLabel = new JLabel("Ime: ");
		JLabel priimekLabel = new JLabel("Priimek: ");
		JLabel stevilkaLabel = new JLabel("Telefonska številka: ");
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
			c.gridy = i;
			vsebina.add(textfields[i], c);
		}
		
		c = new GridBagConstraints();
		JButton shrani = new JButton("Shrani");
		shrani.setActionCommand(SHRANI);
		shrani.addActionListener(this);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 6;
		vsebina.add(shrani, c);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(SHRANI)) {
			String[] noviPodatki = new String[textfields.length];
			for (int i = 0; i < textfields.length; i++) {
				noviPodatki[i] = textfields[i].getText();
			}
			if (novKontakt) {
				baza.dodajKontakt(noviPodatki);
			} else {
				// baza.posodobiKontakt(stariPodatki, noviPodatki);
			}
			dispose();
		}
	}
	
}
