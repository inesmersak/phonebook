package grafika;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import baza.Baza;

@SuppressWarnings("serial")
public class VsebinskoPodrocje extends JPanel implements ActionListener, ListSelectionListener, 
DocumentListener {
	/**
	 * JFrame, ki vsebuje objekt tipa VsebinskoPodrocje
	 */
	private JFrame parentFrame;
	
	/**
	 * velikost panela
	 */
	private Dimension velikost = new Dimension(400, 300);
	
	private Baza baza;
	
	private JList<String> kontakti;
	
	/**
	 * ListModel za JList kontakti; en element je String z imenom in priimkom kontakta
	 */
	private DefaultListModel<String> model;
	private JEditorPane prikazKontakta;
	
	/**
	 * seznam vseh kontaktov; te pridobimo iz baze
	 */
	private Vector<String[]> seznamVsehKontaktov;
	
	/**
	 * seznam kontaktov, ki so trenutno prikazani na JListu; se spreminja glede na iskalno poizvedbo 
	 */
	private Vector<String[]> seznamTrenutnihKontaktov;
	
	private JTextField iskalnik;
	
	/**
	 * okno, ki se pojavi ob dodajanju/urejanju kontakta
	 */
	private NovKontakt oknoKontakta;
	
	// BARVE 
//	private Color barvaToolbara;
//	private Color barvaGumbov;
//	private Color barvaSeznamov;
	
	// ACTION COMMANDS
	static private final String DODAJ = "dodaj";
	static private final String UREDI = "uredi";
	static private final String IZBRISI = "izbrisi";
	
	public VsebinskoPodrocje(JFrame parent, Baza danaBaza) {
		super(new GridBagLayout());
		
		parentFrame = parent;
		
		baza = danaBaza;
		seznamVsehKontaktov = baza.izberiTabelo();
		seznamTrenutnihKontaktov = baza.izberiTabelo();  // na zacetku prikazemo vse kontakte
		
		// nastavi velikost panela
		setPreferredSize(velikost);
		
//		barvaToolbara = new Color(180, 175, 204);
//		barvaGumbov = new Color(215, 213, 224);
//		barvaSeznamov = new Color(215, 213, 224);
		
		
		/* ZACETEK TOOLBARJA */
		
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);  // toolbarja uporabnik ne more premikati
        toolbar.setRollover(true);
        toolbar.setLayout(new GridBagLayout());
//        toolbar.setBackground(barvaToolbara);
        
        // toolbar dodamo na panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
        add(toolbar, c);
        
        // ISKALNIK
        JLabel isci = new JLabel("Išči! ");
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.weightx = 1;
        c1.weighty = 1;
        c1.anchor = GridBagConstraints.CENTER;
        toolbar.add(isci, c1);
        
        iskalnik = new JTextField();
        iskalnik.getDocument().addDocumentListener(this);
        c1.gridy = 1;
        toolbar.add(iskalnik, c1);
        
        dodajGumbe(toolbar);
        
        /* KONEC TOOLBARJA */
        
        
        // JList vseh kontaktov
        String[] imena = dobiPolnaImena();
        model = new DefaultListModel<String>();
        for (int i = 0; i < imena.length; i++) {
        	model.addElement(imena[i]);
        }
        kontakti = new JList<String>(model);
        kontakti.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        kontakti.setSelectedIndex(0);
        kontakti.addListSelectionListener(this);
//        kontakti.setBackground(barvaSeznamov);
        
        // okvir s podatki kontakta
        prikazKontakta = new JEditorPane();
        posodobiPrikaz(0);
//        kontakti.setBackground(barvaSeznamov);
        
        JScrollPane kontaktiScrollPane = new JScrollPane(kontakti);
        JScrollPane prikazScrollPane = new JScrollPane(prikazKontakta);
        
        JSplitPane glavnoPodrocje = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kontaktiScrollPane, 
        		prikazScrollPane);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(glavnoPodrocje, c);
	}

	/**
	 * Iz polja, v katerem so shranjeni vsi kontakti, izlusci samo imena in priimke ter jih vrne.
	 * @return array stringov; vsak string je ime in priimek nekega kontakta
	 */
	private String[] dobiPolnaImena() {
		String[] polnaImena = new String[seznamVsehKontaktov.size()];
		int i = 0;
		for(String[] vrs : seznamVsehKontaktov) {
			String polnoIme = vrs[0] + " " + vrs[1];
			polnaImena[i] = polnoIme;
			i++;
		}
		return polnaImena;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(DODAJ)) {
			oknoKontakta = new NovKontakt(parentFrame, "Dodaj nov kontakt", baza, -1);
			// na gumb v novem frameu dodamo action listener, da bomo vedeli, kdaj je uporabnik 
			// koncal z dodajanjem kontakta
			oknoKontakta.shrani.addActionListener(this);
			oknoKontakta.pack();
			oknoKontakta.setVisible(true);
		} else if (cmd.equals(UREDI)) {
			// ime in priimek izbranega kontakta
			String izbranKontakt = kontakti.getSelectedValue();
			String[] kontakt = seznamTrenutnihKontaktov.elementAt(kontakti.getSelectedIndex());
			// id izbranega kontakta je na zadnjem mestu v arrayu, ki predstavlja kontakt
			int idIzbranegaKontakta = Integer.parseInt(kontakt[kontakt.length-1]);
			oknoKontakta = new NovKontakt(parentFrame, izbranKontakt, baza, idIzbranegaKontakta);
			oknoKontakta.shrani.addActionListener(this);
			oknoKontakta.pack();
			oknoKontakta.setVisible(true);
		} else if (cmd.equals(IZBRISI)) {
			int izbranIndeks = kontakti.getSelectedIndex();
			String izbranKontakt = kontakti.getSelectedValue();
			// prikazemo dialog, ki od uporabnika zahteva potrditev brisanja
			int potrditevBrisanja = JOptionPane.showOptionDialog(this.getParent(), 
					"Ali ste prepričani, da želite izbrisati kontakt " + izbranKontakt + "?", 
					"Izbriši " + izbranKontakt,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null, 
			        new String[]{"Da", "Ne"},
			        "default");
			if (potrditevBrisanja == JOptionPane.YES_OPTION) {
				String[] kontakt = seznamTrenutnihKontaktov.elementAt(izbranIndeks);
				baza.izbrisiKontakt(Integer.parseInt(kontakt[kontakt.length-1]));
				posodobiSeznam(true, 0);
			}
		} else if (cmd.equals(NovKontakt.SHRANI)) {
			String[] noviPodatki = new String[oknoKontakta.textfields.length];
			for (int i = 0; i < oknoKontakta.textfields.length; i++) {
				noviPodatki[i] = oknoKontakta.textfields[i].getText();
			}
			// preverimo, da so obvezna polja izpolnjena
			if (noviPodatki[0].length() == 0 || noviPodatki[1].length() == 0 || noviPodatki[2].length() == 0) {
				JOptionPane.showMessageDialog(oknoKontakta.getParent(), 
						"Polja 'Ime', 'Priimek' in 'Telefonska številka' so obvezna!", 
						"Manjkajoči podatki", JOptionPane.ERROR_MESSAGE);
			} else {
				int idZaPrikaz = 0;
				if (oknoKontakta.id < 0) {
					int id = baza.dodajKontakt(noviPodatki);
					if (id == -1) {
						JOptionPane.showMessageDialog(this, 
								"Dodajanje kontakta ni uspelo!", 
								"Kontakt ni bil dodan", JOptionPane.ERROR_MESSAGE);
					}
					else {
						idZaPrikaz = id;
					}
				} else {
					boolean uspesno = baza.posodobiKontakt(oknoKontakta.id, noviPodatki);
					if (uspesno) {
						idZaPrikaz = oknoKontakta.id;
					} else {
						JOptionPane.showMessageDialog(this, 
								"Urejanje kontakta ni uspelo!", 
								"Kontakt ni bil urejen", JOptionPane.ERROR_MESSAGE);
					}
				}
				posodobiSeznam(true, idZaPrikaz);
				oknoKontakta.dispose();  // zapremo okno za urejanje kontakta
			}
		} else {
			System.out.println("command not found");
		}
	}
	
	/**
	 * @param imeSlike
	 * @param actionCommand
	 * @param altTekst
	 * @return gumb z dano sliko in alt-tekstom
	 */
	private JButton narediGumb(String imeSlike, String actionCommand, String altTekst) {
		JButton gumb = new JButton();
		gumb.setActionCommand(actionCommand);
		gumb.setToolTipText(altTekst);
		gumb.addActionListener(this);
//		gumb.setBackground(barvaGumbov);
		
		String potSlike = "/" + imeSlike + ".png";
		URL urlSlike = VsebinskoPodrocje.class.getResource(potSlike);
		
		if (urlSlike != null) {
			gumb.setIcon(new ImageIcon(urlSlike, altTekst));
		} else {
			// ce slika ne obstaja, namesto slike prikazemo alternativni tekst
			gumb.setText(altTekst);
		}
		return gumb;
	}
	
	/**
	 * S pomocjo 'narediGumb' naredi gumbe in jih doda v dani toolbar. 
	 * @param toolbar
	 */
	private void dodajGumbe(JToolBar toolbar) {
		JButton gumb = null;
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.EAST;
		
		gumb = narediGumb("dodaj", DODAJ, "Dodaj številko");
		toolbar.add(gumb, c);
		
		c.gridx = 2;
		gumb = narediGumb("uredi", UREDI, "Uredi številko");
		toolbar.add(gumb, c);
		
		c.gridx = 3;
		gumb = narediGumb("izbrisi", IZBRISI, "Izbriši številko");
		toolbar.add(gumb, c);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<String> kliknjenSeznam = (JList<String>) e.getSource();
		int ind = kliknjenSeznam.getSelectedIndex();
		posodobiPrikaz(ind);
	}
	
	/**
	 * Ob kliku na drug kontakt zamenja podatke kontakta na desni strani.
	 * @param indeks Indeks elementa, ki je bil kliknjen.
	 */
	private void posodobiPrikaz(int indeks) {
		String[] izbranKontakt = seznamTrenutnihKontaktov.elementAt(indeks);
		String zaPrikaz = String.format("Ime: %s \n" +
				"Priimek: %s \n" +
				"Telefonska številka: %s \n" +
				"Naslov: %s \n" +
				"Kraj: %s \n" +
				"Poštna številka: %s \n", 
				(Object[]) izbranKontakt);
		prikazKontakta.setText(zaPrikaz);
	}
		
	/**
	 * Posodobi JList s kontakti. Ce so se zgodile spremembe v bazi, iskalno polje nastavi na prazno. 
	 * Sicer pri posodabljanju uposteva le kontakte, ki vsebujejo vzorec v iskalnem polju.
	 * @param spremembaBaze
	 * @param id
	 */
	private void posodobiSeznam(boolean spremembaBaze, int id) {
		if (spremembaBaze) { 
			// ce so se zgodile spremembe v bazi, moramo najprej posodobiti seznam vseh kontaktov
			seznamVsehKontaktov = baza.izberiTabelo();
			iskalnik.setText("");
			}
		String poizvedba = iskalnik.getText().toLowerCase();  // kar je trenutno napisano v polju isci
		DefaultListModel<String> novModel = new DefaultListModel<String>();
		seznamTrenutnihKontaktov.removeAllElements();
		String[] imena = dobiPolnaImena();
		int prikaz = 0;
		for (int i = 0; i < imena.length; i++) {
			String ime = imena[i];
			if (ime.toLowerCase().contains(poizvedba)) {
				String[] kontakt = seznamVsehKontaktov.elementAt(i);
				seznamTrenutnihKontaktov.add(kontakt);
				novModel.addElement(ime);
				if (Integer.parseInt(kontakt[kontakt.length-1]) == id) {
					prikaz = novModel.getSize() - 1;
				}
			}
        }
		kontakti.removeListSelectionListener(this);
		kontakti.setModel(novModel);
		kontakti.addListSelectionListener(this);
        kontakti.setSelectedIndex(prikaz);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		posodobiSeznam(false, 0);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		posodobiSeznam(false, 0);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		posodobiSeznam(false, 0);
	}
}
