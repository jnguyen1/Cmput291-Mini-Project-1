import java.sql.*;
import java.util.*;

public class UserPage {

	private String email;
	private Statement stmt;

	public UserPage(Connection conn, String email) throws SQLException
	{
		this.email = email;
		this.stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}

	/**
	 * Destructor.
	 * Close statement.
	 *
	 * jnguyen1 20100311
	 */
	protected void finalize() throws Throwable
	{
		try
		{
			this.stmt.close();
		}
		catch (SQLException e)
		{
			System.out.println("Error closing statement.");
		}
		super.finalize();
	}

	/**
	 * Function:
	 * The main function and menu loop for a user who is already logged in.
	 *
	 * Param:
	 * None
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	public void startUp() throws SQLException
	{
		String name = "wondefully named character";
		String createString = "select name from users where email = '" + this.email + "'";

		ResultSet rset = this.stmt.executeQuery(createString);
		if(rset.next())
		{
			name = rset.getString("name").trim();
		}

		System.out.println();
		System.out.println("Welcome " + name + ". Here are your options for today.");

		//if there are notifications, show them first then menu
		this.checkFreqests(this.stmt);

		boolean run = true;
		while(run)
		{
			System.out.println();
			System.out.println("Menu:");
			System.out.println("1. Search for pages.");
			System.out.println("2. Search for users.");
			System.out.println("3. Send friend request.");
			System.out.println("4. Post a status.");
			System.out.println("5. List all friends' status posts.");
			System.out.println("6. Send a message.");
			System.out.println("7. Check inbox.");
			System.out.println("8. Logout.");
			int input = Keyboard.in.readInteger().intValue();

			switch(input)
			{
				case 1:
					this.searchPages(this.stmt);
					break;
				case 2:
					this.searchUsers(this.stmt);
					break;
				case 3:
					this.sendFriendRequest(this.stmt);
					break;
				case 4:
					this.postStatus(this.stmt);
					break;
				case 5:
					this.listStatus(this.stmt);
					break;
				case 6:
					this.sendMessage(this.stmt);
					break;
				case 7:
					this.checkMessages(this.stmt);
					break;
				case 8:
					run = false;
					break;
				default:
					System.out.println("Please enter a valid numerical value which corresponds to the menu.");
					break;
			}
		}
	}

	/**
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
		System.out.print("Search: ");
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
		String[] input = Keyboard.in.readString().split(" ");

		Vector<String> keywords = new Vector<String>(Arrays.asList(input));
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
					stmt.executeUpdate("insert into fans values('" + this.email + "', '" + results.get(choice).getPid() + "', current_date)");
					System.out.println("You became a fan of " + results.get(choice).getTitle());
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
			System.out.format("%3d %16s %12s %1s\n", rset.getRow(), rset.getString("email"),
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
				rset.last();
				if (menuChoice > rset.getRow() || menuChoice < 1)
				{
					System.out.println("Invalid choice. Returning to menu.");
					break;
				}

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

				// We are not looping. But the infrastructure is here.
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
		// Because the statement can only have one result set data at one time, we have to grab and
		// store whatever data we need before moving on to the next query.
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
	 * Send a friend request to another user.
	 *
	 * Param:
	 * stmt - the Statement object to execute statements on.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100311
	 */
	private void sendFriendRequest(Statement stmt)
	{
		System.out.println("Enter email to send friend request to.");
		String friend = Keyboard.in.readString();

		try
		{
			ResultSet rset = stmt.executeQuery("select * from users where email = '" + friend + "'");

			rset.last();
			if (rset.getRow() != 1)
			{
				System.out.println("Your friend does not exist.");
				return;
			}

			rset = stmt.executeQuery("select * from friends where email = '" + friend + "' and femail = '" + this.email + "'");
			rset.last();
			if (rset.getRow() != 0)
			{
				System.out.println("You are already friends. Friends don't forget friends.");
				return;
			}

			String query = String.format("insert into friend_requests values('%s', '%s', 'N')", this.email, friend);
			stmt.executeUpdate(query);

			System.out.println("Request sent!");
		}
		catch (SQLException e)
		{
			System.out.println("Could not send friend request.");
		}
	}

