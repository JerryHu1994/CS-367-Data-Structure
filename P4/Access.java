/**
 * Represents the access to a folder or a file of a particular user
 * 
 * @author Jerry
 *
 */
public class Access {

	/* The user who is represented by this access */
	private User user;
	/* The type of the access */
	private char accessType;

	/**
	 * Constructs a Access object throws IllegalArgumentException when the input
	 * user is null;
	 * 
	 * @param user
	 *            the user of the access
	 * @param accessType
	 *            the type of the access
	 */
	public Access(User user, char accessType) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		this.user = user;
		this.accessType = accessType;
	}

	/**
	 * Returns the user of the access object
	 * 
	 * @return the user of the access object
	 */
	public User getUser() {
		// TODO
		return user;
	}

	/**
	 * Returns the type of the access object
	 * 
	 * @return the type of the access object
	 */
	public char getAccessType() {
		// TODO
		return accessType;
	}

	/**
	 * Set the accesstype to the given char
	 * 
	 * @param accessType
	 *            supposed to set
	 */
	public void setAccessType(char accessType) {
		this.accessType = accessType;
	}

	/*
	 * Get the String representation of an accesstype
	 */
	public String toString() {
		return (user.getName() + ":" + accessType);
	}

}
