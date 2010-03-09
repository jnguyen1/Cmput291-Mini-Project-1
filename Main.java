import java.sql.*;
import java.util.Vector;
import java.util.Collections;

public class Main {

	/**
	 * @param args
	 */
	
	static String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	static String m_driverName = "oracle.jdbc.driver.OracleDriver";

	static String m_userName = "user";
	static String m_password = "pass";

	static Connection m_con;
	
	static Statement stmt;
	
	static boolean menu;
	
	static String createString;
	static String user;
	
	//static boolean DEBUG = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{

		Class drvClass = Class.forName(m_driverName);
		DriverManager.registerDriver((Driver)
		drvClass.newInstance());

		} catch(Exception e)
		{

		System.err.print("ClassNotFoundException: ");
		System.err.println(e.getMessage());

		}

		System.out.println("Welcome! Please log in with your e-mail and password or choose the next option " +
				"to register if you don't have an account yet. (Type the corresponding number for the option you want)");
		
		System.out.println("1. Login (for registered users)");
		System.out.println("2. Register");
		System.out.println("3. Exit");
		
		int input = Keyboard.in.readInteger().intValue();
		menu = true;
		
		while(menu){
			
			switch(input){
			case 1:
				if(login()){
					UserPage nUser = new UserPage(user);
					nUser.startUp();
				}
				else
					System.out.println("Username or password is wrong. Please try again.");
				break;
			case 2:
				register();
				break;
			case 3:
				menu = false;
				break;
			default:
				System.out.println("Please enter a valid value determined by the numbered options listed.");
				break;
			}
			
			if(menu){
				System.out.println("1. Login (for registered users)");
				System.out.println("2. Register");
				System.out.println("3. Exit");
				input = Keyboard.in.readInteger().intValue();
			}
			
		}
		
		System.out.print("Goodbye!");
		
	}
	
	public static boolean login(){
		
		String pass;
		
		System.out.print("E-mail: ");
		user = Keyboard.in.readString();
		
		System.out.print("Password: ");
		pass = Keyboard.in.readString();
		
		createString = "select email, pwd from users;";
		
		boolean exists = false;
		
		try
		{

		m_con = DriverManager.getConnection(m_url, m_userName, m_password);

		stmt = m_con.createStatement();
		ResultSet rs = stmt.executeQuery(createString);
				
		while(rs.next() && !exists){
			if(rs.getString("email") == user){
				if(rs.getString("pwd") == pass)
					exists = true;
			}
		}
				
		stmt.close();
		m_con.close();

		} catch(SQLException ex) {

		System.err.println("SQLException: " +
		ex.getMessage());

		}
		
		if(exists)
			return true;
		else
			return false;
		
		//return DEBUG;//debugging purposes
	}
	
	public static void register(){
		String email, name, city, pass, pass2; 
		char[] gender;
		
		System.out.println("Please enter your information as follows (type 1 and press enter if you have to go back " +
				"to change your information):");
		System.out.print("Please enter your e-mail: ");
		email = Keyboard.in.readString();
		System.out.print("Now your (full) name: ");
		name = Keyboard.in.readString();
		System.out.print("City: ");
		city = Keyboard.in.readString();
		System.out.print("Gender (M or F): ");
		gender = Keyboard.in.readString().toCharArray();
		while(!((gender.length == 1) && ((gender[0] != 'M') || (gender[0] != 'F') || (gender[0] != 'm') || (gender[0] != 'f')))){
			System.out.print("Please enter either M or F for gender: ");
			gender = Keyboard.in.readString().toCharArray();
		}
		System.out.print("Finally, enter your password: ");
		pass = Keyboard.in.readString();
		System.out.print("Confirm your password: ");
		pass2 = Keyboard.in.readString();
		while(pass.compareTo(pass2) != 0){
			System.out.print("Please re-enter your password: ");
			pass = Keyboard.in.readString();
			System.out.print("Confirm your password: ");
			pass2 = Keyboard.in.readString();
		}
		System.out.println("Thank you. Entering information into database... Please wait a moment.");
		
		createString = "insert into users values ('"+email+"', '"+name+"', '"+city+"', '"+gender[0]+"', '"+pass+"');";
		
		System.out.println(createString);
		
		/*try{
			m_con = DriverManager.getConnection(m_url, m_userName, m_password);
			stmt = m_con.createStatement();
			stmt.executeQuery(createString);
			
			stmt.close();
			m_con.close();
		} catch(SQLException ex) {

			System.err.println("SQLException: " +
			ex.getMessage());

		}*/
		
		System.out.println("Congrats! You have created your account. You will now be directed to the login screen.");
		Keyboard p = new Keyboard();
		p.pause();
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
		user = Keyboard.readString();
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
	message = Keyboard.readString();

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
	// If max(mid) is null, 0 is returned which works nicely.
	int messageId = stmt.executeQuery("select max(mid) from messages").getInt(1);

	// Insert into messages table.
	stmt.executeUpdate("insert into messages values('" + Integer.toString(messageId) + "', current_date, '" + content + "', '" + email + "')");

	Iterator itr = recipientList.iterator();
	while (itr.hasNext())
	{
		this.sendMessageToUser(stmt, messageId, itr.next());
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
		stmt.executeUpdate("insert into receives values('" + Integer.toString(messageId) + "', '" + recipient "')");
	}
	catch (SQLException e)
	{
		// Foreign key constraint fail.
		System.out.println("Could not send message to " + recipient + ", maybe they are not a valid user.");
	}
}
