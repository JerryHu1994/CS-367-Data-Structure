import java.util.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the person who logins in and could perform a set of operations on
 * posts
 * 
 * @author Jerry
 *
 */
public class User {
	final private String name;// the name of the user
	final private Karma karma;// the karma which keeps tracks of likes and
								// dislikes on the user
	private List<String> subscribed;// the list of subrredit which the user is
									// under
	private List<Post> posted;// the list of the posts which is posted by the
								// user
	private List<Post> liked;// the list of the posts which is liked by the user
	private List<Post> disliked;// the list of the posts which is disliked by
								// the user

	/**
	 * The construstor creates a new user
	 * 
	 * @param name
	 *            The name of the user takes from the txt file
	 */
	public User(String name) {
		// initialize the new User;
		this.name = name;
		karma = new Karma();
		subscribed = new ArrayList<String>();
		posted = new ArrayList<Post>();
		liked = new ArrayList<Post>();
		disliked = new ArrayList<Post>();
		// TODO
	}

	/**
	 * 
	 * @return a string which represents the name of the user
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @return a karma which is the karma of the user
	 */
	public Karma getKarma() {
		return this.karma;
	}

	/**
	 * 
	 * @return a list which records the a list of subrredits the user is under
	 */
	public List<String> getSubscribed() {
		return subscribed;
		// TODO
	}

	/**
	 * 
	 * @return a list which contains the posts posted by the user
	 */
	public List<Post> getPosted() {
		return posted;
		// TODO
	}

	/**
	 * 
	 * @return a list which is liked by the user
	 */
	public List<Post> getLiked() {
		return liked;
		// TODO
	}

	/**
	 * 
	 * @return a list which is disliked by the user
	 */
	public List<Post> getDisliked() {
		return disliked;
		// TODO
	}

	/**
	 * Add the subreddit to user's subreddit list if it is not there Otherwise
	 * unsubscribe it
	 * 
	 * @param subreddit
	 *            the string which represents the subreddit under which we want
	 *            to add the user
	 */
	public void subscribe(String subreddit) {
		// initialize the iterator points at each subscribed string;
		Iterator<String> itr = subscribed.iterator();
		while (itr.hasNext()) {
			// check if the subreddit is already existed;
			if (subreddit.equals(itr.next())) {
				// remove the subreddit from the subscribed list;
				unsubscribe(subreddit);
				return;
			}
		}
		// if not existed, add this subreddit;
		subscribed.add(subreddit);
		// TODO
	}

	/**
	 * Find the subreddit in the subscribed list and remove it
	 * 
	 * @param subreddit
	 *            the string which represents the subreddit under which we want
	 *            to remove the user
	 */
	public void unsubscribe(String subreddit) {
		Iterator<String> itr = subscribed.iterator();
		// Use i to keep track of the iterator
		int i = 0;
		while (itr.hasNext()) {
			// remove the subreddit when iterator find the subreddit
			if (subreddit.equals((itr.next()))) {
				subscribed.remove(i);
			}
			i++;
		}
		// TODO
	}

	/**
	 * Add a new post under the user
	 * 
	 * @param subreddit
	 *            string represents user's subreddit
	 * @param type
	 *            the post's type
	 * @param title
	 *            the string which represents the content of the post
	 * @return the new post
	 */
	public Post addPost(String subreddit, PostType type, String title) {
		// Create the newPost;
		Post newPost = new Post(this, subreddit, type, title);
		// Add the newPost to the posted list;
		posted.add(newPost);
		return newPost;
		// TODO
	}

	/**
	 * Like and upvote the post if is is not currently liked by the user
	 * Otherwise undolike If it is disliked by the user, undodislike
	 * 
	 * @param post
	 *            the post user is going to like
	 */
	public void like(Post post) {

		// Create a boolean variable checks if the user likes the post;
		boolean likeCheck = true;

		// If the post is already in the likedlist, undolike and make the
		// likeCheck to false;
		for (int i = 0; i < liked.size(); i++) {
			if (post.equals(liked.get(i))) {
				undoLike(post);
				likeCheck = false;
			}
		}

		// If the post is not in the likedlist, upvote it and add it to the
		// liked list;
		if (likeCheck) {
			post.upvote();
			liked.add(post);
		}

		// check if the post is disliked: if it is disliked, undo the dislike;
		for (int i = 0; i < disliked.size(); i++) {
			if (post.equals(disliked.get(i))) {
				undoDislike(post);
			}
		}

		// TODO
	}

	/**
	 * find the post in the liked list and remove it
	 * 
	 * @param post
	 *            post we want to remove
	 */
	public void undoLike(Post post) {
		// decrease the karma of the post by two
		post.downvote();
		post.downvote();
		Iterator<Post> itr = liked.iterator();
		// keep track of the index which the Iterator points on the liked list;
		int i = 0;
		while (itr.hasNext()) {
			// Remove the post when Iterator finds the post;
			if (post.equals(itr.next())) {
				liked.remove(i);
				break;
			}
			i++;
		}

		// TODO
	}

	/**
	 * Dislike and downvote the post if is is not currently disliked by the user
	 * Otherwise undodislike If it is liked by the user, undolike
	 * 
	 * @param post
	 *            post we want to dislike
	 */
	public void dislike(Post post) {
		// Create a boolean variable checks if the user likes the post;
		boolean dislikeCheck = true;
		Iterator<Post> itr = disliked.iterator();
		while (itr.hasNext()) {
			// if it is liked, undolike the post, and make the dislikeCheck
			// false;
			if (post.equals(itr.next())) {
				undoDislike(post);
				dislikeCheck = false;
			}
		}
		// if it is not in the disliked list, downvote and add the post;
		if (dislikeCheck) {
			post.downvote();
			disliked.add(post);
		}

		// check if the post is liked: if it is liked, undo the like;
		for (int i = 0; i < liked.size(); i++) {
			if (post.equals(liked.get(i))) {
				undoLike(post);
			}
		}
		// TODO
	}

	/**
	 * Find the post in the dislike list and remove it
	 * 
	 * @param post
	 *            post we want to remove
	 */
	public void undoDislike(Post post) {

		Iterator<Post> itr = disliked.iterator();
		// keep track of the index which the Iterator points on the liked list;
		int pos = 0;
		while (itr.hasNext()) {
			// Remove the post when Iterator finds the post;
			if (itr.next().equals(post)) {
				disliked.remove(pos);
				break;
			}
			pos++;
		}
		// increase the karma by one;
		post.upvote();
		post.downvote();
		// TODO
	}

}
