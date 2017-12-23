///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS               
// Title:            CS 367 Programming Assignment p3
// Files:            VersionControlApp.java
//					 SimpleQueue.java
// 					 SimpleStack.java
//					 User.java
//    				 Repo.java
//                   ChangeSet.java 
// Semester:         CS367 Spring 2015
//
// Author:           Jieru Hu
// Email:            jhu76@wisc.edu
// CS Login:         jieru
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001
////////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.util.Scanner;

/**
 * Version control application. Implements the command line utility for Version
 * control.
 * 
 * @author
 *
 */
public class VersionControlApp {

	/* Scanner object on input stream. */
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * An enumeration of all possible commands for Version control system.
	 */
	private enum Cmd {
		AU, DU, LI, QU, AR, DR, OR, LR, LO, SU, CO, CI, RC, VH, RE, LD, AD, ED, DD, VD, HE, UN
	}

	/**
	 * Displays the main menu help.
	 */
	private static void displayMainMenu() {
		System.out.println("\t Main Menu Help \n"
				+ "====================================\n"
				+ "au <username> : Registers as a new user \n"
				+ "du <username> : De-registers a existing user \n"
				+ "li <username> : To login \n" + "qu : To exit \n"
				+ "====================================\n");
	}

	/**
	 * Displays the user menu help.
	 */
	private static void displayUserMenu() {
		System.out.println("\t User Menu Help \n"
				+ "====================================\n"
				+ "ar <reponame> : To add a new repo \n"
				+ "dr <reponame> : To delete a repo \n"
				+ "or <reponame> : To open repo \n" + "lr : To list repo \n"
				+ "lo : To logout \n"
				+ "====================================\n");
	}

	/**
	 * Displays the repo menu help.
	 */
	private static void displayRepoMenu() {
		System.out.println("\t Repo Menu Help \n"
				+ "====================================\n"
				+ "su <username> : To subcribe users to repo \n"
				+ "ci: To check in changes \n" + "co: To check out changes \n"
				+ "rc: To review change \n" + "vh: To get revision history \n"
				+ "re: To revert to previous version \n"
				+ "ld : To list documents \n" + "ed <docname>: To edit doc \n"
				+ "ad <docname>: To add doc \n"
				+ "dd <docname>: To delete doc \n"
				+ "vd <docname>: To view doc \n" + "qu : To quit \n"
				+ "====================================\n");
	}

	/**
	 * Displays the user prompt for command.
	 * 
	 * @param prompt
	 *            The prompt to be displayed.
	 * @return The user entered command (Max: 2 words).
	 */
	private static String[] prompt(String prompt) {
		System.out.print(prompt);
		String line = scnr.nextLine();
		String[] words = line.trim().split(" ", 2);
		return words;

	}

	/**
	 * Displays the prompt for file content.
	 * 
	 * @param prompt
	 *            The prompt to be displayed.
	 * @return The user entered content.
	 */
	private static String promptFileContent(String prompt) {
		System.out.println(prompt);
		String line = null;
		String content = "";
		while (!(line = scnr.nextLine()).equals("q")) {
			content += line + "\n";
		}
		return content;
	}

	/**
	 * Validates if the input has exactly 2 elements.
	 * 
	 * @param input
	 *            The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput2(String[] input) {
		if (input.length != 2) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input has exactly 1 element.
	 * 
	 * @param input
	 *            The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput1(String[] input) {
		if (input.length != 1) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Returns the Cmd equivalent for a string command.
	 * 
	 * @param strCmd
	 *            The string command.
	 * @return The Cmd equivalent.
	 */
	private static Cmd stringToCmd(String strCmd) {
		try {
			return Cmd.valueOf(strCmd.toUpperCase().trim());
		} catch (IllegalArgumentException e) {
			return Cmd.UN;
		}
	}

