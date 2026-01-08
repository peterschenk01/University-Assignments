// JDBC: Online-Supermarkt

// Downloaden: in eine Datei mit Namen Supermarkt.java!
// Anpassen: Name der Datenbank und Benutzer!
// Uebersetzen: javac Supermarkt.java
// vor der Benutzung: Datenbank anlegen
// Usage: java Supermarkt
// danach: alleWaren oder Bestellungen oder Bestellen oder # (fuer Beenden)
// momentan notwendiger Treiber: postgresql-9.1-903-jdbc4.jar

import java.io.*;
import java.sql.*;

public class Supermarkt {
   

   public Supermarkt() throws ClassNotFoundException, FileNotFoundException,
                                   IOException, SQLException {
   }

   public void alleWaren () throws Exception{
	  //.....................
   }
	   
   public void alleBestellungen() throws Exception {
	   //.....................
   }
	   
   public void bestellen() throws Exception {
	  //......................
   }
   
   public static void main (String args[]) throws IOException {
	if (args.length != 0) {
		System.out.println ("Usage: java supermarkt");
		System. exit(1);
	}
	String driverClass = "org.postgresql.Driver";
	try {
		Class.forName (driverClass);
	} catch (ClassNotFoundException exc) {
		System.out.println ("ClassNotFoundException: " + exc.getMessage() );
	}
      try {
	// 193.175.36.101 ist die vm3-1-Maschine 
	Class.forName ("org.postgresql.Driver");
	// Folgendes Statement: Name der Datenbank anpassen!
	String db_url = "jdbc:postgresql://193.175.36.101/wjsupermarkt";
	// Folgendes Statement: Name des Benutzers anpassen!
	con = DriverManager.getConnection (db_url, "wojue", "");
	Supermarkt sup = new Supermarkt();
	String aufgabe = null;
	BufferedReader userin
	      = new BufferedReader (new InputStreamReader (System.in));
	System.out.println
	      ("\nGeben Sie ein: Waren oder Bestellungen oder Bestellen oder #!\n");
	while ( ! (aufgabe =userin.readLine()).startsWith("#")) {
		if (aufgabe.equals ("Waren")) sup.alleWaren();
		else if (aufgabe.equals("Bestellungen")) sup.alleBestellungen();
		else if (aufgabe.equals("Bestellen")) sup.bestellen();
		System.out.println
			("\nGeben Sie ein: Waren oder Bestellungen oder Bestellen oder #!\n");
	}
	con.close();
      } catch (Exception exc)
      {
         System.err.println ("Exception in Main. " + exc.getMessage());
	 exc.printStackTrace();
      }
   }
   
   static private Connection con;
}


