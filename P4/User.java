import java.util.ArrayList;

/**
 * Represents a user who owns a list of files and folders
 * 
 * @author Jerry
 *
 */
public class User {

	/* the name of the user */
	private String name; // name of the user.
	/* the arraylist of files owned by the user */
	private ArrayList<SimpleFile> files;// list of files owned/created by user
	/* the arraylist of folders owned by the user */
	private ArrayList<SimpleFolder> folders;// list of folder owned by user.

	/**
	 * contructs a user object
	 * 
	 * @param name
	 *            the string represents the name of the user
	 */
	public User(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.files = new ArrayList<SimpleFile>();
		this.folders = new ArrayList<SimpleFolder>();
	}

	@Override
	public boolean equals(Object obj) {
		User comUser = (User) obj;
		return this.name.equals(comUser.getName());
	}

	// returns the name of the user.
	public String getName() {
		return name;
	}

	// returns the list of files owned by the user.
	public ArrayList<SimpleFile> getFiles() {

		return files;
	}

	// adds the file to the list of files owned by the user.
	public void addFile(SimpleFile newfile) {
		if (newfile == null) {
			throw new IllegalArgumentException();
		}
		files.add(newfile);
	}

	/**
	 * removes the file from the list of owned files of the user. throw
	 * IllegalArgumentException when the rmFile is null
	 * 
	 * @param rmFile
	 *            the file to remove
	 * @return whether the removal is a success
	 */
	public boolean removeFile(SimpleFile rmFile) {
		if (rmFile == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).equals(rmFile)) {
				files.remove(i);
				return true;
			}
		}
		return false;
	}

	// returns the list of folders owned by the user.
	public ArrayList<SimpleFolder> getFolders() {
		return folders;
	}

	// adds the folder to the list of folders owned by the user.
	public void addFolder(SimpleFolder newFolder) {
		if (newFolder == null) {
			throw new IllegalArgumentException();
		}
		folders.add(newFolder);
	}

	/**
	 * removes the folder from the list of folders owned by the user. throw
	 * IllegalArgumentException when the rmFolder is null
	 * 
	 * @param rmFolder
	 *            the folder to remove
	 * @return whether the removal is a success
	 */
	public boolean removeFolder(SimpleFolder rmFolder) {
		if (rmFolder == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < folders.size(); i++) {
			if (folders.get(i).equals(rmFolder)) {
				folders.remove(i);
				return true;
			}
		}
		return false;
	}

	// returns the string representation of the user object.
	@Override
	public String toString() {
		String retType = name + "\n";
		retType = retType + "Folders owned :\n";
		for (SimpleFolder preFolder : folders) {
			retType = retType + "\t" + preFolder.getPath() + "/"
					+ preFolder.getName() + "\n";
		}
		retType = retType + "\nFiles owned :\n";
		for (SimpleFile preFile : files) {
			retType = retType + "\t" + preFile.getPath() + "/"
					+ preFile.getName() + "."
					+ preFile.getExtension().toString() + "\n";
		}
		return retType;
	}

}
