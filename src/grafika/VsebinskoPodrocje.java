package grafika;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private JEditorPane prikazKontakta;
	private Baza baza;
	private String[][] seznamKontaktov;
	
	static private final String DODAJ = "dodaj";
	static private final String UREDI = "uredi";
	static private final String IZBRISI = "izbrisi";
	static private final String VNESEN_TEKST = "vnesen tekst";
	
	public VsebinskoPodrocje() {
		super(new GridBagLayout());
		
		baza = new Baza();
		baza.poveziSeZBazo();
		seznamKontaktov = baza.izberiTabelo();
		
		setPreferredSize(new Dimension(300, 300));
		
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setLayout(new GridBagLayout());
        
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
        
        JTextField iskalnik = new JTextField();
        iskalnik.setActionCommand(VNESEN_TEKST);
        iskalnik.addActionListener(this);
        c1.gridy = 1;
        toolbar.add(iskalnik, c1);
        
        dodajGumbe(toolbar);
        
        // TODO implement ListListener, extending the JSplitPane class
        String[] imena = dobiPolnaImena();
        JList<String> kontakti = new JList<String>(imena);
        kontakti.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        kontakti.setSelectedIndex(0);
        kontakti.addListSelectionListener(this);
        
        prikazKontakta = new JEditorPane();
        prikazKontakta.setText(kontakti.getSelectedValue());
        
        JScrollPane kontaktiScrollPane = new JScrollPane(kontakti);
        JScrollPane prikazScrollPane = new JScrollPane(prikazKontakta);
        
        JSplitPane glavnoPodrocje = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kontaktiScrollPane, 
        		prikazScrollPane);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(glavnoPodrocje, c);
        
        String[] staro = {"Ines", "Meršak", "040126776", "Plešičeva ulica 23", "Ljubljana", "1000"};
        String[] novo = {"Ines", "Meršak", "040126776", "Plešičeva ulica 25", "Ljubljana", "1000"};
        // baza.dodajKontakt("Marijan", "Meršak", "041659073", "Plešičeva ulica 25", "Ljubljana", "1000");
        baza.zapriPovezavo();
	}

	private String[] dobiPolnaImena() {
		String[] polnaImena = new String[seznamKontaktov.length];
		int i = 0;
		for(String[] vrs : seznamKontaktov) {
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
		} else if (cmd.equals(UREDI)) {
			System.out.println("2" + cmd);
		} else if (cmd.equals(IZBRISI)) {
			System.out.println("3" + cmd);
		} else {
			System.out.println("command not found");
		}
	}
	
	private JButton narediGumb(String imeSlike, String actionCommand, String altTekst) {
		JButton gumb = new JButton();
		gumb.setActionCommand(actionCommand);
		gumb.setToolTipText(altTekst);
		gumb.addActionListener(this);
		
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
		prikazKontakta.setText(kliknjenSeznam.getSelectedValue());
		System.out.println(kliknjenSeznam.getSelectedIndex());
	}
	
}
