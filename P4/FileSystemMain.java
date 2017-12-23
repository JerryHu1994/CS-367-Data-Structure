///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS               
// Title:            CS 367 Programming Assignment p4
// Files:            FileSystemMain.java
//					 SimpleFileSystem.java
// 					 SimpleFile.java
//					 SimpleFolder.java
//    				 User.java
//                   Access.java 
// Semester:         CS367 Spring 2015
//
// Author:           Jieru Hu
// Email:            jhu76@wisc.edu
// CS Login:         jieru
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001
// Pair Partner:     Jingxuan Lyu
// Email:            Jlyu3@wisc.edu
// CS Login:         jingxuan
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001
////////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the main class of the program. First, the class create a
 * SimpleFileSytem and load all information in the input file in to the
 * simplefilesystem including h users, folders, files and contents. Then the
 * class ask users to give command and does the correspouding input.
 * 
 * @author Jerry
 *
 */
public class FileSystemMain {

	private static Scanner scnr;
	/* The admin user of the simplefilesystem */
	public static User admin;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java FileSystemMain FileName");
			System.exit(0);
		}
		// Create the admin user for the system
		admin = new User("admin");
		ArrayList<User> users = new ArrayList<User>();
		// Create a working =System and load the information
		SimpleFileSystem workingSystem = loadfile(args[0], users);
		workingSystem.setCurrentUser("admin");
		boolean done = true;
		scnr = new Scanner(System.in);

		while (done) {
			System.out
					.print(workingSystem.getCurrUser().getName() + "@CS367$ ");
			String input = scnr.nextLine();
			String[] words = input.split(" ");
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			// Changes the user to admin and location to root folder
			case reset:
				if (words.length > 1) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "No Argument Needed");
				} else {
					workingSystem.reset();

					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "Reset done");
				}
				break;

			// Shows the present working directory for the user.
			case pwd:
				if (words.length > 1) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "No Argument Needed");
				} else {
					System.out.println(workingSystem.getPWD());
				}
				break;

			// Shows the folder and files accessible to user, contained in the
			// present working directory.
			case ls:
				if (words.length > 1) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "No Argument Needed");
				} else {

					workingSystem.printAll();

				}
				break;

			// Changes the current user to username
			case u:
				if (words.length != 2) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "One Argument Needed");
				} else {
					String username = words[1];
					for (int i = 0; i < users.size(); i++) {
						if (users.get(i).getName().equals(username)) {
							workingSystem.setCurrentUser(username);
							if (workingSystem.setCurrentUser(username) == false) {
								System.out.println(workingSystem.getCurrUser()
										.getName()
										+ "@CS367$ "
										+ "User <"
										+ username + "> does not exist");
							}
							break;
						}
					}

				}
				break;

			// Prints all the user's information.
			case uinfo:
				if (words.length > 1) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "No Argument Needed");
				} else {
					if (workingSystem.getCurrUser().equals(admin)) {
						workingSystem.printUsersInfo();
					} else {
						System.out.println(workingSystem.getCurrUser()
								.getName()
								+ "@CS367$ "
								+ "Insufficient privileges");
					}
				}
				break;

			// Open the folder based on the argument. The argument path can be
			// absolute or relative.
			case cd:
				if (words.length != 2) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "One Argument Needed");
				} else {
					if (workingSystem.moveLoc(words[1])) {

					} else {
						System.out.println(workingSystem.getCurrUser()
								.getName()
								+ "@CS367$ "
								+ "Invalid location passed");
					}

				}
				break;

			// Remove that file/folder in the in the present working directory.
			case rm:
				if (words.length != 2) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "One Argument Needed");
				} else {
					String rmname = words[1];
					if (workingSystem.containsFileFolder(rmname)) {
						if (workingSystem.remove(rmname)) {
							System.out.println(workingSystem.getCurrUser()
									.getName()
									+ "@CS367$ "
									+ rmname
									+ " removed");
						} else {
							System.out.println(workingSystem.getCurrUser()
									.getName()
									+ "@CS367$ "
									+ "Insufficient privilege");
						}
					} else {
						System.out.println(workingSystem.getCurrUser()
								.getName() + "@CS367$ " + "Invalid name");
					}
				}
				break;

			// Create a folder as immediate child of current location with name
			// = folderName and owner = current user.
			case mkdir:
				if (words.length != 2) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "One Argument Needed");
				} else {
					String newname = words[1];
					workingSystem.mkdir(newname);
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + newname + " added");
				}
				break;

			// Creates a new file as immediate child of current location with
			// name = fileName and content = filecontent.
			case mkfile:
				if (words.length != 2 && words.length != 3) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "One Argument Needed");
				} else {
					String name = words[1];
					if (containsExtension(name)) {

						if (words.length == 2) {
							String fileContent = "";
							workingSystem.addFile(name, fileContent);
						}
						if (words.length == 3) {
							String fileContent = words[2];
							workingSystem.addFile(name, fileContent);
						}
						System.out.println(workingSystem.getCurrUser()
								.getName() + "@CS367$ " + name + " added");
					} else {
						System.out.println(workingSystem.getCurrUser()
								.getName() + "@CS367$ " + "Invalid filename");
					}
				}
				break;

			// Give access of a particular immediate child file/folder of
			// current location identified by fname to the user identified by
			// username.
			case sh:
				if (words.length != 4) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "Three Arguments Needed");
				} else {
					String fname = words[1];
					String username = words[2];
					char accessType = words[3].charAt(0);
					if (accessType != 'r' && accessType != 'w') {
						System.out.println(workingSystem.getCurrUser()
								.getName()
								+ "@CS367$ "
								+ "Invalid permission type");
					} else {
						if (workingSystem.containsUser(username) == null) {
							System.out.println(workingSystem.getCurrUser()
									.getName() + "@CS367$ " + "Invalid user");
						} else {
							if (workingSystem.containsFileFolder(fname) == false) {
								System.out.println(workingSystem.getCurrUser()
										.getName()
										+ "@CS367$ "
										+ "Invalid file/folder name");
							} else {
								if (workingSystem.addUser(fname, username,
										accessType) == false) {
									System.out.println(workingSystem
											.getCurrUser().getName()
											+ "@CS367$ "
											+ "Insufficient privilege");
								} else {
									System.out.println(workingSystem
											.getCurrUser().getName()
											+ "@CS367$ " + "Privilege granted");
								}
							}
						}
					}
				}
				break;

			// exit
			case x:
				done = false;
				break;

			default:
				if (!input.equals("")) {
					System.out.println(workingSystem.getCurrUser().getName()
							+ "@CS367$ " + "Invalid command");
				}
			}
		}

	}

	/**
	 * convert the input string into the Cmd
	 * 
	 * @param strCmd
	 *            string of the command
	 * @return correspounding Cmd
	 */
	private static Cmd stringToCmd(String strCmd) {
		if (strCmd == null) {
			throw new IllegalArgumentException();
		}
		try {
			return Cmd.valueOf(strCmd.toLowerCase().trim());
		} catch (IllegalArgumentException e) {
			return Cmd.uk;
		}
	}

	// Cmd of different types on input
	private enum Cmd {
		reset, pwd, ls, u, uinfo, cd, rm, mkdir, mkfile, sh, x, uk
	}

	/**
	 * load information into the simplefilesystem throws
	 * IllegalArgumentException when the txtname or the users is null
	 * 
	 * @param txtname
	 *            the string represents the name of the load txt file
	 * @param users
	 *            empty arraylist which stores the list of users
	 * @return simplefilesystem when the load is done
	 */
	public static SimpleFileSystem loadfile(String txtname,
			ArrayList<User> users) {
		if (txtname == null) {
			throw new IllegalArgumentException();
		}
		File newfile = new File(txtname);
		try {
			scnr = new Scanner(newfile);
		} catch (FileNotFoundException e) {

		}
		String rootname = scnr.nextLine();
		String path = "";
		// create the root folder;
		SimpleFolder rootfolder = new SimpleFolder(rootname, path, null, admin);
		String username = scnr.nextLine();
		String[] usernamelist = username.split(", ");
		// add the user admin first and add users one by one
		users.add(admin);
		for (int i = 0; i < usernamelist.length; i++) {
			users.add(new User(usernamelist[i]));
		}
		// create a new simplefilesystem base on the root folder and users
		SimpleFileSystem sfs = new SimpleFileSystem(rootfolder, users);

		while (scnr.hasNextLine()) {
			String newaddress = scnr.nextLine();
			// split the address argument
			String[] newaddresslist = newaddress.split("/");
			// check for each element in the address arguement
			for (int i = 1; i < newaddresslist.length; i++) {
				// if the currLoc does not contain folder or file, make a new
				// one, else move to that folder
				if (sfs.containsFileFolder(newaddresslist[i]) == false) {
					// check whether the argument is a file or a folder
					if (containsExtension(newaddresslist[i]) == false) {
						// make the new folder
						sfs.mkdir(newaddresslist[i]);
						// move to the new folder
						sfs.moveLoc(newaddresslist[i]);
						// add the access of users into the new folder or file
						// made
						for (int j = 1; j < users.size(); j++) {
							sfs.currLoc.addAllowedUser(new Access(users.get(j),
									'r'));
						}
					} else {
						String[] lastlist = newaddresslist[i].split("\\s+");
						// make new file
						sfs.addFile(lastlist[0], lastlist[1]);
						// add the access of users into the new folder or file
						// made
						for (int z = 0; z < users.size(); z++) {
							sfs.currLoc.getFile(lastlist[0]).addAllowedUser(
									new Access(users.get(z), 'r'));
						}
					}
				} else {
					sfs.moveLoc(newaddresslist[i]);
				}
			}
			// reset the system when each line of the input is loaded
			sfs.reset();
		}
		return sfs;

	}

	/**
	 * check if the input string contains the extension
	 * 
	 * @param s
	 *            the input string
	 * @return whether the the input string contains the extension
	 */
	public static boolean containsExtension(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		String txt = Extension.txt.toString();
		String doc = Extension.doc.toString();
		String rtf = Extension.rtf.toString();
		if (s.contains(txt) || s.contains(doc) || s.contains(rtf)) {
			return true;
		} else {
			return false;
		}
	}

}
