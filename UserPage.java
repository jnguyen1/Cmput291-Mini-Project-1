import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;


public class UserPage {

	/**
	 * @param args
	 */

	static String user;
	static String createString;

	public UserPage(){
		createString = "select name from users where email = '"+createString+"';";
		try
		{

			Main.m_con = DriverManager.getConnection(Main.m_url, Main.m_userName, Main.m_password);

			Main.stmt = Main.m_con.createStatement();
			ResultSet rs = Main.stmt.executeQuery(createString);

			user = rs.getString("name");

			Main.stmt.close();
			Main.m_con.close();

		} catch(SQLException ex) {

			System.err.println("SQLException: " +
					ex.getMessage());

		}
	}

	static boolean loggedOn = true;
	public void startUp(){
		System.out.println("Welcome "+user+". Here are your options for today.");

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
			int menuChoice = Keyboard.in.readInteger();

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
			int choice = Keyboard.in.readInteger();
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
				try
				{
					registerFan(stmt, results.get(choice).getPid());
				}
				catch (SQLException e)
				{
					System.out.println("Could not register as fan of page.");
				}

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
	private void registerFan(Statement stmt, String pid) throws SQLException
	{
		stmt.executeUpdate("insert into fans values('" + Main.user + "', '" + pid + "', current_date)");
	}

	/**
	 * Function:
	 * Implements query 8. User interface for sending messages to other users.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100308
	 */
	private void sendMessage(Statement stmt)
	{
		Vector<String> recipientList = new Vector<String>();
		String user;
		String message;

		System.out.println("Enter a list of users. Newline to finish list.");
		while (true)
		{
			user = Keyboard.in.readString();
			if (user.length() == 0)
			{
				break;
			}
			else
			{
				recipientList.add(user);
			}
		}

		// No users to send to, no message needed.
		if (recipientList.size() == 0)
		{
			return;
		}

		System.out.println("Enter your message, followed by newline.");
		message = Keyboard.in.readString();

		this.sendMessageSql(stmt, recipientList, message);
	}

	/**
	 * Function:
	 * Sends a message to all users in list. Creates a message in messages table and sends it to all users in list.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * recipientList - list of users to send this message to.
	 * content - the string message to send.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100308
	 */
	private void sendMessageSql(Statement stmt, Vector<String> recipientList, String content)
	{
		try
		{
			// If max(mid) is null, 0 is returned which works nicely.
			int messageId = stmt.executeQuery("select max(mid) from messages").getInt(1);

			// Insert into messages table.
			stmt.executeUpdate("insert into messages values('" + Integer.toString(messageId) + "', current_date, '" + content + "', '" + Main.user + "')");

			Iterator itr = recipientList.iterator();
			while (itr.hasNext())
			{
				this.sendMessageToUserSql(stmt, messageId, (String)itr.next());
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Function:
	 * Associate a user to be the recipient of a message. Insert into receives table.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * messageId - the id of the message found in messages table.
	 * recipient - string name of the user to send the message to.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100308
	 */
	private void sendMessageToUserSql(Statement stmt, int messageId, String recipient)
	{
		try
		{
			stmt.executeUpdate("insert into receives values('" + Integer.toString(messageId) + "', '" + recipient + "')");
		}
		catch (SQLException e)
		{
			// Foreign key constraint fail.
			System.out.println("Could not send message to " + recipient + ", maybe they are not a valid user.");
		}
	}
}
