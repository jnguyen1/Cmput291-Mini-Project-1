import java.sql.*;
import java.io.*;
import java.util.Vector;
import java.util.*;
import java.util.regex.*;

public class SortPages
{
	private class SearchPageObject implements Comparable<SearchPageObject>
	{
		private String pid;
		private java.sql.Date createDate;
		private String title;
		private String content;
		private String creator;
		private int ranking;

		/**
		 * SearchPageObject constructor.
		 *
		 * Param:
		 * pid - the page id from the db.
		 * createDate - the date the page was created.
		 * title - the title of the page.
		 * content - the content of the page.
		 * creator - the creator of the page.
		 *
		 * jnguyen1 20100307
		 */
		public SearchPageObject(String pid, java.sql.Date createDate, String title,
				String content, String creator)
		{
			this.pid = pid;
			this.createDate = createDate;
			this.title = title;
			this.content = content;
			this.creator = creator;
		}

		/**
		 * Function:
		 * Calculates the ranking based on the formula T*2+C where T is the number of
		 * matches found in title and C is the number of matches found in content.
		 * Updates the rank member variable with the calculated value.
		 *
		 * Param:
		 * keywords - the keywords used in the search and matching.
		 *
		 * Return:
		 * None.
		 *
		 * jnguyen1 20100307
		 */
		public void calculateRanking(Vector<String> keywords)
		{
			int rankingTally = 0;

			// Generate the pattern which is simply "x1|x2|x3|..."
			String stringPattern = keywords.get(0);
			for (int i=1; i<keywords.size(); i++)
			{
				stringPattern.concat("|" + keywords.get(i));
			}

			Pattern pattern = Pattern.compile(stringPattern);

			// Match against title and add 2 points for each match.
			Matcher matcher = pattern.matcher(this.title);
			while (matcher.find())
			{
				rankingTally += 2;
			}

			// Match against content and add 1 points for each match.
			matcher = pattern.matcher(this.content);
			while (matcher.find())
			{
				rankingTally++;
			}

			this.ranking = rankingTally;
		}

		/**
		 * Function:
		 * Getter for ranking member variable.
		 *
		 * Param:
		 * None.
		 *
		 * Return:
		 * The integer value of the ranking.
		 *
		 * jnguyen1 20100307
		 */
		public int getRanking()
		{
			return this.ranking;
		}

		/**
		 * Function:
		 * Compare algorithm that compares the rankings between two SearchPageObjects.
		 *
		 * Param:
		 * spo1 - the first SearchPageObject.
		 * spo2 - the second SearchPageObject.
		 *
		 * Return:
		 * integer that is negative if the spo1 is less then spo2 in ranking, 0 if equal and positive is greater.
		 *
		 * jnguyen1 20100307
		 */
		public int compareTo(SearchPageObject spo)
		{
			return this.getRanking() - spo.getRanking();
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
		/*
		 * Do somethign with the sorted result. Probably print it to user in sorted order.
		 */

		stmt.close(); 
	}
}

