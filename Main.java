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

}
