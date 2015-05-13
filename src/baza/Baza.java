package baza;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Baza {
	private Connection c;
	private Statement izjava;
	private int steviloStolpcev = 6; // stevilo stolpcev v bazi
	
	public void poveziSeZBazo() {
		c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:imenik.db");
			izjava = c.createStatement();
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
			izjava.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void izbrisiTabelo() {
		try {
			String query = "DROP TABLE KONTAKTI;";
			izjava.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String[][] izberiTabelo() {
		String[][] kontakti = null;
		try {
			String query0 = "SELECT COUNT (*) AS length FROM KONTAKTI;";
			ResultSet rez0 = izjava.executeQuery(query0);
			int len = rez0.getInt("length");
			
			String query = "SELECT * FROM KONTAKTI;";
			ResultSet rez = izjava.executeQuery(query);
			
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
	
	public boolean dodajKontakt(String dIme, String dPriimek, String dStevilka, 
			String dNaslov, String dKraj, String dPosta) {
		if (dIme.length() == 0 || dPriimek.length() == 0 || dStevilka.length() == 0 ||
				dPosta.length() != 4) {
			return false;
		}
		try {
			String query = String.format("INSERT INTO KONTAKTI (IME, PRIIMEK, STEVILKA, NASLOV, KRAJ, POSTA) "
					+ "VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", 
					dIme, dPriimek, dStevilka, dNaslov, dKraj, dPosta);
			izjava.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean izbrisiKontakt(String[] dPodatki) {
		try {
			String query = String.format("DELETE FROM KONTAKTI WHERE IME = \"%s\" AND "
					+ "PRIIMEK = \"%s\" AND STEVILKA = \"%s\" AND NASLOV = \"%s\" AND "
					+ "KRAJ = \"%s\" AND POSTA = \"%s\";",
					(Object[]) dPodatki);
			izjava.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean posodobiKontakt(String[] stariPodatki, String[] noviPodatki) {
		try {
			String[] dPodatki = new String[2 * steviloStolpcev];
			System.arraycopy(noviPodatki, 0, dPodatki, 0, steviloStolpcev);
			System.arraycopy(stariPodatki, 0, dPodatki, steviloStolpcev, steviloStolpcev);
			String query = String.format("UPDATE KONTAKTI SET IME = \"%s\", PRIIMEK = \"%s\", "
					+ "STEVILKA = \"%s\", NASLOV = \"%s\", KRAJ = \"%s\", POSTA = \"%s\" "
					+ "WHERE IME = \"%s\" AND PRIIMEK = \"%s\" AND STEVILKA = \"%s\" "
					+ "AND NASLOV = \"%s\" AND KRAJ = \"%s\" AND POSTA = \"%s\";", 
					(Object[]) dPodatki);
			izjava.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}