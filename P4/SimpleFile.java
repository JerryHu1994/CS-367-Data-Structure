import java.util.ArrayList;

/**
 * Represent a file which could be a type of either "txt, doc, rtf" and could
 * store content.
 * 
 * @author Jerry
 *
 */
public class SimpleFile {
	/* The string represents the name of the file */
	private String name;
	/* The extension which determines the type of the file */
	private Extension extension;
	/* The content of a file */
	private String content;
	/* The user who owns the file */
	private User owner;
	/* The arraylist of access */
	private ArrayList<Access> allowedUsers;
	/* The string representation of the path */
	private String path;
	/* The simplefolder which is the parent of the file */
	private SimpleFolder parent;

	/**
	 * contructs a simpleile object throws IllegalArgumentException when the
	 * name, extension, content, parent or the owner is null
	 * 
	 * @param name
	 *            the name of the simplefile
	 * @param extension
	 *            the extension of the filename
	 * @param path
	 *            the path of the file
	 * @param content
	 *            the content of the file
	 * @param parent
	 *            the parent simple folder of the simplefile
	 * @param owner
	 *            the user who owns the file
	 */
	public SimpleFile(String name, Extension extension, String path,
			String content, SimpleFolder parent, User owner) {
		if (name == null || extension == null || path == null
				|| content == null || parent == null || owner == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.extension = extension;
		this.content = content;
		this.owner = owner;
		this.allowedUsers = new ArrayList<Access>();
		this.path = path;
		this.parent = parent;
	}

	// returns the path variable.
	public String getPath() {
		return path;
	}

	// return the parent folder of this file.
	public SimpleFolder getParent() {
		return parent;
	}

	// returns the name of the file.
	public String getName() {
		return name;
	}

	// returns the extension of the file.
	public Extension getExtension() {
		return extension;
	}

	// returns the content of the file.
	public String getContent() {
		return content;
	}

	// returns the owner user of this file.
	public User getOwner() {
		return owner;
	}

	// returns the list of allowed user of this file.
	public ArrayList<Access> getAllowedUsers() {
		return allowedUsers;
	}

	// adds a new access(user+accesstype pair) to the list of allowed user.
	// If the access is null, throw IllegalArgumentException
	public void addAllowedUser(Access newAllowedUser) {
		if (newAllowedUser == null) {
			throw new IllegalArgumentException();
		}
		allowedUsers.add(newAllowedUser);
	}

	// adds a list of the accesses to the list of allowed users.
	// If the arraylist is null, throw IllegalArgumentException
	public void addAllowedUsers(ArrayList<Access> newAllowedUser) {
		if (newAllowedUser == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < newAllowedUser.size(); i++) {
			allowedUsers.add(newAllowedUser.get(i));
		}
	}

	// returns true if the user name is in allowedUsers.
	// Otherwise return false.
	// If the name is null, throw IllegalArgumentException
	public boolean containsAllowedUser(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < allowedUsers.size(); i++) {
			if (allowedUsers.get(i).getUser().getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	// removes the file for all users.
	// If the user is owner of the file or the admin or the user has 'w' access,
	// then it is removed for everybody.
	// If the user is null, throw IllegalArgumentException
	public boolean removeFile(User removeUsr) {
		if (removeUsr == null) {
			throw new IllegalArgumentException();
		}
		if (removeUsr.equals(owner) || removeUsr.equals(FileSystemMain.admin)
				|| containsAllowedUser(removeUsr.getName())) {
			// Remove the file from the filelist of its parent folder
			parent.getFiles().remove(this);
			// Remove the file from the filelist of its owner
			owner.getFiles().remove(this);
		}
		return false;
	}

	// may not be given to students
	// returns the string representation of the file.
	@Override
	public String toString() {
		String retString = "";
		retString = name + "." + extension.name() + "\t" + owner.getName()
				+ "\t";
		for (Access preAccess : allowedUsers) {
			retString = retString + preAccess + " ";
		}
		retString = retString + "\t\"" + content + "\"";
		return retString;
	}

}
