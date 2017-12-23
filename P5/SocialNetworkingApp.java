///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS               
// Title:            CS 367 Programming Assignment p5
// Files:            SocialNetworkingApp.java
//					 UndirectedGraph.java
// 					 SocialGraph.java
//					 GraphADT.java

// Semester:         CS367 Spring 2015
//
// Author:           Jieru Hu
// Email:            jhu76@wisc.edu
// CS Login:         jieru
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001
////////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This is the main class for the program. First, the class contructs the social
 * networking graph by loading the information in the sampleinput.txt into the
 * SocialGraph. The it asks user to login and give command. Next, the class does
 * the corresponding operation on the graph.
 * 
 * @author Jerry
 *
 */
public class SocialNetworkingApp {

	/**
	 * Returns a social network as defined in the file 'filename'. See
	 * assignment handout on the expected file format.
	 * 
	 * @param filename
	 *            filename of file containing social connection data
	 * @return
	 * @throws FileNotFoundException
	 *             if file does not exist
	 */
	public static SocialGraph loadConnections(String filename)
			throws FileNotFoundException {
		// TODO
		if (filename == null) {
			throw new IllegalArgumentException();
		}
		File newfile = new File(filename);
		stdin = new Scanner(newfile);
		graph = new SocialGraph();
		while (stdin.hasNextLine()) {
			String line = stdin.nextLine();
			String[] namelist = line.split("\\s+");
			graph.addVertex(namelist[0]);
			for (int i = 1; i < namelist.length; i++) {
				graph.addVertex(namelist[i]);
				graph.addEdge(namelist[0], namelist[i]);
			}

		}
		stdin = new Scanner(System.in);

		return graph;
	}

	static Scanner stdin = new Scanner(System.in);
	static SocialGraph graph;// the main gragh database
	static String prompt = ">> "; // Command prompt

	/**
	 * Access main menu options to login or exit the application.
	 * 
	 * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
	 */
	public static void enterMainMenu() {
		boolean exit = false;

		while (!exit) {
			System.out.print(prompt);
			String input = stdin.nextLine();
			String[] tokens = input.split(" ");
			String cmd = tokens[0];
			String person = (tokens.length > 1 ? tokens[1] : null);

			switch (cmd) {
			case "login":
				System.out.println("Logged in as " + person);
				enterSubMenu(person);
				System.out.println("Logged out");
				break;
			case "exit":
				exit = true;
				break;
			default:
				System.out.println("Invalid command");
			}
		}
	}

	/**
	 * Access submenu options to view the social network from the perspective of
	 * currUser. Assumes currUser exists in the network.
	 * 
	 * @param currUser
	 */
	public static void enterSubMenu(String currUser) {

		// Define the set of commands that have no arguments or one argument
		String commands = "friends fof logout print\n"
				+ "connection friend unfriend";
		Set<String> noArgCmds = new HashSet<String>(Arrays.asList(commands
				.split("\n")[0].split(" ")));
		Set<String> oneArgCmds = new HashSet<String>(Arrays.asList(commands
				.split("\n")[1].split(" ")));

		boolean logout = false;
		while (!logout) {
			System.out.print(prompt);

			// Read user commands
			String[] tokens = stdin.nextLine().split(" ");
			String cmd = tokens[0];
			String otherPerson = (tokens.length > 1 ? tokens[1] : null);

			// Reject erroneous commands
			// You are free to do additional error checking of user input, but
			// this isn't necessary to receive a full grade.
			if (tokens.length == 0)
				continue;
			if (!noArgCmds.contains(cmd) && !oneArgCmds.contains(cmd)) {
				System.out.println("Invalid command");
				continue;
			}
			if (oneArgCmds.contains(cmd) && otherPerson == null) {
				System.out.println("Did not specify person");
				continue;
			}

			switch (cmd) {

			// Prints the shortest path between the current user to the person
			case "connection": {
				// TODO
				List<String> temp = graph.getPathBetween(currUser, otherPerson);
				if (temp == null) {
					System.out.println("You are not connected to "
							+ otherPerson);
				} else {
					System.out.print("[");
					for (int i = 0; i < temp.size(); i++) {
						if (i == (temp.size() - 1)) {
							System.out.print(temp.get(i));
						} else {
							System.out.print(temp.get(i) + ", ");
						}
					}
					System.out.println("]");
				}
				break;
			}

			// Prints all 1st-degree friends of the current user
			case "friends": {
				// TODO
				Set<String> temp = graph.getNeighbors(currUser);
				ArrayList<String> list = new ArrayList<String>(temp);
				Collections.sort(list);

				if (temp.isEmpty()) {
					System.out.println("You do not have any friends");
				} else {
					System.out.print("[");

					for (int i = 0; i < list.size(); i++) {
						if (i != list.size() - 1) {
							System.out.print(list.get(i) + ", ");
						} else {
							System.out.print(list.get(i));
						}
					}
					System.out.println("]");
				}
				break;
			}

			// Prints all 2nd-degree friends of the current user
			case "fof": {
				// TODO
				Set<String> temp = graph.friendsOfFriends(currUser);
				ArrayList<String> list = new ArrayList<String>(temp);
				Collections.sort(list);

				if (temp.isEmpty()) {
					System.out
							.println("You do not have any friends of friends");
				} else {
					System.out.print("[");
					for (int i = 0; i < list.size(); i++) {
						if (i != list.size() - 1) {
							System.out.print(list.get(i) + ", ");
						} else {
							System.out.print(list.get(i));
						}
					}
					System.out.println("]");
				}
				break;
			}

			// Creates friendship relation between the current user and the
			// person
			case "friend": {
				// TODO
				if (graph.addEdge(currUser, otherPerson) == false) {
					System.out.println("You are already friends with "
							+ otherPerson);
				} else {
					System.out.println("You are now friends with "
							+ otherPerson);
				}
				break;
			}

			// Removes friendship relation between the current user and the
			// person
			case "unfriend": {
				// TODO
				if (!graph.getNeighbors(currUser).contains(otherPerson)) {
					System.out.println("You are already not friends with "
							+ otherPerson);
				} else {
					graph.removeEdge(currUser, otherPerson);
					System.out.println("You are no longer friends with "
							+ otherPerson);
				}
				break;
			}

			case "print": {
				// This command is left here for your debugging needs.
				// You may want to call graph.toString() or graph.pprint() here
				// You are free to modify this or remove this command entirely.
				//
				// YOU DO NOT NEED TO COMPLETE THIS COMMAND
				// THIS COMMAND WILL NOT BE PART OF GRADING
				break;
			}

			case "logout":
				logout = true;
				break;
			} // End switch
		}
	}

	/**
	 * Commandline interface for a social networking application.
	 *
	 * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java SocialNetworkingApp <filename>");
			return;
		}
		try {
			graph = loadConnections(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		enterMainMenu();

	}

}
