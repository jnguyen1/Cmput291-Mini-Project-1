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
