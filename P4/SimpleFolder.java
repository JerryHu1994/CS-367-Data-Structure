import java.util.ArrayList;

/**
 * Rrepresents a folder which could store folders and files.
 * 
 * @author Jerry
 *
 */
public class SimpleFolder {
	/* The string which represents the name of the simplefolder */
	private String name;
	/* The string represents the absolute path of the folder */
	private String path;
	/* The simplefolder who is the parent of this folder */
	private SimpleFolder parent;
	/* The user who owns the simplefolder */
	private User owner;
	/* The arraylist stores folders which are owned by this folder */
	private ArrayList<SimpleFolder> subFolders;
	/* The arraylist stores files which are owned by this folder */
	private ArrayList<SimpleFile> files;
	/* The arraylist stores accesses of users */
	private ArrayList<Access> allowedUsers;

	/**
	 * contructs a simplefolder
	 * 
	 * @param name
	 *            the name of the folder
	 * @param path
	 *            the path of the folder
	 * @param parent
	 *            the parent folder of this folder
	 * @param owner
	 *            the user who owns the folder
	 */
	public SimpleFolder(String name, String path, SimpleFolder parent,
			User owner) {
		if (name == null || path == null || owner == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.path = path;
		this.parent = parent;
		this.owner = owner;
		this.subFolders = new ArrayList<SimpleFolder>();
		this.files = new ArrayList<SimpleFile>();
		this.allowedUsers = new ArrayList<Access>();
	}

	/**
	 * checks if user - "name" is allowed to access this folder or not. If yes,
	 * return true, otherwise false. throw IllegalArugmentException if the name
	 * is null
	 * 
	 * @param name
	 *            the name of the user to check
	 * @return whether the access of the user is found
	 */
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

	/**
	 * checks if this folder contains the child folder identified by 'name'. If
	 * it does contain then it returns the folder otherwise returns null. throw
	 * IllegalArgumentException if name is null
	 * 
	 * @param name
	 *            the name of the subfolder to get
	 * @return the subfolder founded;null if nothing is found
	 */
	public SimpleFolder getSubFolder(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < subFolders.size(); i++) {
			if (subFolders.get(i).getName().equals(name)) {
				return subFolders.get(i);
			}
		}
		return null;
	}

