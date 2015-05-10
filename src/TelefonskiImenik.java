import grafika.GlavnoOkno;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;


public class TelefonskiImenik {
	public static Connection poveziSeZBazo() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:imenik.db");
		} catch (Exception e) {
			System.out.println("Povezava z bazo ni uspela.");
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}	
		return c;
	}
	
	public static void main(String[] args) {
		Connection c = poveziSeZBazo();
		if (c == null) {
			return ;
		}
		JFrame glavnoOkno = new GlavnoOkno();
		glavnoOkno.pack();
		glavnoOkno.setVisible(true);
	}
}
