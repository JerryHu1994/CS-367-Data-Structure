///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS               
// Title:            CS 367 Programming Assignment p2
// Files:            AmazonStore.java
//					 DLinkedList.java
// 					 InsufficientCreditException.java
//					 Product.java
//    				 User.java
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
import java.io.PrintStream;
import java.util.*;

/**
 * This is the main class of the program. First the class takes in the input of
 * the user and do the input validation. Then the class loads txt files which
 * contains information of the store products and user's wishlists into to
 * DLinkedLists. Next, the user is prompted to input and the class will perform
 * different operations on the list and user based on different input.
 * 
 * @author Jieru Hu
 *
 */
public class AmazonStore {

	// Store record of users and products

	// The DLinkedList which stores all the products in the store;
	private static ListADT<Product> products = new DLinkedList<Product>();
	// The DLinkedList which contains all the users which is loaded from the txt
	// files;
	private static ListADT<User> users = new DLinkedList<User>();
	// current user logged in
	private static User currentUser = null;

	// scanner for console input
	public static final Scanner stdin = new Scanner(System.in);

	// main method
	public static void main(String args[]) throws InsufficientCreditException {

		// Populate the two lists using the input files: Products.txt User1.txt
		// User2.txt ... UserN.txt
		if (args.length < 2) {
			System.out               
					.println("Usage: java AmazonStore [PRODUCT_FILE] [USER1_FILE] [USER2_FILE] ...");
			System.exit(0);
		}
		// load store products
		loadProducts(args[0]);

		// load users one file at a time
		for (int i = 1; i < args.length; i++)
			loadUser(args[i]);

		// User Input for login
		boolean done = false;
		while (!done) {
			System.out.print("Enter username : ");
			String username = stdin.nextLine();
			System.out.print("Enter password : ");
			String passwd = stdin.nextLine();

			if (login(username, passwd) != null) {
				// generate random items in stock based on this user's wish list
				ListADT<Product> inStock = currentUser.generateStock();
				// show user menu
				userMenu(inStock);
			} else
				System.out.println("Incorrect username or password");

			System.out
					.println("Enter 'exit' to exit program or anything else to go back to login");
			if (stdin.nextLine().equals("exit"))
				done = true;
		}

	}

