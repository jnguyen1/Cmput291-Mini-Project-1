import java.sql.*;

public class Main {

	private static String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	private static String m_driverName = "oracle.jdbc.driver.OracleDriver";

	private static String m_userName = "schwehr";
	private static String m_password = "urarurar12";

	private static Connection m_con;
	private static Statement stmt;

	private static String user;

	/**
	 * Function:
	 * Main function.
	 *
	 * Param:
	 * args - an array of the commandline arguments.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100310
	 */
	public static void main(String[] args)
	{
		try
		{
			Class drvClass = Class.forName(m_driverName);
			DriverManager.registerDriver( (Driver)drvClass.newInstance() );
			m_con = DriverManager.getConnection(m_url, m_userName, m_password);
			stmt = m_con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			System.out.println();
			System.out.println("Welcome! Please log in with your e-mail and password or choose the next option " +
					"to register if you don't have an account yet. (Type the corresponding number for the option you want)");

			int input;
			boolean menu = true;

			while(menu)
			{
				System.out.println();
				System.out.println("Main menu:");
				System.out.println("1. Login (for registered users)");
				System.out.println("2. Register");
				System.out.println("3. Exit");
				input = Keyboard.in.readInteger().intValue();

				switch(input)
				{
					case 1:
						if (login(stmt, Main.user))
						{
							System.out.println("Login successful... Please wait to be directed to the user screen.");
							UserPage nUser = new UserPage(m_con, Main.user);
							nUser.startUp();
						}
						else
							System.out.println("Username or password is wrong. Please try again.");
						break;
					case 2:
						register(stmt);
						break;
					case 3:
						menu = false;
						break;
					default:
						System.out.println("Please enter a valid value determined by the numbered options listed.");
						break;
				}
			}

			System.out.println("Goodbye!");
		}
		catch(Exception e)
		{
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());

		}
		finally
		{
			try
			{
				stmt.close();
				m_con.close();
			}
			catch (SQLException e)
			{
				System.out.println("Could not close connection.");
			}
		}
	}

	/**
	 * Function:
	 * Attempt to login the user.
	 *
	 * Param:
	 * stmt - Statement object to run select.
	 * user - the name of the account to login to.
	 *
	 * Return:
	 * True if the credentials are correct.
	 *
	 * schwehr 20100310
	 */
	private static boolean login(Statement stmt, String user)
	{
		String pass;

		System.out.print("E-mail: ");
		user = Keyboard.in.readString();

		System.out.print("Password: ");
		pass = Keyboard.in.readString();

		try
		{
			String query = String.format("select email, pwd from users where email ='%s' and pwd ='%s'", user, pass);
			ResultSet rset = stmt.executeQuery(query);		

			// There should only be one record found if the credentials are valid..
			rset.last();
			if (rs.getRow() == 1)
			{
				return true;
			}
		}
		catch(SQLException ex)
		{
			System.err.println("SQLException: " + ex.getMessage());
		}

		return false;
	}

	/**
	 * Function:
	 * Register a new user.
	 *
	 * Param:
	 * stmt - Statement object to run select and insert query.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310 
	 */
	private static void register(Statement stmt)
	{
		String email, name, city, pass, pass2; 
		char[] gender;

		System.out.println("Welcome to the registration process.");

		System.out.print("Enter your e-mail: ");
		email = Keyboard.in.readString();

		System.out.print("Enter your (full) name: ");
		name = Keyboard.in.readString();

		System.out.print("Enter the City: ");
		city = Keyboard.in.readString();

		do
		{
			System.out.print("Please enter either M or F for gender: ");
			gender = Keyboard.in.readString().toLowerCase().toCharArray();
		} while ( !(gender.length == 1 && (gender[0] == 'm' || gender[0] == 'f')) );

		System.out.print("Finally, enter your password: ");
		pass = Keyboard.in.readString();

		System.out.print("Confirm your password: ");
		pass2 = Keyboard.in.readString();

		while (pass.compareTo(pass2) != 0)
		{
			System.out.println("Passwords do not match!");

			System.out.print("Please re-enter your password: ");
			pass = Keyboard.in.readString();

			System.out.print("Confirm your password: ");
			pass2 = Keyboard.in.readString();
		}

		System.out.println("Thank you. Entering information into database... Please wait a moment.");

		try
		{
			String query = String.format("insert into users values ('%s','%s','%s','%c','%s')", email, name, city, gender[0], pass);
			stmt.executeUpdate(query);

			System.out.println("Congrats! You have created your account. You will now be directed to the login screen.");
			Keyboard.in.pause();
		}
		catch (SQLException ex)
		{
			System.err.println("SQLException: " + ex.getMessage());
		}
	}

}
