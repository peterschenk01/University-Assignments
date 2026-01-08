import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Supermarkt {
   

   public Supermarkt() throws ClassNotFoundException, FileNotFoundException,
                                   IOException, SQLException {
   }

   public void alleWaren () throws SQLException{
	   try (Statement statement = con.createStatement();){
		   String query = "SELECT bezeichnung, preis, vorrat, kname FROM ware, kategorie WHERE ware.katid = kategorie.katid;";
		   ResultSet rs = statement.executeQuery(query);
		   
		   while(rs.next()) {
			   String bezeichnung = rs.getString("bezeichnung");
			   double preis = rs.getDouble("preis");
			   int vorrat = rs.getInt("vorrat");
			   String kname = rs.getString("kname");
			   
			   System.out.println(bezeichnung + ", " + preis + ", " + vorrat + ", " + kname);
		   }
	   } catch(SQLException e) {
		   System.err.println(e.getMessage());
	   }
   }
	   
   public void alleBestellungen() throws SQLException, IOException {
	   String query = "SELECT bestid, datum, status, kname FROM bestellung, kunde WHERE kundeid = kundid AND kname = ?;";
	   try (PreparedStatement statement= con.prepareStatement(query);){
		   BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		   boolean b = false;
		   List<String> kunden = new ArrayList<String>();
		   while(!b) {
			   System.out.println("Geben Sie einen Kunden ein");
			   kunden.add(in.readLine());
			   System.out.println("Möchten Sie einen weiteren Kunden eingeben? J/N?");
			   if(in.readLine().equals("N"))
				   b = true;
		   }
		   System.out.println("Alle Bestellungen:\n");
		   for(String name : kunden) {
			   statement.setString(1, name);
			   ResultSet rs = statement.executeQuery();
			   
			   while(rs.next()) {
				   System.out.println(rs.getInt("bestid") + ", " + rs.getDate("datum") + ", " + rs.getInt("status") + ", " + rs.getString("kname"));
			   }
		   }
	   } catch(SQLException e) {
		   System.err.println(e.getMessage());
	   } catch(IOException e2) {
		   System.err.println(e2.getMessage());
	   }
   }
	   
   public void bestellen() throws SQLException, IOException {
   try (Statement statement = con.createStatement();){
	   BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
	   List<String> waren = new ArrayList<String>();
	   List<Integer> anzahl = new ArrayList<Integer>();
	   System.out.println("Kundenname:\n");
	   String kundenname = in.readLine();
	   boolean b = false;
	   while(!b){
		   System.out.println("Ware:\n");
		   waren.add(in.readLine());
		   System.out.println("Anzahl:\n");
		   anzahl.add(Integer.parseInt(in.readLine()));
		   System.out.println("Möchten Sie noch mehr bestellen? J/N?");
		   if(in.readLine().equals("N"))
			   b = true;
	   }
	   ResultSet rs = statement.executeQuery("SELECT kundeid FROM kunde WHERE kname = '" + kundenname + "';");
	   rs.next();
	   int kundenid = rs.getInt("kundeid");
	   for(int i = 0; i < anzahl.size(); i++) {
		   String query = "INSERT INTO bestellung VALUES (nextval('bestellid'), CURRENT_DATE, 0, " + kundenid + ");";
		   statement.executeUpdate(query);
		   
		   ResultSet rs2 = statement.executeQuery("SELECT warenid FROM ware WHERE bezeichnung = '" + waren.get(i) + "';");
		   rs2.next();
		   int warenid = rs2.getInt("warenid");
		   
		   String query2 = "INSERT INTO enthaelt VALUES (currval('bestellid'), " + warenid + ", " + anzahl.get(i) + ");";
		   statement.executeUpdate(query2);
		   
	   }
   } catch(SQLException e) {
	   System.err.println(e.getMessage());
   } catch(IOException e2) {
	   System.err.println(e2.getMessage());
   }
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
	String db_url = "jdbc:postgresql://localhost:5432/DB-4";
	// Folgendes Statement: Name des Benutzers anpassen!
	con = DriverManager.getConnection (db_url, "postgres", "2304");
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