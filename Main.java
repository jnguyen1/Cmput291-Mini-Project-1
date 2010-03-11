import java.sql.*;

public class Main {

	private static String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	private static String m_driverName = "oracle.jdbc.driver.OracleDriver";

	private static String m_userName = "";
	private static String m_password = "";

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

			System.out.println("Enter username to db:");
			m_userName = Keyboard.in.readString();

			System.out.println("Enter password to db:");
			m_password = Keyboard.in.readString();

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
						if (login(stmt))
						{
							System.out.println("Login successful... Please wait to be directed to the user screen.");

							// login() has initialized Main.user with a value.
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
	 *
	 * Return:
	 * True if the credentials are correct.
	 *
	 * schwehr 20100310
	 */
	private static boolean login(Statement stmt)
	{
		String pass;

		System.out.print("E-mail: ");
		Main.user = Keyboard.in.readString();

		System.out.print("Password: ");
		pass = Keyboard.in.readString();

		try
		{
			String query = String.format("select email, pwd from users where email ='%s' and pwd ='%s'", Main.user, pass);
			ResultSet rset = stmt.executeQuery(query);		

			// There should only be one record found if the credentials are valid..
			rset.last();
			if (rset.getRow() == 1)
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
		String email, name, city, pass1, pass2; 
		char gender;

		System.out.println("Welcome to the registration process.");

		do
		{
			System.out.print("Enter your e-mail (1-25 char): ");
			email = Keyboard.in.readString();
		} while (email.length() > 25 || email.length() == 0);

		try
		{
			ResultSet rset = stmt.executeQuery("select * from users where email = '" + email + "'");
			rset.last();
			if (rset.getRow() != 0)
			{
				System.out.println("User exists already. Cannot register another user with the same email.");
				return;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error checking unique identity during registration.");
		}

		do
		{
			System.out.print("Enter your (full) name (0-16 char): ");
			name = Keyboard.in.readString();
		} while (name.length() > 16);

		do
		{
			System.out.print("Enter the City (0-12 char): ");
			city = Keyboard.in.readString();
		} while (city.length() > 12);

		do
		{
			System.out.print("Please enter either M or F for gender: ");
			gender = Keyboard.in.readString().toLowerCase().charAt(0);
		} while (gender != 'm' && gender != 'f');

		System.out.print("Finally, enter your password: ");
		pass1 = Keyboard.in.readString();

		System.out.print("Confirm your password: ");
		pass2 = Keyboard.in.readString();

		while (pass1.length() > 4 || pass1.compareTo(pass2) != 0)
		{
			System.out.println("Passwords do not match!");

			System.out.print("Please re-enter your password: ");
			pass1 = Keyboard.in.readString();

			System.out.print("Confirm your password: ");
			pass2 = Keyboard.in.readString();
		}

		System.out.println("Thank you. Entering information into database... Please wait a moment.");

		try
		{
			String query = String.format("insert into users values ('%s','%s','%s','%c','%s')", email, name, city, gender, pass1);
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
