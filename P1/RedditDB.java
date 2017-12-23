import java.util.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a database which contains a list of users and some operation which
 * can be done on the user list
 * 
 * @author Jerry
 *
 */
public class RedditDB {
	private List<User> users;

	/**
	 * Intialize a new RedditDB
	 */
	public RedditDB() {
		this.users = new ArrayList<User>();
	}

	/**
	 * 
	 * @return the list of the user
	 */
	public List<User> getUsers() {
		return users;
		// TODO
	}

	/**
	 * Add a new user into the list
	 * 
	 * @param name
	 *            the string represents the name of the user we want to add
	 * @return null if the user already exists; added user if the user does not
	 *         exits
	 */
	public User addUser(String name) {
		// Create a new User;
		User u = new User(name);
		// Check if the new User is in the users list, if yes, return null;
		Iterator<User> itr = users.iterator();
		while (itr.hasNext()) {
			if (itr.next().equals(u)) {
				return null;
			}
		}
		// Add the user to the list and return it;
		users.add(u);
		return u;
		// TODO
	}

	/**
	 * Find the user in the user list by the given name
	 * 
	 * @param name
	 *            the string represents the name of the user we want to find
	 * @return the user if the user already exists; null if the user does not
	 *         exits
	 */
	public User findUser(String name) {

		// Create a Iterator to find a particular user;
		Iterator<User> itr = users.iterator();
		int i = 0;
		while (itr.hasNext()) {
			// Return the user if found;
			if (itr.next().getName().equals(name)) {
				return users.get(i);

			}
			i++;
		}
		// Otherwise return null;
		return null;

		// TODO
	}

	/**
	 * Remove the user from the user list by the given name
	 * 
	 * @param name
	 *            the string represents the name of the user we want to delete
	 * @return true if the user is deleted and false if not
	 */
	public boolean delUser(String name) {

		/*
		 * for(int i=0;i<users.size();i++){
		 * if(users.get(i).getName().equals(name)){ for(int
		 * j=0;j<users.get(i).getLiked().size();j++){
		 * System.out.println(users.get(i).getLiked().get(j).getTitle()); } } }
		 */
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				for (int z = 0; z < users.get(i).getPosted().size(); z++) {
					for (int j = 0; j < users.size(); j++) {
						for (int a = 0; a < users.get(j).getLiked().size(); a++) {
							if (users
									.get(i)
									.getPosted()
									.get(z)
									.getTitle()
									.equals(users.get(j).getLiked().get(a)
											.getTitle())) {
								users.get(j).getLiked().remove(a);
							}
						}

					}
				}
			}
		}
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {

				for (int z = 0; z < users.get(i).getPosted().size(); z++) {
					for (int j = 0; j < users.size(); j++) {
						for (int a = 0; a < users.get(j).getDisliked().size(); a++) {
							if (users
									.get(i)
									.getPosted()
									.get(z)
									.getTitle()
									.equals(users.get(j).getDisliked().get(a)
											.getTitle())) {
								users.get(j).getDisliked().remove(a);
							}
						}

					}
				}
			}
		}
		/*
		 * for(int i=0;i<users.size();i++){
		 * if(users.get(i).getName().equals(name)){ for(int
		 * j=0;j<users.get(i).getLiked().size();j++){
		 * System.out.println(users.get(i).getLiked().get(j).getTitle()); } } }
		 */
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				for (int j = 0; j < users.get(i).getLiked().size(); j++) {

					users.get(i).undoLike(users.get(i).getLiked().get(j));
					j--;
				}

			}

		}
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				for (int j = 0; j < users.get(i).getDisliked().size(); j++) {

					users.get(i).undoDislike(users.get(i).getDisliked().get(j));
					j--;
				}
				users.remove(i);
				return true;
			}
		}
		return false;

		// TODO
	}

	/**
	 * 
	 * @param user
	 *            the user whose's posts we want to display
	 * @return all posts from all users, if the user is null; return all the
	 *         posts from the user's subscribed subreddits if a user is
	 *         specified; posts which have previously been liked or disliked by
	 *         the user should not be returned
	 */
	public List<Post> getFrontpage(User user) {
		// if the user is null, return all posts from all users;
		if (user == (null)) {
			List<Post> list = new ArrayList<Post>();
			for (int i = 0; i < users.size(); i++) {
				for (int j = 0; j < users.get(i).getPosted().size(); j++) {
					list.add(users.get(i).getPosted().get(j));
				}
			}
			return list;
		}
		// if the user is specified, create an empty postlist;
		List<Post> list = new ArrayList<Post>();

		for (int i = 0; i < users.size(); i++) {
			// first take out each user;
			User compareUser = users.get(i);
			for (int j = 0; j < compareUser.getPosted().size(); j++) {
				Post comparePost = compareUser.getPosted().get(j);
				for (int z = 0; z < user.getSubscribed().size(); z++) {
					notfind:
					// match the subreddit;
					if (comparePost.getSubreddit().equals(
							user.getSubscribed().get(z))) {
						// check if the post was created by the user;
						boolean userself = true;
						if (comparePost.getUser().equals(user)) {
							userself = false;
						}
						if (userself) {
							for (int a = 0; a < user.getLiked().size(); a++) {
								// check if the post is in the liked list of the
								// user;
								if (comparePost.getTitle().equals(
										user.getLiked().get(a).getTitle())) {
									break notfind;
								}
							}
							for (int b = 0; b < user.getDisliked().size(); b++) {
								// check if the post is in the unliked list of
								// the
								// user;
								if (comparePost.getTitle().equals(
										user.getDisliked().get(b).getTitle())) {
									break notfind;
								}

							}
						}
						list.add(comparePost);
					}

				}
			}
		}

		return list;

		// TODO
	}

	/**
	 * 
	 * @param user
	 *            the user we want to display
	 * @param subreddit
	 *            the subreddit to which the post belongs
	 * @return all relevant posts from all users If the user is null; return all
	 *         the posts from the subreddit which have not previously been liked
	 *         or disliked by the user, if the user is specified
	 */
	public List<Post> getFrontpage(User user, String subreddit) {
		if (user == (null)) {
			List<Post> list = new ArrayList<Post>();
			for (int i = 0; i < users.size(); i++) {
				for (int j = 0; j < users.get(i).getPosted().size(); j++) {
					// check if the post is under the given subreddit;
					if (users.get(i).getPosted().get(j).getSubreddit()
							.equals(subreddit)) {
						list.add(users.get(i).getPosted().get(j));
					}
				}
			}
			return list;
		}
		List<Post> list = new ArrayList<Post>();

		for (int j = 0; j < users.size(); j++) {
			User compareUser = users.get(j);
			for (int z = 0; z < compareUser.getPosted().size(); z++) {
				Post comparePost = compareUser.getPosted().get(z);
				notfind: if (comparePost.getSubreddit().equals(subreddit)) {
					boolean userself = true;
					if (comparePost.getUser().equals(user)) {
						userself = false;
					}
					if (userself) {
						for (int a = 0; a < user.getLiked().size(); a++) {
							// check if the post is in the liked list of the
							// user;
							if (comparePost.getTitle().equals(
									user.getLiked().get(a).getTitle())) {
								break notfind;
							}
						}
						for (int b = 0; b < user.getDisliked().size(); b++) {
							// check if the post is in the unliked list of the
							// user;
							if (comparePost.getTitle().equals(
									user.getDisliked().get(b).getTitle())) {
								break notfind;
							}

						}
					}
					list.add(comparePost);
				}

			}
		}

		return list;

		// TODO
	}

}
