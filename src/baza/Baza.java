package baza;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class Baza {
	/**
	 * povezava z bazo
	 */
	private Connection c;
	
	/**
	 * sem bomo shranili nas query
	 */
	private PreparedStatement izjava = null;
	
	/**
	 * Se poveze z bazo 'imenik.db'. Vkolikor ta baza na disku ne obstaja, naredi novo.
	 * Povezavo shrani v field 'c'. 
	 */
	public void poveziSeZBazo() {
		c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:imenik.db");
		} catch (Exception e) {
			System.out.println("Povezava z bazo ni uspela.");
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}	
	}
	
	/**
	 * Poskrbi za pravilno zaprtje povezave z bazo.
	 */
	public void zapriPovezavo() {
		try {
			izjava.close();
			c.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Ustvari tabelo 'KONTAKTI' v bazi.
	 */
	public void dodajTabelo() {
		try {
			String query = "CREATE TABLE KONTAKTI " +
	                "(ID INTEGER PRIMARY KEY, " +
	                " IME TEXT NOT NULL, " + 
	                " PRIIMEK TEXT NOT NULL, " +
	                " STEVILKA VARCHAR(25) NOT NULL, " +
	                " NASLOV TEXT, " + 
	                " KRAJ TEXT, " +
	                " POSTA CHAR(4))";	
			izjava = c.prepareStatement(query);
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Izbrise tabelo 'KONTAKTI' iz baze.
	 */
	public void izbrisiTabelo() {
		try {
			String query = "DROP TABLE KONTAKTI";
			izjava = c.prepareStatement(query);
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
		
	/**
	 * Izbere celotno tabelo v bazi; podatke o vsakem kontaktu spravi v array,
	 * array pa v Vector, kjer so zbrani vsi kontakti. 
	 * @return Vector vseh kontaktov
	 */
	public Vector<String[]> izberiTabelo() {
		Vector<String[]> kontakti = null;
		try {
			// kontakte razvrstimo po abecednem vrstnem redu priimka, nato se imena
			String query = "SELECT * FROM KONTAKTI ORDER BY PRIIMEK ASC, IME ASC";
			izjava = c.prepareStatement(query);
			ResultSet rez = izjava.executeQuery();
			
			kontakti = new Vector<String[]>(); 
			
			while (rez.next()) {
		         String id = Integer.toString(rez.getInt("id"));
		         String ime = rez.getString("ime");
		         String priimek = rez.getString("priimek");
		         String stevilka = rez.getString("stevilka");
		         String naslov = rez.getString("naslov");
		         String kraj = rez.getString("kraj");
		         String posta = rez.getString("posta");
		         System.out.println(id + ime + priimek + stevilka + naslov + kraj + posta);
		         String[] kontakt = {ime, priimek, stevilka, naslov, kraj, posta, id};
		         kontakti.add(kontakt);
		      }
			
			rez.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return kontakti;
	}
	
	/**
	 * Sprejme array podatkov o novem kontaktu in ga, vkolikor zadosca vsem pogojem, 
	 * doda v tabelo 'KONTAKTI'.
	 * @param dPodatki 
	 * @return id dodanega kontakta; -1 za neuspel poskus
	 */
	public int dodajKontakt(String[] dPodatki) {
		int id = -1;
		// TODO field, kjer je shranjeno stevilo stolpcev
		if (dPodatki.length != 6) {
			return id;
		}
		// TODO preveri, ce lahko tole izbrises
		else if (dPodatki[0].length() == 0 || dPodatki[1].length() == 0 || dPodatki[2].length() == 0 
				|| (dPodatki[5].length() != 4 && dPodatki[5].length() != 0)) {
			return id;
		}
		try {
			String query = "INSERT INTO KONTAKTI (IME, PRIIMEK, STEVILKA, NASLOV, KRAJ, POSTA) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			izjava = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < dPodatki.length; i++) {
				izjava.setString(i+1, dPodatki[i]);
			}
			izjava.executeUpdate();
			
			// poizvedba za id novega kontakta
			ResultSet kljuc = izjava.getGeneratedKeys();
			if (kljuc.next()) {
				id = kljuc.getInt(1);
			}
			kljuc.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return id;
		}
		return id;
	}
	
	/**
	 * Izbrise kontakt z danim id-jem.
	 * @param id
	 * @return uspesnost brisanja kontakta
	 */
	public boolean izbrisiKontakt(int id) {
		try {
			String query = "DELETE FROM KONTAKTI WHERE ID = ?";
			izjava = c.prepareStatement(query);
			izjava.setInt(1, id);
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Podatke kontakta z danim id-jem zamenja z novimi.
	 * @param id
	 * @param noviPodatki
	 * @return uspesnost posodabljanja podatkov
	 */
	public boolean posodobiKontakt(int id, String[] noviPodatki) {
		try {
			String query = "UPDATE KONTAKTI SET IME = ?, PRIIMEK = ?, "
					+ "STEVILKA = ?, NASLOV = ?, KRAJ = ?, POSTA = ? "
					+ "WHERE ID = ?";
			izjava = c.prepareStatement(query);
			for (int i = 0; i < noviPodatki.length; i++) {
				izjava.setString(i+1, noviPodatki[i]);
			}
			izjava.setInt(noviPodatki.length + 1, id);
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Naredi poizvedbo v tabeli 'KONTAKTI' za kontakt z danim id-jem.
	 * @param id
	 * @return kontakt
	 */
	public String[] pridobiKontakt(int id) {
		try {
			String query = "SELECT * FROM KONTAKTI WHERE ID = ?";
			izjava = c.prepareStatement(query);
			izjava.setInt(1, id);
			ResultSet rez = izjava.executeQuery();
			
			String ime = rez.getString("ime");
	        String priimek = rez.getString("priimek");
	        String stevilka = rez.getString("stevilka");
	        String naslov = rez.getString("naslov");
	        String kraj = rez.getString("kraj");
	        String posta = rez.getString("posta");
	        String[] kontakt = {ime, priimek, stevilka, naslov, kraj, posta};
	        
	        rez.close();
	        return kontakt;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}		
	}
}