	/**
	 * Handles add user. Checks if a user with name "username" already exists;
	 * if exists the user is not registered.
	 * 
	 * @param username
	 *            The user name.
	 * @return USER_ALREADY_EXISTS if the user already exists, SUCCESS
	 *         otherwise.
	 */
	private static ErrorType handleAddUser(String username) {
		if (VersionControlDb.addUser(username) != null) {
			return ErrorType.SUCCESS;
		} else {
			return ErrorType.USERNAME_ALREADY_EXISTS;
		}
	}

	/**
	 * Handles delete user. Checks if a user with name "username" exists; if
	 * does not exist nothing is done.
	 * 
	 * @param username
	 *            The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleDelUser(String username) {
		User user = VersionControlDb.findUser(username);
		if (user == null) {
			return ErrorType.USER_NOT_FOUND;
		} else {
			VersionControlDb.delUser(user);
			return ErrorType.SUCCESS;
		}
	}

	/**
	 * Handles a user login. Checks if a user with name "username" exists; if
	 * does not exist nothing is done; else the user is taken to the user menu.
	 * 
	 * @param username
	 *            The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 * @throws EmptyQueueException
	 * @throws EmptyStackException
	 */
	private static ErrorType handleLogin(String username)
			throws EmptyQueueException, EmptyStackException {
		User currUser = VersionControlDb.findUser(username);
		if (currUser != null) {
			System.out.println(ErrorType.SUCCESS);
			processUserMenu(currUser);
			return ErrorType.SUCCESS;
		} else {
			return ErrorType.USER_NOT_FOUND;
		}
	}

