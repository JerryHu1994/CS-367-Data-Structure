import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a database stores the root of the tree, list of users, the current
 * folder pointer and the current user logined in. Users could do different
 * operations on the system.
 * 
 * @author Jerry
 *
 */
public class SimpleFileSystem {

	/* The root simplefolder of the tree */
	SimpleFolder root;
	/* The arraylist which stores the list of users */
	ArrayList<User> users;
	/* The pointer which points to the current simplefolder */
	SimpleFolder currLoc;
	/* The user which represents the user login in */
	User currUser;

	// constructor
	/**
	 * Contructs a simplefilesystem database
	 * 
	 * @param _root
	 *            the root of the tree
	 * @param _users
	 *            the arraylist of the users
	 */
	public SimpleFileSystem(SimpleFolder _root, ArrayList<User> _users) {
		if (_root == null || _users == null) {
			throw new IllegalArgumentException();
		}
		this.root = _root;
		this.users = _users;
		currLoc = root;
		currUser = FileSystemMain.admin;
	}

	// resets everything to default values.
	// i.e., currUser to admin.
	// and currLoc = root.
	// It does not delete anything. It just reset the pointers to original
	// values.
	public void reset() {
		currLoc = root;
		currUser = FileSystemMain.admin;
	}

	// gets currUser.
	public User getCurrUser() {
		return currUser;
	}