	/**
	 * Tries to login for the given credentials. Updates the currentUser if
	 * successful login
	 * 
	 * @param username
	 *            name of user
	 * @param passwd
	 *            password of user
	 * @returns the currentUser
	 */
	public static User login(String username, String passwd) {
		if (username == null || passwd == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).checkLogin(username, passwd)) {
				currentUser = users.get(i);
				return users.get(i);
			}
		}
		return null;
	}

	/**
	 * Reads the specified file to create and load products into the store.
	 * Every line in the file has the format: <NAME>#<CATEGORY>#<PRICE>#<RATING>
	 * Create new products based on the attributes specified in each line and
	 * insert them into the products list Order of products list should be the
	 * same as the products in the file For any problem in reading the file
	 * print: 'Error: Cannot access file'
	 * 
	 * @param fileName
	 *            name of the file to read
	 */
	public static void loadProducts(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException();
		}
		try {
			File newfile = new File(fileName);
			Scanner productLoadin = new Scanner(newfile);

			while (productLoadin.hasNextLine()) {
				String productInfo = productLoadin.nextLine();
				String[] productInfolist = productInfo.split("#");
				// instantiate a new product based on the parameter;
				Product newProduct = new Product(productInfolist[0],
						productInfolist[1],
						Integer.parseInt(productInfolist[2]),
						Float.parseFloat(productInfolist[3]));
				products.add(newProduct);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot access file");
		}

	}

	/**
	 * Reads the specified file to create and load a user into the store. The
	 * first line in the file has the format:<NAME>#<PASSWORD>#<CREDIT> Every
	 * other line after that is a name of a product in the user's wishlist,
	 * format:<NAME> For any problem in reading the file print: 'Error: Cannot
	 * access file'
	 * 
	 * @param fileName
	 *            name of the file to read
	 */
	public static void loadUser(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException();
		}
		try {
			File newfile = new File(fileName);
			Scanner UserIn = new Scanner(newfile);
			String userInfo = UserIn.nextLine();
			String[] userInfolist = userInfo.split("#");
			String userName = userInfolist[0];
			String userPass = userInfolist[1];
			int userCredit = Integer.parseInt(userInfolist[2]);
			User newUser = new User(userName, userPass, userCredit);
			while (UserIn.hasNextLine()) {
				String wishproductName = UserIn.nextLine();
				// search in the products in the store and find the
				// correspounding product, add it to the wishlist;
				for (int i = 0; i < products.size(); i++) {
					if (products.get(i).getName().equals(wishproductName)) {
						newUser.addToWishList(products.get(i));
					}
				}
			}

			users.add(newUser);

		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot access file");
		}
	}

	/**
	 * See sample outputs Prints the entire store inventory formatted by
	 * category The input text file for products is already grouped by category,
	 * use the same order as given in the text file format: <CATEGORY1> <NAME>
	 * [Price:$<PRICE> Rating:<RATING> stars] ... <NAME> [Price:$<PRICE>
	 * Rating:<RATING> stars]
	 * 
	 * <CATEGORY2> <NAME> [Price:$<PRICE> Rating:<RATING> stars] ... <NAME>
	 * [Price:$<PRICE> Rating:<RATING> stars]
	 */
	public static void printByCategory() {
		String targetCategory = products.get(0).getCategory();
		System.out.println();
		System.out.println(targetCategory + ":");
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getCategory().equals(targetCategory)) {
				System.out.println(products.get(i).toString());
			} else {
				targetCategory = products.get(i).getCategory();
				System.out.println();
				System.out.println(targetCategory + ":");
				i--;
			}
		}
		System.out.println();
	}

	/**
	 * Interacts with the user by processing commands
	 * 
	 * @param inStock
	 *            list of products that are in stock
	 * @throws InsufficientCreditException
	 */
	public static void userMenu(ListADT<Product> inStock)
			throws InsufficientCreditException {

		if (inStock == null) {
			throw new IllegalArgumentException();
		}
		boolean done = false;
		while (!done) {
			System.out.print("Enter option : ");
			String input = stdin.nextLine();

			// only do something if the user enters at least one character
			if (input.length() > 0) {
				String[] commands = input.split(":");// split on colon, because
														// names have spaces in
														// them
				if (commands[0].length() > 1) {
					System.out.println("Invalid Command");
					continue;
				}
				switch (commands[0].charAt(0)) {

				case 'v':
					// if the input is "v:all", print all products with thrie
					// categories;
					if (commands[1].equals("all")) {
						printByCategory();
					}
					// if the input is "v:wishlist", print products in the
					// wishlist;
					else if (commands[1].equals("wishlist")) {
						PrintStream ps = new PrintStream(System.out);
						currentUser.printWishList(ps);
					}
					// if the inout is "v:instock", print the products in stock;
					else if (commands[1].equals("instock")) {

						for (int i = 0; i < inStock.size(); i++) {
							System.out.println(inStock.get(i).toString());
						}
					} else {
						System.out.println("Invalid Command");
					}
					break;

				// if the input is "s", search for products' name which contains
				// a particular string, and print it out;
				case 's':
					String subName = commands[1];
					for (int i = 0; i < products.size(); i++) {
						if (products.get(i).getName().contains(subName)) {
							System.out.println(products.get(i).toString());
						}
					}
					break;

				// if the input is "a", add the specified product to the
				// wishlist;
				case 'a':
					String productName = commands[1];
					boolean check = true;
					for (int i = 0; i < products.size(); i++) {
						if (products.get(i).getName().equals(productName)) {
							currentUser.addToWishList(products.get(i));
							System.out.println("Added to wishlist");
							check = false;
						}
					}
					if (check) {
						System.out.println("Products not found");
					}
					break;

				// if the input is "r", remove the specified product in the
				// wishlist;
				case 'r':
					String productNamemove = commands[1];
					if (currentUser.removeFromWishList(productNamemove) == null) {
						System.out.println("Products not found");
					} else {
						System.out.println("Removed from wishlist");
					}
					break;

				// if the input is "b", buy the product by removing the product
				// from the wishlist and charges the user credit;
				case 'b':
					for (int i = 0; i < inStock.size(); i++) {
						currentUser.buy(inStock.get(i).getName());
					}
					break;

				// if the input is"c", display the credit of the user;
				case 'c':
					System.out.println("$" + currentUser.getCredit());
					break;

				// if the input is "l", log out the user;
				case 'l':
					done = true;
					System.out.println("Logged Out");
					break;

				default: // a command with no argument
					System.out.println("Invalid Command");
					break;
				}
			}
		}
	}

}