	/**
	 * Function:
	 * Check for new friend requests and accept, reject, or ignore these requests.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	private void checkFreqests(Statement stmt) throws SQLException
	{
		ResultSet rset = stmt.executeQuery("select f.* from users u,friend_requests f where u.email = '" + this.email + "' and u.email = f.femail");

		System.out.println();
		while (rset.next())
		{
			if (rset.getString("checked").toLowerCase().toCharArray()[0] == 'n')
			{
				String friend = rset.getString("email");
				System.out.println("You have a friend request from: " + friend);

				char ans[];
				do
				{
					System.out.print("Accept or ignore? (Y[es] or N[o] or I[gnore]): ");
					ans = Keyboard.in.readString().toLowerCase().toCharArray();
					System.out.println(ans[0]);
				} while( !(ans.length == 1 && (ans[0] == 'y' || ans[0] == 'n' || ans[0] == 'i')));

				// We still need rset for our loop. So we'll just pass a clone down.
				Statement cloneStmt = stmt.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

				if (ans[0] == 'y')
				{
					this.addFriend(cloneStmt, friend);
				}
				else if (ans[0] == 'n')
				{
					this.rejectFriend(cloneStmt, friend);
				}
				else if (ans[0] == 'i')
				{
					this.ignoreRequest(cloneStmt, friend);
				}

				cloneStmt.close();
			}
		}
	}

	/**
	 * Function:
	 * Accept a friend request. Insert into friends table. Send a message to the friend declaring
	 * friendship. Remove the friend_requests record.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * requestor - the user requesting friendship.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	private void addFriend(Statement stmt, String requestor)
	{
		try
		{
			int mid;
			ResultSet rset = stmt.executeQuery("select max(mid) from messages");
			if (!rset.next())
			{
				System.out.println("Could not get mid.");
				return;
			}
			else
			{
				mid = rset.getInt(1) + 1;
			}

			stmt.executeUpdate("insert into friends values ('" + requestor + "', '" + this.email + "')");
			stmt.executeUpdate("insert into friends values ('" + this.email + "', '" + requestor + "')");
			stmt.executeUpdate("insert into messages values (" + mid + ",sysdate,'I have accepted your friend request!','" + this.email + "')");
			stmt.executeUpdate("insert into receives values (" + mid + ",'" + requestor + "')");
			stmt.executeUpdate("delete from friend_requests where femail='" + this.email + "' and email='" + requestor +"'");
			System.out.println("Friend request accepted.");
		}
		catch(SQLException ex)
		{
			System.err.println("Could not add friend: " + ex);
		}
	}

	/**
	 * Function:
	 * Reject a friend request. Send a message of rejection.
	 * Remove the friend_requests record.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * requestor - the user requesting friendship.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	private void rejectFriend(Statement stmt, String requestor)
	{
		try
		{
			int mid;
			ResultSet rset = stmt.executeQuery("select max(mid) from messages");
			if (!rset.next())
			{
				System.out.println("Could not get mid.");
				return;
			}
			else
			{
				// Get the previous max and increment it by one obtain next number in sequence. 
				// Should have used a damn index.
				mid = rset.getInt(1) + 1;
			}

			stmt.executeUpdate("insert into messages values (" + mid + ",sysdate,'I have rejected your friend request!','" + this.email + "')");
			stmt.executeUpdate("insert into receives values (" + mid + ",'" + requestor + "')");
			System.out.println("WE jhave inserted");
			stmt.executeUpdate("delete from friend_requests where femail='"+ this.email + "' and email='" + requestor + "'");
			System.out.println("Friend request rejected.");
		}
		catch (SQLException ex)
		{
			System.err.println("Could not reject friend: " + ex);
		}
	}

	/**
	 * Function:
	 * Ignore a friend request. Update the friend_requests record and mark as checked.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * requestor - the user requesting friendship.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	private void ignoreRequest(Statement stmt, String requestor)
	{
		try
		{
			stmt.executeUpdate("update friend_requests set checked = 'y' where femail='" + this.email + "' and email='" + requestor + "'");
			System.out.println("Friend request ignored.");
		}
		catch (SQLException ex)
		{
			System.err.println("Could not update friend_requests: " + ex);
		}
	}

	/**
	 * Function:
	 * Inserts a new status for the user.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 *
	 * Return:
	 * None.
	 *
	 * schwehr 20100310
	 */
	private void postStatus(Statement stmt){
		String status, query;
		int sno;
		try
		{
			ResultSet rset = stmt.executeQuery("select max(sno) from status");
			if (!rset.next())
			{
				throw new SQLException("No sno.");
			}
			else
			{
				// Obtain next sno by incrementing max.
				sno = rset.getInt(1) + 1;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Could not get next sno.");
			return;
		}
			
		do
		{
			System.out.print("Enter Status (max 40 chars.): ");
			status = Keyboard.in.readString();
		} while(status.length() >40);

		query = "insert into status values ('" + this.email + "'," + sno + ",'" + status + "',sysdate)";

		try
		{
			stmt.executeUpdate(query);
			System.out.println("Status posted successfully!");
		}
		catch(SQLException ex)
		{
			System.err.println("Could not post status: " + ex.getMessage());
		}
	}

	/**
	 * Function:
	 * List the status posts of the user's friends that are within 3 days. Then the comments
	 * related to that status are displayed. The user can add their own comment.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100311
	 */
	private void listStatus(Statement stmt){
		try
		{
			ResultSet rset = stmt.executeQuery("select s.* from status s join friends f on (s.email = f.femail)" +
					"where f.email = '" + this.email + "' and round(pdate) >= round(sysdate)-3"); 
			while (rset.next())
			{
				String statusResult = String.format("%3d %25s %s %s\n",
						rset.getRow(), rset.getString("email"), rset.getString("pdate"), rset.getString("text"));
				System.out.println(statusResult);
				Statement cloneStmt = stmt.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				this.listComments(cloneStmt,rset.getString("email"), rset.getInt("sno"));
				cloneStmt.close();
			}

			rset.last();
			int count = rset.getRow();
			if (count == 0)
			{
				System.out.println("No status postings found.");
				return;
			}

			System.out.println("Choose a status to comment on (-1 to cancel)");
			int choice = Keyboard.in.readInteger();
			if (choice == -1 || choice > count)
			{
				return;
			}
			else
			{
				// Get the data we need before 'releasing' the result set back out.
				rset.absolute(choice);
				int sno = rset.getInt("sno");
				String semail = rset.getString("email");

				System.out.println("Enter comment:");
				String comment = Keyboard.in.readString();
				
				// Calculate the next cno number by incrementing the max value.
				rset = stmt.executeQuery("select max(cno) from comments");
				rset.next();
				int cno = rset.getInt(1) + 1;

				String insert = String.format("insert into comments values('%s',%d,%d,'%s','%s',sysdate)", this.email, cno, sno, semail,comment);
				stmt.executeUpdate(insert);
				System.out.println("You have commented succesfully to the status.");
			}
		}
		catch (SQLException e)
		{
			System.out.println("Could not get status listing." + e.getMessage());
		}
	}
	
	/**
	 * Function:
	 * List all the comments of a status post.
	 *
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * senderEmail - the user who made the comment.
	 * sno - the number of the status posting.
	 *
	 * Return:
	 * None.
	 *
	 * jnguyen1 20100311
	 */
	private void listComments(Statement stmt, String senderEmail, int sno)
	{
		try
		{
			ResultSet rset = stmt.executeQuery("select * from comments where semail = '" + senderEmail + "' and sno = " + sno);
			while (rset.next())
			{
				String commentResult = String.format("	Comment:%25s %s %s\n",
						rset.getString("email"), rset.getString("cdate"), rset.getString("text"));
				System.out.println(commentResult);
			}

			System.out.println();
		}
		catch (SQLException e)
		{
			System.out.println("Could not get comments." + e.getMessage());
		}
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

		recipientList = this.getValidUsers(stmt, recipientList);

		// No users to send to, no message needed.
		if (recipientList.size() == 0)
		{
			return;
		}

		do
		{
			System.out.println("Enter your message (max 40 chars.), followed by newline.");
			message = Keyboard.in.readString();
		} while(message.length() > 40);

		this.sendMessageSql(stmt, recipientList, message);
	}

	/**
	 * Function:
	 * Finds the users from the list that are valid users in the users table.
	 * 
	 * Param:
	 * stmt - Statement object to execute sql statements on.
	 * users - a list of users that need to be verified.
	 *
	 * Return:
	 * A new list of users that have a match in the table.
	 *
	 * jnguyen1 20100311
	 */
	private Vector<String> getValidUsers(Statement stmt, Vector<String> users)
	{
		Vector<String> validUsers = new Vector<String>();

		try
		{
			String query = "select email from users where email = '" + users.get(0) + "'";
			for (int i=1; i<users.size(); i++)
			{
				query = query.concat(" or email = '" + users.get(i) + "'");
			}

			ResultSet rset = stmt.executeQuery(query);
			while (rset.next())
			{
				validUsers.add(rset.getString("email"));
			}
		}
		catch (SQLException e)
		{
			System.out.println("No valid user in list.");
		}

		return validUsers;
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
			ResultSet rset = stmt.executeQuery("select max(mid) from messages");
			rset.next();
			// If max(mid) is null, 0 is returned which works nicely.
			int messageId = rset.getInt(1) + 1;

			// Insert into messages table.
			stmt.executeUpdate("insert into messages values('" + messageId + "', current_date, '" + content + "', '" + this.email + "')");

			Iterator itr = recipientList.iterator();
			while (itr.hasNext())
			{
				this.sendMessageToUserSql(stmt, messageId, (String)itr.next());
			}

			System.out.println("Message sent to users.");
		}
		catch (SQLException e)
		{
			System.out.println("Could not create message." + e.getMessage());
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
			stmt.executeUpdate("insert into receives values('" + messageId + "', '" + recipient + "')");
		}
		catch (SQLException e)
		{
			// Foreign key constraint fail.
			System.out.println("Could not send message to " + recipient + ", maybe they are not a valid user.");
		}
	}

