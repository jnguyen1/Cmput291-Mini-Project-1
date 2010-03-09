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

		for (int i=0; i<results.size(); i++)
		{
			SearchPageObject spo = results.get(i);
			System.out.format("#%d %20s %20s %20s\n", i, spo.getTitle(), spo.getContent(), spo.getCreator());
		}

		System.out.println("1. Become a fan of a page.");
		System.out.println("2. Return.");
		boolean menuLoop = true;
		while (menuLoop)
		{
			int menuChoice = Keyboard.readInt();

			switch (menuChoice)
			{
				case 0:
					fanPageRequest(stmt, results);
					menuLoop = false;
					break;
				case 1:
					menuLoop = false;
					break;
				default:
					break;
			}
		}

		stmt.close(); 
	}

	/**
	 * Function:
	 * Request the fan page information from user and register them as a fan.
	 *
	 * Param:
	 * stmt - the Statement object to execute statements on.
	 * results - list of SearchPageObject which are sorted in ranking. Users will choose a from the sorted list.
	 *
	 * Return:
	 * None.
	 */
	private void fanPageRequest(Statement stmt, Vector<SearchPageObject> results)
	{
		while (true)
		{
			System.out.println("Select a page number to become a fan. -1 to cancel.");
			int choice = Keyboard.getInt();
			if (choice == -1)
			{
				return;
			}
			else if (choice < 0 || choice > results.size())
			{
				System.out.println("Wrong choice. Pick again");
			}
			else
			{
				registerFan(stmt, results.get(choice).getPid());
				return;
			}
		}
	}

	/**
	 * Function:
	 * Register the user as a fan of the page.
	 *
	 * Param:
	 * stmt - Statement object used to execute sql statements.
	 * pid - the id of the page.
	 *
	 * Return:
	 * None.
	 */
	private void registerFan(Statement stmt, String pid)
	{
		stmt.executeUpdate("insert into fans values('" + email + "', '" + pid + "', current_date)");
	}
}
