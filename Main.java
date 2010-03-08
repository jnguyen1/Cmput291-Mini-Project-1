import java.sql.*;
import java.io.*;
import java.util.Vector;

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
}
