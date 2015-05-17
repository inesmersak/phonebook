package baza;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Baza {
	private Connection c;
	private PreparedStatement izjava = null;
	private int steviloStolpcev = 6; // stevilo stolpcev v bazi
	
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
	
	public String[][] izberiTabelo() {
		String[][] kontakti = null;
		try {
			String query0 = "SELECT COUNT (*) AS length FROM KONTAKTI";
			izjava = c.prepareStatement(query0);
			ResultSet rez0 = izjava.executeQuery();
			int len = rez0.getInt("length");
			
			String query = "SELECT * FROM KONTAKTI";
			izjava = c.prepareStatement(query);
			ResultSet rez = izjava.executeQuery();
			
			kontakti = new String[len][steviloStolpcev]; 
			
			int i = 0;
			while (rez.next()) {
		         int id = rez.getInt("id");
		         String ime = rez.getString("ime");
		         String priimek = rez.getString("priimek");
		         String stevilka = rez.getString("stevilka");
		         String naslov = rez.getString("naslov");
		         String kraj = rez.getString("kraj");
		         String posta = rez.getString("posta");
		         System.out.println(id + ime + priimek + stevilka + naslov + kraj + posta);
		         String[] kontakt = {ime, priimek, stevilka, naslov, kraj, posta};
		         kontakti[i] = kontakt;
		         i++;
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
	
	public boolean izbrisiKontakt(String[] dPodatki) {
		try {
			String query = "DELETE FROM KONTAKTI WHERE IME = ? AND "
					+ "PRIIMEK = ? AND STEVILKA = ? AND NASLOV = ? AND "
					+ "KRAJ = ? AND POSTA = ?";
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
	
	public boolean posodobiKontakt(String[] stariPodatki, String[] noviPodatki) {
		try {
//			String[] dPodatki = new String[2 * steviloStolpcev];
//			System.arraycopy(noviPodatki, 0, dPodatki, 0, steviloStolpcev);
//			System.arraycopy(stariPodatki, 0, dPodatki, steviloStolpcev, steviloStolpcev);
			String query = "UPDATE KONTAKTI SET IME = ?, PRIIMEK = ?, "
					+ "STEVILKA = ?, NASLOV = ?, KRAJ = ?, POSTA = ? "
					+ "WHERE IME = ? AND PRIIMEK = ? AND STEVILKA = ? "
					+ "AND NASLOV = ? AND KRAJ = ? AND POSTA = ?";
			izjava = c.prepareStatement(query);
			for (int i = 0; i < noviPodatki.length; i++) {
				izjava.setString(i+1, noviPodatki[i]);
			}
			for (int i = steviloStolpcev; i < steviloStolpcev + stariPodatki.length; i++) {
				izjava.setString(i+1, stariPodatki[i % steviloStolpcev]);
			}
			izjava.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}
