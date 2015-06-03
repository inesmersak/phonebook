package grafika;

import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import baza.Baza;

@SuppressWarnings("serial")
public class VsebinskoPodrocje extends JPanel implements ActionListener, ListSelectionListener {
	private Dimension velikost = new Dimension(400, 300);
	private JList<String> kontakti;
	private DefaultListModel<String> model;
	private JEditorPane prikazKontakta;
	private Baza baza;
	private Vector<String[]> seznamVsehKontaktov;
	private Vector<String[]> seznamTrenutnihKontaktov;
	private NovKontakt oknoKontakta;
	private JTextField iskalnik;
	private Color barvaToolbara;
	private Color barvaGumbov;
	private Color barvaSeznamov;
	
	static private final String DODAJ = "dodaj";
	static private final String UREDI = "uredi";
	static private final String IZBRISI = "izbrisi";
	static private final String VNESEN_TEKST = "vnesen tekst";
	
	public VsebinskoPodrocje(Baza danaBaza) {
		super(new GridBagLayout());
		
		baza = danaBaza;
		seznamVsehKontaktov = baza.izberiTabelo();
		seznamTrenutnihKontaktov = baza.izberiTabelo();
		
		setPreferredSize(velikost);
		
		barvaToolbara = new Color(180, 175, 204);
		barvaGumbov = new Color(215, 213, 224);
		barvaSeznamov = new Color(215, 213, 224);
		
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setLayout(new GridBagLayout());
        toolbar.setBackground(barvaToolbara);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
        add(toolbar, c);
        
        JLabel isci = new JLabel("Išči!");
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.weightx = 1;
        c1.weighty = 1;
        c1.anchor = GridBagConstraints.CENTER;
        toolbar.add(isci, c1);
        
        iskalnik = new JTextField();
        iskalnik.setActionCommand(VNESEN_TEKST);
        iskalnik.addActionListener(this);
        c1.gridy = 1;
        toolbar.add(iskalnik, c1);
        
        dodajGumbe(toolbar);
        
        String[] imena = dobiPolnaImena();
        model = new DefaultListModel<String>();
        for (int i = 0; i < imena.length; i++) {
        	model.addElement(imena[i]);
        }
        
        kontakti = new JList<String>(model);
        kontakti.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        kontakti.setSelectedIndex(0);
        kontakti.addListSelectionListener(this);
        kontakti.setBackground(barvaSeznamov);
        
        prikazKontakta = new JEditorPane();
        posodobiPrikaz(0);
        kontakti.setBackground(barvaSeznamov);
        
        JScrollPane kontaktiScrollPane = new JScrollPane(kontakti);
        JScrollPane prikazScrollPane = new JScrollPane(prikazKontakta);
        
        JSplitPane glavnoPodrocje = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kontaktiScrollPane, 
        		prikazScrollPane);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(glavnoPodrocje, c);
        
//        String[] staro = {"Ines", "Meršak", "040126776", "Plešičeva ulica 25", "Ljubljana", "1000"};
//        String[] novo = {"Ines", "Meršak", "040126776", "Plešičeva ulica 23", "Ljubljana", "1000"};
//        boolean dodaj = baza.dodajKontakt("Sandi", "Slak", "041751971", "Ulica bratov Potočnikov 13", 
//        		"Brezovica pri Ljubljani", "");
//        System.out.println(dodaj);
//        boolean update = baza.posodobiKontakt(novo, staro);
//        System.out.println(update);
	}

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
			System.out.println("1" + cmd);
			oknoKontakta = new NovKontakt("Dodaj nov kontakt", baza, -1);
			oknoKontakta.shrani.addActionListener(this);
			oknoKontakta.pack();
			oknoKontakta.setVisible(true);
		} else if (cmd.equals(UREDI)) {
			System.out.println("2" + cmd);
			String izbranKontakt = kontakti.getSelectedValue();
			int idIzbranegaKontakta = Integer.parseInt(
					seznamTrenutnihKontaktov.elementAt(kontakti.getSelectedIndex()) [6]);
			oknoKontakta = new NovKontakt(izbranKontakt, baza, idIzbranegaKontakta);
			oknoKontakta.shrani.addActionListener(this);
			oknoKontakta.pack();
			oknoKontakta.setVisible(true);
		} else if (cmd.equals(IZBRISI)) {
			System.out.println("3" + cmd);
			int izbranIndeks = kontakti.getSelectedIndex();
			String izbranKontakt = kontakti.getSelectedValue();
			int potrditevBrisanja = JOptionPane.showOptionDialog(this.getParent(), 
					"Ali ste prepričani, da želite izbrisati kontakt " + izbranKontakt + "?", 
					"Izbriši " + izbranKontakt,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null, 
			        new String[]{"Da", "Ne"},
			        "default");
			if (potrditevBrisanja == JOptionPane.YES_OPTION) {
				baza.izbrisiKontakt(Integer.parseInt(seznamTrenutnihKontaktov.elementAt(izbranIndeks) [6]));
				posodobiSeznam(true);
			}
		} else if (cmd.equals(NovKontakt.SHRANI)) {
			String[] noviPodatki = new String[oknoKontakta.textfields.length];
			for (int i = 0; i < oknoKontakta.textfields.length; i++) {
				noviPodatki[i] = oknoKontakta.textfields[i].getText();
			}
			if (noviPodatki[0].length() == 0 || noviPodatki[1].length() == 0 || noviPodatki[2].length() == 0) {
				JOptionPane.showMessageDialog(oknoKontakta.getParent(), 
						"Polja 'Ime', 'Priimek' in 'Telefonska številka' so obvezna!", 
						"Manjkajoči podatki", JOptionPane.ERROR_MESSAGE);
			} else {
				if (oknoKontakta.id < 0) {
					baza.dodajKontakt(noviPodatki);
				} else {
					baza.posodobiKontakt(oknoKontakta.id, noviPodatki);
				}
				posodobiSeznam(true);
				oknoKontakta.dispose();
			}
		} else if (cmd.equals(VNESEN_TEKST)) {
			posodobiSeznam(false);
		} else {
			System.out.println("command not found");
		}
	}
	
	private JButton narediGumb(String imeSlike, String actionCommand, String altTekst) {
		JButton gumb = new JButton();
		gumb.setActionCommand(actionCommand);
		gumb.setToolTipText(altTekst);
		gumb.addActionListener(this);
		gumb.setBackground(barvaGumbov);
		
		String potSlike = "/" + imeSlike + ".png";
		URL urlSlike = VsebinskoPodrocje.class.getResource(potSlike);
		
		if (urlSlike != null) {
			gumb.setIcon(new ImageIcon(urlSlike, altTekst));
		} else {
			gumb.setText(altTekst);
		}
		
		return gumb;
	}
	
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
	 * @param indeks Indeks elementa, ki je bil kliknjen.
	 * Ob kliku na drug kontakt zamenja prikaz na desni strani.
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
	
	// TODO po urejanju ali dodajanju kontakta prikazi ta kontakt
	
	private void posodobiSeznam(boolean spremembaBaze) {
		if (spremembaBaze) { 
			seznamVsehKontaktov = baza.izberiTabelo();
			iskalnik.setText("");
			}
		String poizvedba = iskalnik.getText().toLowerCase();
		DefaultListModel<String> novModel = new DefaultListModel<String>();
		seznamTrenutnihKontaktov.removeAllElements();
		String[] imena = dobiPolnaImena();
		for (int i = 0; i < imena.length; i++) {
			String ime = imena[i];
			if (ime.toLowerCase().contains(poizvedba)) {
				seznamTrenutnihKontaktov.add(seznamVsehKontaktov.elementAt(i));
				novModel.addElement(ime);
			}
        }
		kontakti.removeListSelectionListener(this);
		kontakti.setModel(novModel);
		kontakti.addListSelectionListener(this);
        kontakti.setSelectedIndex(0);
	}
}