	/**
	 * checks if this folder contains the child file identified by "name". If it
	 * does contain, return File otherwise null. throw IllegalArgumentException
	 * if the fname is null
	 * 
	 * @param fname
	 *            the name of the file to get
	 * @return the file founded; null if nothing is found
	 */
	public SimpleFile getFile(String fname) {
		if (fname == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).getName().equals(fname.split("\\.")[0])) {
				return files.get(i);
			}
		}

		return null;
	}

	// returns the owner of the folder.
	public User getOwner() {
		return owner;
	}

	// returns the name of the folder.
	public String getName() {
		return name;
	}

	// returns the path of this folder.
	public String getPath() {
		return path;
	}

	// returns the Parent folder of this folder.
	public SimpleFolder getParent() {
		// TODO
		return parent;
	}

	// returns the list of all folders contained in this folder.
	public ArrayList<SimpleFolder> getSubFolders() {
		return subFolders;
	}

	/**
	 * adds a folder into this folder. throw IllegalArugmentException when the
	 * subfolder is null
	 * 
	 * @param subFolder
	 *            the subfolder to add
	 */
	public void addSubFolder(SimpleFolder subFolder) {
		if (subFolder == null) {
			throw new IllegalArgumentException();
		}
		subFolder.addAllowedUser(new Access(subFolder.getOwner(), 'w'));
		subFolder.getOwner().addFolder(subFolder);
		subFolders.add(subFolder);

	}

	/**
	 * adds a folder into this folder. throw IllegalArgumentException if the
	 * name, parent and owner in the argument is null;
	 * 
	 * @param name
	 *            name of the new folder
	 * @param parent
	 *            parent folder of the new folder
	 * @param owner
	 *            the user who own the folder
	 */
	public void addSubFolder(String name, SimpleFolder parent, User owner) {
		if (name == null || parent == null || owner == null) {
			throw new IllegalArgumentException();
		}
		String newpath = "";
		if (parent.getPath().equals("")) {
			newpath = parent.getName();
		} else {
			newpath = parent.getPath() + "/" + parent.getName();
		}
		SimpleFolder newfolder = new SimpleFolder(name, newpath, parent, owner);
		newfolder.addAllowedUser(new Access(newfolder.getOwner(), 'w'));
		owner.addFolder(newfolder);
		subFolders.add(newfolder);
	}

	// returns the list of files contained in this folder.
	public ArrayList<SimpleFile> getFiles() {
		return files;
	}

	/**
	 * add the file to the list of files contained in this folder. throw
	 * IllegalArgumentException if the file is null
	 * 
	 * @param file
	 *            the file to add
	 */
	public void addFile(SimpleFile file) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		file.addAllowedUser(new Access(file.getOwner(), 'w'));
		if (!file.getOwner().equals(FileSystemMain.admin)) {
			file.addAllowedUser(new Access(file.getOwner(), 'w'));
		}
		file.getOwner().addFile(file);
		files.add(file);
	}

	// returns the list of allowed user to this folder.
	public ArrayList<Access> getAllowedUsers() {
		return allowedUsers;
	}

	// adds another user to the list of allowed user of this folder.
	public void addAllowedUser(Access allowedUser) {
		if (allowedUser == null) {
			throw new IllegalArgumentException();
		}
		allowedUsers.add(allowedUser);
	}

	// adds a list of allowed user to this folder.
	public void addAllowedUser(ArrayList<Access> allowedUser) {
		if (allowedUser == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < allowedUser.size(); i++) {
			allowedUsers.add(allowedUsers.get(i));
		}
	}

	/**
	 * If the user is owner of this folder or the user is admin or the user has
	 * 'w' privilege, then delete this folder along with all its content.
	 * 
	 * @param removeUsr
	 *            the user to remove
	 * @return whether the removal succeeds
	 */
	public boolean removeFolder(User removeUsr) {
		if (removeUsr == null) {
			throw new IllegalArgumentException();
		}
		// get the access type
		Access currAccess = null;
		for (int i = 0; i < this.getAllowedUsers().size(); i++) {
			if (this.getAllowedUsers().get(i).getUser().equals(removeUsr)) {
				currAccess = this.getAllowedUsers().get(i);
			}
		}

		// check the condition
		if (removeUsr.equals(owner) || removeUsr.equals(FileSystemMain.admin)
				|| currAccess.getAccessType() == 'w') {
			SimpleFolder curr = this;

			// base case: the current folder does not have any subfolders
			if (curr.getSubFolders().isEmpty()) {

				for (int j = 0; j < curr.getFiles().size(); j++) {
					curr.getFiles().get(j).removeFile(removeUsr);
				}
				curr.owner.removeFolder(curr);
				curr.parent.getSubFolders().remove(curr);

				return true;
			}

			// recursive case;
			for (int i = 0; i < curr.getSubFolders().size(); i++) {
				curr.getSubFolders().get(i).removeFolder(removeUsr);
				i--;
			}
			curr.getOwner().removeFolder(curr);
			curr.getParent().getSubFolders().remove(curr);

			return true;
		} else {
			return false;
		}
	}

	// returns the string representation of the Folder object.
	@Override
	public String toString() {
		String retString = "";
		retString = path + "/" + name + "\t" + owner.getName() + "\t";
		for (Access preAccess : allowedUsers) {
			retString = retString + preAccess + " ";
		}

		retString = retString + "\nFILES:\n";

		for (int i = 0; i < files.size(); i++) {
			retString = retString + files.get(i);
			if (i != files.size() - 1)
				retString = retString + "\n";

		}
		return retString;
	}

}
