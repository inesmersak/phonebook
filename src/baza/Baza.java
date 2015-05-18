package baza;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class Baza {
	private Connection c;
	private PreparedStatement izjava = null;
	
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
	
	public void zapriPovezavo() {
		try {
			izjava.close();
			c.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
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
	
	public void izbrisiTabelo() {
		try {
			String query = "DROP TABLE KONTAKTI";
			izjava = c.prepareStatement(query);
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Vector<String[]> izberiTabelo() {
		Vector<String[]> kontakti = null;
		try {
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return kontakti;
	}
	
	public boolean dodajKontakt(String[] dPodatki) {
		if (dPodatki.length != 6) {
			return false;
		} else if (dPodatki[0].length() == 0 || dPodatki[1].length() == 0 || dPodatki[2].length() == 0 
				|| (dPodatki[5].length() != 4 && dPodatki[5].length() != 0)) {
			return false;
		}
		try {
			String query = "INSERT INTO KONTAKTI (IME, PRIIMEK, STEVILKA, NASLOV, KRAJ, POSTA) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			izjava = c.prepareStatement(query);
			for (int i = 0; i < dPodatki.length; i++) {
				izjava.setString(i+1, dPodatki[i]);
			}
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
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
