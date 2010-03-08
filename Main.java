import java.sql.*;
import java.io.*;
import java.util.Vector;
import java.util.Collections;

public class Main
{
	private static Keyboard keyboard = new Keyboard();
	public void main(String[] args)
	{
		try
		{ 
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
		} 
		catch (ClassNotFoundException e)
		{ 
			System.out.println("Could not load the driver"); 
		} 

		String user, pass; 
		System.out.print("userid :");
		user = keyboard.readString();
		System.out.print("password :");
		pass = keyboard.readString();

		Connection conn;
		try
		{
			conn = DriverManager.getConnection("jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS",user,pass); 

		/* All the functionality.
		 */
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println("SQL error in connecting to database.");
		}
	}

	/*
	 * Function:
	 * Searches through pages table for records whose title or content contains the supplied keywords.
	 * Display the results in a sorted order that places greater weighting on matches on title (T*2+C).
	 *
	 * Param:
	 * conn - the connection to the database.
	 * keywords - a vector of 1 or more keywords to match on.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100307
	 */
	private void searchPages(Connection conn, Vector<String> keywords) throws SQLException
	{
		Vector<SearchPageObject> results = new Vector<SearchPageObject>();

		String condition = "title like '" + keywords.get(0) + "' or content like '" + keywords.get(0) + "'";
		for (int i=1; i<keywords.size(); i++)
		{
			condition.concat(" or title like '" + keywords.get(i) + "' or content like '" + keywords.get(i) + "'");
		}

		Statement stmt = conn.createStatement(); 
		ResultSet rset = stmt.executeQuery("select * from pages where " + condition + ";"); 

		while(rset.next())
		{ 
			SearchPageObject spo = new SearchPageObject(
				rset.getString("pid"),
				rset.getDate("cdate"),
				rset.getString("title"),
				rset.getString("content"),
				rset.getString("creator")
				);
			spo.calculateRanking(keywords);

			results.add(spo);
		} 

		Collections.sort(results);
		/*
		 * Do somethign with the sorted result. Probably print it to user in sorted order.
		 */

		stmt.close(); 
	}
}
