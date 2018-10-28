package chat;
/**
 * Vocabulary of special commands the user can send to server
 * @author davidroussel
 */
public interface Vocabulary
{
	/**
	 * Keyword used to logout from server and quit the client
	 */
	public final static String byeCmd="bye";

	/**
	 * Keyword used (by a super user) to kill the server
	 */
	public final static String killCmd="kill";

	/**
	 * Keyword used (by a super user) to kick other user from server:kick <username>
	 */
	public final static String kickCmd="kick";

	/**
	 * Keyword used to ask server for all recorded messages:
	 * the server then sends us all recorded messages
	 */
	public final static String catchUpCmd="catchup";

	/**
	 * Line separator used on this OS (used in text)
	 */
	public final static String newLine = System.getProperty("line.separator");

	/**
	 * Array containing all commands so we can use it to check if user has typed
	 * any special command
	 */
	public final static String[] commands =
	{
		byeCmd,
		kickCmd,
		killCmd,
		catchUpCmd
	};
}