	/**
	 * sets the current user to the user with name passed in the argument.
	 * throws IllegalArgumentException when the name in the argument is null
	 * 
	 * @param name
	 *            the name of the user to set
	 * @return whether the set succeeds
	 */
	public boolean setCurrentUser(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				currUser = users.get(i);
				currLoc = root;
				return true;
			}
		}
		return false;

	}

	/**
	 * checks if the user is contained in the existing users list or not. throws
	 * IllegalArgumentException if the name in the argument is true
	 * 
	 * @param name
	 *            the name in the arguemnt
	 * @return the existed user, null if the user does not exist
	 */
	public User containsUser(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				return users.get(i);
			}
		}
		return null;
	}

	/**
	 * checks whether curr location contains any file/folder with name name
	 * =fname throw IllegalArgumentException if the fname in the argument is
	 * null
	 * 
	 * @param fname
	 *            the name to check
	 * @return whether the simplefilesystem contains the file or the folder
	 */
	public boolean containsFileFolder(String fname) {
		if (fname == null) {
			throw new IllegalArgumentException();
		}

		if ((currLoc.getFile(fname) != null)
				|| (currLoc.getSubFolder(fname) != null)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * changes the current location. If successful returns true, false
	 * otherwise. throw IllegalArgumentException is the argument is null
	 * 
	 * @param argument
	 *            the argument of the path change
	 * @return whether the moveloc succeeds
	 */
	public boolean moveLoc(String argument) {

		String firstChar = argument.substring(0, 1);
		if (firstChar.equals("/")) {
			// if the argument starts with "/", it is an absolute path
			String[] addresslist = argument.split("/");
			SimpleFolder temp = currLoc;
			currLoc = root;

			if (addresslist.length < 2 || !addresslist[0].equals("")
					|| !addresslist[1].equals(root.getName())) {
				currLoc = temp;
				return false;
			}
			for (int i = 2; i < addresslist.length; i++) {

				if (currLoc.getSubFolder(addresslist[i]) != null) {
					currLoc = currLoc.getSubFolder(addresslist[i]);
				} else {
					currLoc = temp;
					return false;
				}

			}
			return true;
		} else {
			String firsttwo = argument.substring(0, 2);
			// if the argument starts with "..", gets relative up
			if (firsttwo.equals("..")) {
				String[] pathlist1 = argument.split("/");
				String[] pathlist2 = argument.split("\\..");
				if (pathlist1.length != pathlist2.length - 1) {

					return false;
				}
				SimpleFolder temp = currLoc;
				for (int i = 0; i < pathlist1.length; i++) {
					if (pathlist1[i].equals("..")) {
						if (currLoc.getParent() != null) {
							currLoc = currLoc.getParent();
						} else {
							currLoc = temp;
							return false;
						}
					} else {

						return false;
					}
				}
				return true;
			} else {
				// in other cases, gets relative down
				String[] pathlist = argument.split("/");
				for (int i = 0; i < pathlist.length; i++) {

					if (currLoc.getSubFolder(pathlist[i]) != null) {

						currLoc = currLoc.getSubFolder(pathlist[i]);
					} else {

						return false;
					}
				}
				return true;
			}

		}

	}

	// returns the currentlocation.path + currentlocation.name.
	public String getPWD() {
		return ((currLoc.getPath().isEmpty() ? "" : (currLoc.getPath() + "/")) + currLoc
				.getName());

	}

	//
	/**
	 * deletes the folder/file identified by the 'name' if the argument name is
	 * null, throw IllegalArgumentException
	 * 
	 * @param name
	 *            the name of the file or folder to remove
	 * @return whether the removal succeeds
	 */
	public boolean remove(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (currLoc.getSubFolder(name) != null) {
			return currLoc.getSubFolder(name).removeFolder(currUser);

		}
		if (currLoc.getFile(name) != null) {
			return currLoc.getFile(name).removeFile(currUser);

		}
		return false;
	}

	/**
	 * Gives the access 'permission' of the file/folder fname to the user if the
	 * current user is the owner of the fname. If succeed, return true,
	 * otherwise false. throw IllegalArgumentException if fname, username is
	 * null
	 * 
	 * @param fname
	 *            the name of the file or folder
	 * @param username
	 *            the name of the target user
	 * @param permission
	 *            the type of permission
	 * @return whether the addUser is success
	 */
	public boolean addUser(String fname, String username, char permission) {
		if (fname == null || username == null) {
			throw new IllegalArgumentException();
		}
		User owner = null;
		if (currLoc.getFile(fname) == null) {
			owner = currLoc.getSubFolder(fname).getOwner();
		} else {
			owner = currLoc.getFile(fname).getOwner();
		}

		if (currUser.equals(owner)) {
			if (currLoc.getFile(fname) != null) {
				currLoc.getFile(fname).getAllowedUsers()
						.add(new Access(containsUser(username), permission));
				return true;
			} else {
				SimpleFolder curr = currLoc.getSubFolder(fname);
				curr.addAllowedUser(new Access(containsUser(username),
						permission));
				for (int i = 0; i < curr.getFiles().size(); i++) {
					curr.getFiles()
							.get(i)
							.addAllowedUser(
									new Access(containsUser(username),
											permission));
				}

				return true;
			}
		} else {

			return false;
		}

	}

	/**
	 * displays the user info in the specified format.
	 * 
	 * @return whether the printUserInfo succeeds
	 */
	public boolean printUsersInfo() {
		if (currUser.equals(currLoc.getOwner())) {
			for (int i = 0; i < users.size(); i++) {
				System.out.println(users.get(i).toString());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * makes a new folder under the current folder with owner = current user.
	 * throw IllegalArgumentException if the name is null
	 * 
	 * @param name
	 *            name of the new folder
	 */
	public void mkdir(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		currLoc.addSubFolder(name, currLoc, currUser);

	}

	/**
	 * makes a new file under the current folder with owner = current user.
	 * throw IllegalArgumentException if the filename or the fileContent is null
	 * 
	 * @param filename
	 * @param fileContent
	 */
	public void addFile(String filename, String fileContent) {
		if (filename == null || fileContent == null) {
			throw new IllegalArgumentException();
		}
		String[] filenamelist = filename.split("\\.");
		String name = filenamelist[0];
		Extension exten = Extension.valueOf(filenamelist[1]);
		String newpath = this.getPWD();
		currLoc.addFile(new SimpleFile(name, exten, newpath, fileContent,
				currLoc, currUser));
	}

	// prints all the folders and files under the current user for which user
	// has access.
	public void printAll() {

		for (SimpleFile f : currLoc.getFiles()) {
			if (f.containsAllowedUser(currUser.getName())) {
				System.out.print(f.getName() + "."
						+ f.getExtension().toString() + " : "
						+ f.getOwner().getName() + " : ");
				for (int i = 0; i < f.getAllowedUsers().size(); i++) {
					Access a = f.getAllowedUsers().get(i);
					System.out.print("(" + a.getUser().getName() + ","
							+ a.getAccessType() + ")");
					if (i < f.getAllowedUsers().size() - 1) {
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
		for (SimpleFolder f : currLoc.getSubFolders()) {
			if (f.containsAllowedUser(currUser.getName())) {
				System.out.print(f.getName() + " : " + f.getOwner().getName()
						+ " : ");
				for (int i = 0; i < f.getAllowedUsers().size(); i++) {
					Access a = f.getAllowedUsers().get(i);
					System.out.print("(" + a.getUser().getName() + ","
							+ a.getAccessType() + ")");
					if (i < f.getAllowedUsers().size() - 1) {
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
	}

}
