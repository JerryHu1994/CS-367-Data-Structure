///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS               
// Title:            CS 367 Programming Assignment p1
// Files:            Reddit.java
//					 RedditDB.java
// 					 Post.java
//					 Karma.java
//    				 User.java
// 					 PostType.java
// Semester:         CS367 Spring 2015
//
// Author:           Jieru Hu
// Email:            jhu76@wisc.edu
// CS Login:         jieru
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001
////////////////////////////////////////////////////////////////////////////////
import java.util.*;
import java.io.*;

/**
 * This is the main class of the program. First, the class checks if the command
 * argument input is valid. Then, it checks if the input file exits and and
 * loads data into the redditdatabase Prompt user to enter command options until
 * the user exites the program
 * 
 * @author Jieru Hu
 *
 */
public class Reddit {
	public static void main(String[] args) {
		// The following block receives the argument from Eclipse and throw out
		// exceptions
		// command line from Run Configurations
		try {
			// check if the length given is larger than or equal to 3;
			if (args.length < 3) {
				throw new IllegalArgumentException();
			}
			// check if the first two word in the command is correct;
			if (!(args[0].equals("java") && args[1].equals("Reddit"))) {
				throw new IllegalArgumentException();
			}
			// check if the word starting from the third position contains
			// ".txt";
			for (int i = 2; i < args.length; i++) {
				if (!(args[i].contains("txt"))) {
					throw new IllegalArgumentException();
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: java Reddit <FileNames>");
			System.exit(0);
		}

		// The following block checks if the input file exists;
		Scanner newIn = null;
		// The following block loads data from each file by a for loop;
		RedditDB redditDatabase = new RedditDB();
		String newuserName = "";
		try {
			for (int i = 2; i < args.length; i++) {
				newuserName = args[i].split("\\.")[0];
				File newFile = new File(args[i]);
				newIn = new Scanner(newFile);
				User newUser = new User(newuserName);

				// takes in the first line of the file which are the subreddits
				// and subreddit the user under these subreddits
				String redditLine = newIn.nextLine();
				String[] sepredditLine = redditLine.split(", ");

				// takes lines starting from the second line and creating the
				// post for the user by using the <reddit, PostType, Title>
				// format
				for (int j = 0; j < sepredditLine.length; j++) {
					newUser.subscribe(sepredditLine[j]);
				}
				int postCount = 0;
				while (newIn.hasNextLine()) {
					String postLine = newIn.nextLine();
					String[] seppostLine = postLine.split(", ");
					// This stores the reddit of the post;
					String newreddit = seppostLine[0];
					// This stores the PostType;
					PostType newType = PostType.valueOf(seppostLine[1]);
					// This stores the Title;
					String newTitle = seppostLine[2];
					// Store the title in series if they are seperated by the
					// previous split;
					for (int z = 3; z < seppostLine.length; z++) {
						newTitle = newTitle + ", " + seppostLine[z];
					}
					newUser.addPost(newreddit, newType, newTitle);
					newUser.like(newUser.getPosted().get(postCount));
					postCount++;
				}
				redditDatabase.getUsers().add(newUser);
			}

		} catch (FileNotFoundException e) {
			System.out.println("File " + newuserName + " not found.");
			System.exit(0);
		} finally {
			newIn.close();
		}

		// Get input from users and start the program;
		// Create a User object which represents the user who currently logins
		// in;
		User userLogin = null;
		// create a variable checks whether the program will be terminated;
		boolean conProgram = true;
		while (conProgram) {
			// check if the user if login in;
			if (userLogin == (null)) {
				System.out.print("[anon@reddit]$ ");
			} else {
				System.out.print("[" + userLogin.getName() + "@reddit]$ ");
			}

			Scanner stdin = new Scanner(System.in);
			String input = stdin.nextLine();
			String lowcaseInput = input.toLowerCase();
			// get the command from the user, seperate the input if needed and
			// take the first part of the input as command
			String[] listInput = lowcaseInput.split(" ");
			String option = listInput[0];

			switch (option) {

			// Prints the list of the users and their karma information;
			case "s": {
				if (userLogin == null) {
					System.out.println("Invalid command!");
				} else {
					if (userLogin.getName().equals("admin")) {
						System.out.println("admin	0	0");
					}
					// Creates an iterator which points to users in the
					// redditDatabase;
					Iterator<User> itr = redditDatabase.getUsers().iterator();
					while (itr.hasNext()) {
						// Save the current user which is pointed by the
						// iterator;
						User currentUser = itr.next();
						System.out.println(currentUser.getName() + "	"
								+ currentUser.getKarma().getLinkKarma() + "	"
								+ currentUser.getKarma().getCommentKarma());
					}
				}
				break;
			}

			// Delete a specific user with user admin loged in;
			case "d": {

				if (userLogin == null) {
					System.out.println("Invalid command!");
				} else {
					String deleteUser = listInput[1];
					// check if the current user is admin;
					if (userLogin.getName().equals("admin")) {
						boolean findcheck = redditDatabase.delUser(deleteUser);

						if (findcheck) {
							System.out.println("User " + deleteUser
									+ " deleted");
						}
						// If the username is not found, print this statement
						if (!findcheck) {
							System.out.println("User " + deleteUser
									+ " not found");
						}
					} else {
						// If the username is not admin, print this;
						System.out.println("Invalid command!");
					}
				}
				break;
			}

			// Logged out a specific user
			case "l": {
				// Check if there is another input behind the "l";
				// If the listInput is of length 2, do the logged in part, else
				// do the logged out part;
				if (listInput.length == 2) {
					// Store the name of the user who is going to login;
					String loginUsername = listInput[1];

					// check if there is user currently logged in;
					if (!(userLogin == (null))) {
						System.out.println("User " + userLogin.getName()
								+ " already logged in.");
					} else {
						// Check if the login name is admin;
						if (loginUsername.equals("admin")) {
							userLogin = new User(loginUsername);
							System.out.println("User admin logged in.");
						} else {
							Iterator<User> itr2 = redditDatabase.getUsers()
									.iterator();
							// check if the logged user if found
							int check = 0;
							// Keep track of the iterator;
							int pos = 0;
							while (itr2.hasNext()) {

								if (itr2.next().getName().equals(loginUsername)) {
									// Assign the user to the userLogin (login
									// in the user);
									userLogin = redditDatabase.getUsers().get(
											pos);
									System.out.println("User " + loginUsername
											+ " logged in.");
									check = 1;
									break;
								}
								pos++;
							}
							// If the login username is not found, print this;
							if (check == 0) {
								System.out.println("User " + loginUsername
										+ " not found.");
							}
						}
					}
				} else {
					// Check if there is a user logged in;
					if (userLogin == (null)) {
						System.out.println("No user logged in.");
					} else {

						// Store the name of the current user;
						String logoutName = userLogin.getName();
						userLogin = null;
						System.out.println("User " + logoutName
								+ " logged out.");
					}
				}
				break;
			}

			// Display the frontpage for the logged user;
			case "f": {
				System.out.println("Displaying the front page...");

				// check if the input is invalid;
				boolean validCheck = true;
				// create a boolean varible that checks if the sub-menu exits;
				boolean exitCheck = true;
				// create the exit break from where the user could exit the
				// sub-menu and go back to the main menu;
				Iterator<Post> itr6 = redditDatabase.getFrontpage(userLogin)
						.iterator();
				while (itr6.hasNext()) {
					boolean stopsub = false;
					// Store the current post;
					Post currentPost = itr6.next();
					// print the post
					System.out.println(currentPost.getKarma() + "	"
							+ currentPost.getTitle());
					validCheck = true;
					while (validCheck) {
						// check if the userlogin is null;
						if (userLogin == null) {
							System.out.print("[anon@reddit]$ ");
						} else {
							System.out.print("[" + userLogin.getName()
									+ "@reddit]$ ");
						}
						// get input from user for the submenu;
						Scanner sub = new Scanner(System.in);
						String subInput = sub.next();

						// create the submenu;
						switch (subInput) {
						// check if the userLogin is null and upvote if needed;
						case "a": {
							if (userLogin == (null)) {
								System.out.println("Login to like post.");
								// print the post
								System.out.println(currentPost.getKarma() + "	"
										+ currentPost.getTitle());
								validCheck = true;
							} else {
								userLogin.like(currentPost);
								validCheck = false;
							}
							break;
						}
						// check if the userLogin is null and downvote if
						// needed;
						case "z": {
							if (userLogin == (null)) {
								System.out.println("Login to dislike post.");
								// print the post
								System.out.println(currentPost.getKarma() + "	"
										+ currentPost.getTitle());
								validCheck = true;
							} else {
								userLogin.dislike(currentPost);
								validCheck = false;
							}
							break;

						}
						// break the switch statement and go to the next
						// iterator;
						case "j": {
							validCheck = false;
							break;
						}
						// exit the sub-menu;
						case "x": {
							System.out.println("Exiting to the main menu...");
							exitCheck = false;
							validCheck = false;
							stopsub = true;
							break;
						}
						// check if the input is valid;

						default: {
							System.out.println("Invalid command!");
							validCheck = true;
						}
						}
					}
					if (stopsub) {
						break;
					}

				}

				if (exitCheck) {
					System.out.println("No posts left to display.");
					System.out.println("Exiting to the main menu...");
				}
				break;
			}

			// display the posts for a specific subreddit;
			case "r": {
				// Store the subredditName which we want to display;
				String subredditName = listInput[1];
				boolean validName = false;
				// create a boolean varible that checks if the sub-menu
				// exits;
				boolean exitCheck = true;
				for (int i = 0; i < redditDatabase.getUsers().size(); i++) {
					User temp = redditDatabase.getUsers().get(i);
					for (int j = 0; j < temp.getSubscribed().size(); j++) {
						if (temp.getSubscribed().get(j).equals(subredditName)) {
							validName = true;
						}
					}
				}

				// print the statement;
				System.out.println("Displaying /u/" + subredditName + "...");
				if (validName) {
					// check if the input is invalid;
					boolean validCheck = true;

					// create the exit break from where the user could exit the
					// sub-menu and go back to the main menu;
					Iterator<Post> itr5 = redditDatabase.getFrontpage(
							userLogin, subredditName).iterator();

					while (itr5.hasNext()) {
						// Store the current post;
						Post currentPost = itr5.next();

						validCheck = true;
						while (validCheck) {
							// print the post
							System.out.println(currentPost.getKarma() + "	"
									+ currentPost.getTitle());
							// check if the userlogin is null;
							if (userLogin == null) {
								System.out.print("[anon@reddit]$ ");
							} else {
								System.out.print("[" + userLogin.getName()
										+ "@reddit]$ ");
							}
							// get input from user for the submenu;
							Scanner sub = new Scanner(System.in);
							String subInput = sub.next();

							// create the submenu;
							switch (subInput) {
							// check if the userLogin is null and upvote if
							// needed;
							case "a": {
								if (userLogin == (null)) {
									System.out.println("Login to like post.");
									System.out.println(currentPost.getKarma()
											+ "	" + currentPost.getTitle());
									validCheck = true;
								} else {
									userLogin.like(currentPost);
									validCheck = false;
								}
								break;
							}
							// check if the userLogin is null and downvote if
							// needed;
							case "z": {
								if (userLogin == (null)) {
									System.out
											.println("Login to dislike post.");
									System.out.println(currentPost.getKarma()
											+ "	" + currentPost.getTitle());
									validCheck = true;
								} else {
									userLogin.dislike(currentPost);
									validCheck = false;
								}
								break;

							}
							// break the switch statement and go to the next
							// iterator;
							case "j": {
								validCheck = false;
								break;
							}
							// exit the sub-menu;
							case "x": {
								System.out
										.println("Exiting to the main menu...");
								exitCheck = true;
								validCheck = false;
								break;
							}
							// check if the input is valid;

							default: {
								System.out.println("Invalid command!");
								validCheck = true;
							}
							}
						}
					}
				}
				// check if the case"x" is executed and print the statement
				// if not;
				if (exitCheck) {
					System.out.println("No posts left to display.");
					System.out.println("Exiting to the main menu...");
				}
				break;
			}

			// Display the posts which is posted by a particular user;
			case "u": {
				// Store the username who we want to display;
				String userName = listInput[1];
				boolean rightName = false;
				// create a boolean varible that checks if the sub-menu
				// exits;
				boolean exitCheck = true;

				for (int i = 0; i < redditDatabase.getUsers().size(); i++) {
					if (redditDatabase.getUsers().get(i).getName()
							.equals(userName)) {
						rightName = true;
					}
				}

				// print the statement
				System.out.println("Displaying /u/" + userName + "...");
				if (rightName) {
					// find the user corresponding to the given name

					// Takes out the User object we want to display;
					User displayUser = redditDatabase.findUser(userName);
					// check if the input is invalid;
					boolean validCheck = true;

					// sub-menu and go back to the main menu;
					Iterator<Post> itr6 = displayUser.getPosted().iterator();

					while (itr6.hasNext()) {
						// Store the current post;
						Post currentPost = itr6.next();
						// print the post
						System.out.println(currentPost.getKarma() + "	"
								+ currentPost.getTitle());
						validCheck = true;
						while (validCheck) {
							// check if the userlogin is null;
							if (userLogin == null) {
								System.out.print("[anon@reddit]$ ");
							} else {
								System.out.print("[" + userLogin.getName()
										+ "@reddit]$ ");
							}
							// get input from user for the submenu;
							Scanner sub = new Scanner(System.in);
							String subInput = sub.next();

							// create the submenu;
							switch (subInput) {
							// check if the userLogin is null and upvote if
							// needed;
							case "a": {
								if (userLogin == (null)) {
									System.out.println("Login to like post.");
									System.out.println(currentPost.getKarma()
											+ "	" + currentPost.getTitle());
									validCheck = true;
								} else {
									userLogin.like(currentPost);
									validCheck = false;
								}
								break;
							}
							// check if the userLogin is null and downvote if
							// needed;
							case "z": {
								if (userLogin == (null)) {
									System.out
											.println("Login to dislike post.");
									System.out.println(currentPost.getKarma()
											+ "	" + currentPost.getTitle());
									validCheck = true;
								} else {
									userLogin.dislike(currentPost);
									validCheck = false;
								}
								break;

							}
							// break the switch statement and go to the next
							// iterator;
							case "j": {

								validCheck = false;
								break;

							}
							// exit the sub-menu;
							case "x": {
								System.out
										.println("Exiting to the main menu...");
								exitCheck = true;
								validCheck = false;
								break;
							}
							// check if the input is valid;

							default: {
								System.out.println("Invalid command!");
								validCheck = true;
							}
							}
						}
					}
					// check if the case"x" is executed and print the statement
					// if not;
				}
				if (exitCheck) {
					System.out.println("No posts left to display.");
					System.out.println("Exiting to the main menu...");
				}
				rightName = false;

				break;
			}
			// Print the statement and terminates the program;
			case "x": {
				System.out.println("Exiting to the real world...");
				System.exit(0);
			}
			default: {
				System.out.println("Invalid command!");
				break;
			}
			}
		}
	}
}