	private void checkMessages(Statement s)
	{
		try
		{
			ResultSet rset = stmt.executeQuery("select * from messages join receives using (mid) where receives.email = '" + this.email + "'");

			rset.last();
			if (rset.getRow() == 0)
			{
				System.out.println("No messages in inbox.");
				return;
			}
			else
			{
				rset.beforeFirst();
			}

			while (rset.next())
			{
				Statement cloneStmt = stmt.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

				String message = String.format("%s %s %s\n", rset.getString("sender"), rset.getString("mdate"), rset.getString("text"));
				System.out.println(message);
				char choice;
				do
				{
					System.out.print("Delete? (Y[es] or N[o]: ");
					choice = Keyboard.in.readString().toLowerCase().charAt(0);
				} while( choice != 'y' && choice != 'n');

				if (choice == 'y')
				{
					cloneStmt.executeUpdate("delete from receives where email = '" + this.email + "' and mid = " + rset.getInt("mid"));
					// Check if the message no longer has any entry in receives.
					ResultSet cloneRset = cloneStmt.executeQuery("select count(*) from messages join receives using (mid) where mid = '" + rset.getString("mid") + "'");
					cloneRset.last();
					if (cloneRset.getRow() == 0)
					{
						cloneStmt.executeUpdate("delete from messages where mid = " + rset.getInt("mid"));
					}
				}

				cloneStmt.close();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Could not check messages." + e.getMessage());
		}
	}

}
