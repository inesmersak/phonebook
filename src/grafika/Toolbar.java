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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class Toolbar extends JPanel implements ActionListener {
	static private final String DODAJ = "dodaj";
	static private final String UREDI = "uredi";
	static private final String IZBRISI = "izbrisi";
	static private final String VNESEN_TEKST = "vnesen tekst";
	
	// TODO rename the inappropriate name
	public Toolbar() {
		super(new GridBagLayout());
		
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
        String[] imena = {"Anja", "Barbara", "Polona"};
        JList<String> kontakti = new JList<String>(imena);
        JEditorPane prikazKontakta = new JEditorPane();
        JSplitPane glavnoPodrocje = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kontakti, prikazKontakta);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(glavnoPodrocje, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private JButton narediGumb(String imeSlike, String actionCommand, String altTekst) {
		JButton gumb = new JButton();
		gumb.setActionCommand(actionCommand);
		gumb.setToolTipText(altTekst);
		gumb.addActionListener(this);
		
		String potSlike = "/" + imeSlike + ".png";
		System.out.println(potSlike);
		URL urlSlike = Toolbar.class.getResource(potSlike);
		
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
	
}