	/**
	 * Processes the main menu commands.
	 * 
	 * @throws EmptyQueueException
	 * @throws EmptyStackException
	 * 
	 */
	public static void processMainMenu() throws EmptyQueueException,
			EmptyStackException {

		String mainPrompt = "[anon@root]: ";
		boolean execute = true;

		while (execute) {
			String[] words = prompt(mainPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AU:
				if (validateInput2(words)) {
					System.out.println(handleAddUser(words[1].trim()));
				}
				break;
			case DU:
				if (validateInput2(words)) {
					System.out.println(handleDelUser(words[1].trim()));
				}
				break;
			case LI:
				if (validateInput2(words)) {
					System.out.println(handleLogin(words[1].trim()));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayMainMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Processes the user menu commands for a logged in user.
	 * 
	 * @param logInUser
	 *            The logged in user.
	 * @throws EmptyQueueException
	 * @throws EmptyStackException
	 * @throws IllegalArgumentException
	 *             in case any argument is null.
	 */
	public static void processUserMenu(User logInUser)
			throws EmptyQueueException, EmptyStackException {

		if (logInUser == null) {
			throw new IllegalArgumentException();
		}

		String userPrompt = "[" + logInUser.getName() + "@root" + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(userPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AR:
				if (validateInput2(words)) {
					String newrepoName = words[1];
					if (VersionControlDb.findRepo(newrepoName) == null) {
						// Add a new repo to the database;
						VersionControlDb.addRepo(newrepoName, logInUser);
						// Subscribe the log in user to the new repo;
						logInUser.subscribeRepo(newrepoName);
						System.out.println(ErrorType.SUCCESS);
					} else {
						System.out.println(ErrorType.REPONAME_ALREADY_EXISTS);
					}
				}
				break;
			case DR:
				if (validateInput2(words)) {
					// Find the correspounding repo to delete;
					Repo repotoDelete = VersionControlDb.findRepo(words[1]);
					if (repotoDelete == null) {
						System.out.println(ErrorType.REPO_NOT_FOUND);
					} else {
						if (logInUser.equals(repotoDelete.getAdmin())) {
							VersionControlDb.delRepo(repotoDelete);
							System.out.println(ErrorType.SUCCESS);
						} else {
							System.out.println(ErrorType.ACCESS_DENIED);
						}
					}
				}
				break;
			case LR:
				if (validateInput1(words)) {
					System.out.println(logInUser.toString());
				}
				break;
			case OR:
				if (validateInput2(words)) {
					if (!(VersionControlDb.findRepo(words[1]) == null)) {
						// Check if the loginuser is subscribed to the repo;
						if (logInUser.isSubRepo(words[1])) {
							// If there is no working copy for the repo, check
							// out a new one;
							if (logInUser.getWorkingCopy(words[1]) == null) {
								logInUser.checkOut(words[1]);
							}
							System.out.println(ErrorType.SUCCESS);
							processRepoMenu(logInUser, words[1]);
							System.out.println(ErrorType.SUCCESS);
						} else {
							System.out.println(ErrorType.REPO_NOT_SUBSCRIBED);
						}
					} else {
						System.out.println(ErrorType.REPO_NOT_FOUND);
					}
				}
				break;
			case LO:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayUserMenu();
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Process the repo menu commands for a logged in user and current working
	 * repository.
	 * 
	 * @param logInUser
	 *            The logged in user.
	 * @param currRepo
	 *            The current working repo.
	 * @throws EmptyQueueException
	 * @throws EmptyStackException
	 * @throws IllegalArgumentException
	 *             in case any argument is null.
	 */
	public static void processRepoMenu(User logInUser, String currRepo)
			throws EmptyQueueException, EmptyStackException {

		if (logInUser == null || currRepo == null) {
			throw new IllegalArgumentException();
		}

		String repoPrompt = "[" + logInUser.getName() + "@" + currRepo + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(repoPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case SU:
				if (validateInput2(words)) {
					Repo curr = VersionControlDb.findRepo(currRepo);
					// Check if the loginuser is the admin;
					if (logInUser.equals(curr.getAdmin())) {
						// If the user is not subscirbed, subsribe the user;
						if (VersionControlDb.findUser(words[1]) != null) {
							VersionControlDb.findUser(words[1]).subscribeRepo(
									currRepo);
							System.out.println(ErrorType.SUCCESS);
						} else {
							System.out.println(ErrorType.USER_NOT_FOUND);
						}
					} else {
						System.out.println(ErrorType.ACCESS_DENIED);
					}
				}
				break;
			case LD:
				// Print all documents in the local working copy;
				if (validateInput1(words)) {
					System.out.println(logInUser.getWorkingCopy(currRepo)
							.toString());

				}
				break;
			case ED:
				// Edits a existing document;
				if (validateInput2(words)) {
					RepoCopy curr = logInUser.getWorkingCopy(currRepo);
					if (curr.getDoc(words[1]) != null) {
						String prompt = "Enter the file content and press q to quit:";
						// Get the input from the user;
						String fileContent = promptFileContent(prompt);
						// Find the old document;
						Document oldDoc = curr.getDoc(words[1]);
						// //Set the content of the old docuemnt to the new one;
						oldDoc.setContent(fileContent);
						// Add the this change to the pendingCheckIns;
						logInUser.addToPendingCheckIn(oldDoc, Change.Type.EDIT,
								currRepo);
						System.out.println(ErrorType.SUCCESS);
					} else {
						System.out.println(ErrorType.DOC_NOT_FOUND);
					}
				}
				break;
			case AD:
				if (validateInput2(words)) {
					RepoCopy curr = logInUser.getWorkingCopy(currRepo);
					if (curr.getDoc(words[1]) != null) {
						System.out.println(ErrorType.DOCNAME_ALREADY_EXISTS);
					} else {
						String prompt = "Enter the file content and press q to quit:";
						// Get the input from the user;
						String fileContent = promptFileContent(prompt);
						// Construct a new document using the input content;
						Document newDoc = new Document(words[1], fileContent,
								currRepo);
						// Add the document;
						curr.addDoc(newDoc);
						// Put the change into the pendingCheckIns;
						logInUser.addToPendingCheckIn(newDoc, Change.Type.ADD,
								currRepo);

						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case DD:
				if (validateInput2(words)) {
					RepoCopy curr = logInUser.getWorkingCopy(currRepo);
					if (curr.getDoc(words[1]) == null) {
						System.out.println(ErrorType.DOC_NOT_FOUND);
					} else {
						// Find the document to delete;
						Document oldDoc = curr.getDoc(words[1]);
						// Remove the document;
						curr.delDoc(oldDoc);
						// Put the change into the pendingCheckIns;
						logInUser.addToPendingCheckIn(oldDoc, Change.Type.DEL,
								currRepo);
						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case VD:
				// Display a existing document;
				if (validateInput2(words)) {
					RepoCopy curr = logInUser.getWorkingCopy(currRepo);
					if (curr.getDoc(words[1]) == null) {
						System.out.println(ErrorType.DOC_NOT_FOUND);
					} else {
						System.out.println(curr.getDoc(words[1]).toString());
					}
				}
				break;
			case CI:
				// Check the pending changes in the pendingCheckIns into repo
				// for the admin approval;
				if (validateInput1(words)) {

					System.out.println(logInUser.checkIn(currRepo));

				}
				break;
			case CO:
				// Check out the latest version of the repo and put it into the
				// working copy;
				if (validateInput1(words)) {
					logInUser.checkOut(currRepo);
					System.out.println(ErrorType.SUCCESS);
				}
				break;
			case RC:
				// Ask the admin to review the checkins;
				if (validateInput1(words)) {
					Repo curr = VersionControlDb.findRepo(currRepo);
					if (curr.getCheckInCount() == 0) {
						System.out.println(ErrorType.NO_PENDING_CHECKINS);
					} else {
						if (!logInUser.equals(VersionControlDb.findRepo(
								currRepo).getAdmin())) {
							System.out.println(ErrorType.ACCESS_DENIED);
						} else {
							// Print the changeSet one by one, queue each
							// changeSet back after the print;
							for (int i = 0; i < curr.getCheckInCount(); i++) {
								ChangeSet currSet = curr
										.getNextCheckIn(logInUser);
								System.out.println(currSet.toString());
								curr.queueCheckIn(currSet);
							}
							System.out
									.print("Approve changes? Press y to accept: ");
							if (scnr.next().equals("y")) {
								scnr.nextLine();
								// Approve the changeSet one by one;
								for (int i = 0; i < curr.getCheckInCount(); i++) {
									curr.approveCheckIn(logInUser,
											curr.getNextCheckIn(logInUser));
								}
								System.out.println(ErrorType.SUCCESS);
							} else {

							}

						}
					}
				}
				break;
			case VH:
				// Display the history of the version;
				if (validateInput1(words)) {
					System.out.println(VersionControlDb.findRepo(currRepo)
							.getVersionHistory());
				}
				break;
			case RE:
				// Allow the admin to revert the current repo to the previous
				// one;
				if (validateInput1(words)) {
					Repo curr = VersionControlDb.findRepo(currRepo);
					ErrorType returntype = curr.revert(logInUser);
					if (returntype == ErrorType.ACCESS_DENIED) {
						System.out.println(ErrorType.ACCESS_DENIED);
					} else {
						if (returntype == ErrorType.NO_OLDER_VERSION) {
							System.out.println(ErrorType.NO_OLDER_VERSION);
						} else {
							System.out.println(ErrorType.SUCCESS);
						}
					}
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayRepoMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * The main method. Simulation starts here.
	 * 
	 * @param args
	 *            Unused
	 */
	public static void main(String[] args) {
		try {
			processMainMenu();
		}
		// Any exception thrown by the simulation is caught here.
		catch (Exception e) {
			System.out.println(ErrorType.INTERNAL_ERROR);
			// Uncomment this to print the stack trace for debugging purpose.
			// e.printStackTrace();
		}
		// Any clean up code goes here.
		finally {
			System.out.println("Quitting the simulation.");
		}
	}
}
