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
	static String email;
	static String friend = new String();
	static String friends[] = new String[25];

	public UserPage(String e){
		email = e;
	}

	static boolean loggedOn = true;
	public void startUp() throws SQLException{
		createString = "select name from users where email = '"+email+"'";
		
		Main.stmt = Main.m_con.createStatement();
		ResultSet rs = Main.stmt.executeQuery(createString);
		
		if(rs.next())
			user = rs.getString("name").trim();
		
		Main.stmt.close();
		
		System.out.println();
		System.out.println("Welcome "+user+". Here are your options for today.");
		//if there are notifications, show them first then menu
	
		if(this.checkFreqests(Main.m_con,Main.stmt, friend, friends));
				
		String in;
		boolean run = true;
		
		while(run){
			System.out.println();
			System.out.println("Menu:");
			System.out.println("1. Search for pages.");
			System.out.println("2. Search for users.");
			System.out.println("3. Post a status.");
			System.out.println("4. List all friends' status posts.");
			System.out.println("5. Send a message.");
			System.out.println("6. Check inbox.");
			System.out.println("7. Logout.");
			int input = Keyboard.in.readInteger().intValue();
			
			switch(input){
			case 1:
				System.out.print("Search: ");
				in = Keyboard.in.readString();
				in.toLowerCase();
			
				String searchArr[] = in.split(" ");
				Vector<String> words = new Vector<String>();
				for(int i = 0; i < searchArr.length; i++)
					words.add(searchArr[i]);
			
				this.searchPages(Main.m_con, words);
				break;
			case 2:
				System.out.print("Search user: ");
				in = Keyboard.in.readString();
				this.searchUser(Main.m_con, Main.stmt, in);
				break;
				//method searches for user
			case 3:
				System.out.print("Status: ");
				in = Keyboard.in.readString();
				this.postStatus(Main.m_con, Main.stmt, in);
				break;
			case 4:
				this.fstatus(Main.m_con, Main.stmt);
				break;
			case 5:
				this.sendMessage(Main.stmt);
				break;
			case 6:
				this.inbox(Main.m_con, Main.stmt);
				break;
			case 7:
				run = false;
				break;
			default:
				System.out.println("Please enter a valid numerical value which corresponds to the menu.");
				break;
			}
		}
	}
	
	private void searchUser(Connection conn, Statement s, String name) throws SQLException{
		String strArr[] = name.split(" ");
		String condition = "name like '%" + strArr[0] + "%' or email like '%" + strArr[0] + "%'";
		for (int i=1; i<strArr.length; i++)
		{
			condition.concat(" or name like '%" + strArr[i] + "%' or email like '%" + strArr[i] + "%'");
		}
		
		String str = "select * from users where "+condition;
		
		s = conn.createStatement();
		ResultSet rs = s.executeQuery(str);
		
		if(rs == null)//rs can't be null... find some way to put if output is empty
			System.out.println("Sorry, that user does not exist. If you are looking for a name, try captailizing the first letter of the name you are looking for.");
		else{
			System.out.println("Results:");
			while(rs.next())
				System.out.println(rs.getString("name").trim()+" "+rs.getString("email").trim()+" "+rs.getString("city").trim()+" "+rs.getString("gender").trim());
		}
		rs.close();
		s.close();
	}
	
	static int sno = 0;
	private void postStatus(Connection conn, Statement s, String status){
		sno++;
		String str = "insert into status values ('"+email+"',"+sno+",'"+status+"',sysdate)";
		System.out.println(status);
		
		try{
		s = conn.createStatement();
		s.executeUpdate(str);
		
		s.close();
		
		System.out.println("Status posted successfully!");
		}
		catch(SQLException ex) {

			System.err.println("SQLException: " +
			ex.getMessage());

		}
		
	}
	
	private void fstatus(Connection conn, Statement s){
		//john is working on this
	}
	
	private void inbox(Connection conn, Statement s){
		
	}
	
	private boolean checkFreqests(Connection conn, Statement s, String fri, String[] fris) throws SQLException{
		s = conn.createStatement();
		ResultSet rs = s.executeQuery("select f.* from users u,friend_requests f where u.email = '"+email+"' and u.email = f.femail");


		System.out.println();
		while(rs.next()){
			if(rs.getString("checked").toLowerCase().toCharArray()[0] == 'n'){
				System.out.println("You have a friend request from: "+rs.getString("email"));
				friend = rs.getString("email");
				char ans[];
				do
				{
					System.out.print("Accept or ignore? (Y[es] or N[o] or I[gnore]): ");
					ans = Keyboard.in.readString().toLowerCase().toCharArray();
					System.out.println(ans[0]);
				} while( !(ans.length == 1 && (ans[0] == 'y' || ans[0] == 'n' || ans[0] == 'i')));
				if(ans[0] == 'y')
					this.addFriend(friend,email,Main.stmt);
				else if(ans[0] == 'n')
					this.rejectFriend(friend,email,Main.stmt);
				else if(ans[0] == 'i')
					this.ignoreRequest(friend,email,Main.stmt);
			}
		
		}
		rs.close();
				
		rs = s.executeQuery("select f.* from users u,friend_requests f where u.email = '"+email+"' and u.email = f.femail");
		
		if(rs.next()){
			rs.close();
			return true;
		}
			
		rs.close();
		s.close();

		return false;
	}
	
	/*
	 * parameters for the next 3 methods:
	 * str1 = femail (inviter)
	 * str2 = email (invitee)
	 * s = Main.stmt
	 */
	private void addFriend(String str1, String str2, Statement s){
		try{
			s = Main.m_con.createStatement();
			s.executeUpdate("insert into friends values ('"+str1+"', '"+str2+"')");
			s.close();
			s = Main.m_con.createStatement();
			s.executeUpdate("insert into friends values ('"+str2+"', '"+str1+"')");
			s.close();
			s = Main.m_con.createStatement();
			s.executeUpdate("insert into messages values ('1',sysdate,'I have accepted your friend request!','"+str2+"')");
			s.close();
			s = Main.m_con.createStatement();
			s.executeUpdate("insert into receives values ('1','"+str1+"')");
			s.close();
			s = Main.m_con.createStatement();
			s.executeUpdate("delete from friend_requests where femail='"+str1+"' and email='"+str2+"'");
			s.close();
			System.out.println("Friend request accepted.");
		} catch(SQLException ex){
			System.err.println("SQLException: " + ex);
		}
	}
	
	private void rejectFriend(String str1, String str2, Statement s){
		try{
			s = Main.m_con.createStatement();
			s.executeUpdate("insert into messages values ('1',sysdate,'I have rejected your friend request!','"+str2+"')");
			s.executeUpdate("insert into receives values ('1','"+str1+"')");
			s.executeUpdate("delete from friend_requests where femail='"+str1+"' and email='"+str2+"'");
			s.close();
			System.out.println("Friend request rejected.");
		} catch(SQLException ex){
			System.err.println("SQLException: " + ex);
		}
	}
	
	private void ignoreRequest(String str1, String str2, Statement s){
		try{
			s = Main.m_con.createStatement();
			s.executeUpdate("update friend_requests set checked = 'y' where femail='"+str1+"' and email='"+str2+"'");
			s.close();
			System.out.println("Friend request ignored.");
		} catch(SQLException ex){
			System.err.println("SQLException: " + ex);
		}
	}


	/*
	 * Function:
	 * Searches through pages table for records whose title or content contains the supplied keywords.
	 * Display the results in a sorted order that places greater weighting on matches on title (T*2+C).
	 *
	 * Param:
	 * stmt - the Statement object to execute statements on.
	 *
	 * Return:
	 * None.
	 *
	 * Exceptions:
	 * SQLException caused by executeQuery.
	 *
	 * jnguyen1 20100307
	 */
	private void searchPages(Statement stmt) throws SQLException
	{
		Vector<String> keywords = this.getKeywords();
		if (keywords.size() == 0)
		{
			System.out.println("No word to search.");
			return;
		}

		Vector<SearchPageObject> results = new Vector<SearchPageObject>();

		String condition = "title like '%" + keywords.get(0) + "%' or content like '%" + keywords.get(0) + "%'";
		for (int i=1; i<keywords.size(); i++)
		{
			condition = condition.concat(" or title like '%" + keywords.get(i) + "%' or content like '%" + keywords.get(i) + "%'");
		}

		ResultSet rset = stmt.executeQuery("select * from pages where " + condition); 

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

		rset.last();
		if (rset.getRow() == 0)
		{
			System.out.println("No page found that matches keywords.");
			return;
		}

		Collections.sort(results);
		Collections.reverse(results);

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
				case 1:
					fanPageRequest(stmt, results);
					menuLoop = false;
					break;
				case 2:
					menuLoop = false;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Function:
	 * Gets a list of keywords.
	 *
	 * Param:
	 *
	 * Return:
	 * Vector of strings that are the desired keywords.
	 *
	 * jnguyen1 20100309
	 */
	private Vector<String> getKeywords()
	{
		Vector<String> keywords = new Vector<String>();
		String word;

		System.out.println("Enter keywords to search title and content for. Newline to finish list.");
		while (true)
		{
			word = Keyboard.in.readString();
			if (word.length() == 0)
			{
				break;
			}
			else
			{
				keywords.add(word);
			}
		}

		return keywords;
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
					stmt.executeUpdate("insert into fans values('" + Main.user + "', '" + results.get(choice).getPid() + "', current_date)");
				}
				catch (SQLException e)
				{
					System.out.println("Could not register as fan of page.");
				}

				return;
			}
		}
	}

	/*
	 * Function:
	 * Searches through pages table for records whose title or content contains the supplied keywords.
	 * Display the results in a sorted order that places greater weighting on matches on title (T*2+C).
	 *
	 * Param:
	 * stmt - the Statement object to execute statements on.
	 *
	 * Return:
	 * None.
	 *
	 * Exceptions:
	 * SQLException caused by executeQuery.
	 *
	 * jnguyen1 20100307
	 */
	private void searchUsers(Statement stmt) throws SQLException
	{
		// Only "a" keyword when searching for users.
		//Vector<String> keywords = this.getKeywords();
		Vector<String> keywords = new Vector<String>();
		System.out.println("Enter the keyword to search users.");
		keywords.add(Keyboard.in.readString());

		if (keywords.size() == 0)
		{
			System.out.println("No word to search.");
			return;
		}

		String condition = "name like '%" + keywords.get(0) + "%' or email like '%" + keywords.get(0) + "%'";
		for (int i=1; i<keywords.size(); i++)
		{
			condition = condition.concat(" or name like '%" + keywords.get(i) + "%' or email like '%" + keywords.get(i) + "%'");
		}

		ResultSet rset = stmt.executeQuery("select email, name, city, gender from users where " + condition); 

		while(rset.next())
		{ 
			System.out.format("%3d %20s %20s %20s\n", rset.getRow(), rset.getString("email"),
					rset.getString("name"),
					rset.getString("city"),
					rset.getString("gender")
					);
		} 

		rset.last();
		if (rset.getRow() == 0)
		{
			System.out.println("No users that matched keyword search.");
			return;
		}

		while (true)
		{
			System.out.println("Select user number to request additional stats. -1 to cancel.");
			int menuChoice = Keyboard.in.readInteger();
			if (menuChoice == -1)
			{
				break;
			}
			else
			{
				// Request user stat if we can move to selected entry from rset.
				if(rset.absolute(menuChoice))
				{
					try
					{
						this.requestUserStat(stmt, rset.getString("email"));
					}
					catch (SQLException e)
					{
						System.out.println("Could not request user stats.");
					}
				}

				// We are not looping. But infrastructure is here.
				break;
			}
		}
	}

	/**
	 * Function:
	 * Gather and print additional stats related to user.
	 *
	 * Param:
	 * stmt - the Statement object to execute statements on.
	 * email - the user to get the stats of.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100309
	 */
	private void requestUserStat(Statement stmt, String email) throws SQLException
	{
		ResultSet rset = null;
		String friendCount, statusCount, commentCount, messageCount;

		rset = stmt.executeQuery("select count(*) from friends where email = '" + email + "'");
		rset.first();
		friendCount = rset.getString(1);

		rset = stmt.executeQuery("select count(*) from status where email = '" + email + "'");
		rset.first();
		statusCount = rset.getString(1);

		rset = stmt.executeQuery("select count(*) from comments where email = '" + email + "'");
		rset.first();
		commentCount = rset.getString(1);

		rset = stmt.executeQuery("select count(*) from messages where sender = '" + email + "'");
		rset.first();
		messageCount = rset.getString(1);

		System.out.println("The user '" + email + "' has the following stats.");
		System.out.println(friendCount + " number of friends.");
		System.out.println(statusCount + " number of status postings.");
		System.out.println(commentCount + " number of comments made.");
		System.out.println(messageCount + " number of messages sent.");
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
