/**
 * The post added by the user which contains information of the user, subreddit,
 * type, title and karma.
 * 
 * @author Jerry
 *
 */
public class Post {
	final private User user; // the user who posts the post
	final private String subreddit;// the subreddit the post belongs to
	final private PostType type;// the type of the post
	final private String title;// the content of the post
	private int karma;// the variable keeps track of the like and dislikes on
						// the post

	/**
	 * The contructor takes in the parameter and set the karma to zero
	 * 
	 * @param user
	 *            the user who posts the post
	 * @param subreddit
	 *            the subreddit the post belongs to
	 * @param type
	 *            the type of the post
	 * @param title
	 *            the content of the post
	 */
	public Post(User user, String subreddit, PostType type, String title) {
		// takes in the parameter and initialize the Post;
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		karma = 0;
		// TODO
	}

	/**
	 * Increase the karma of the post if type == LINK or COMMENT
	 */
	public void upvote() {
		// If the type ==LINK or COMMENT, increase the karma of the post by two;
		if (type == (PostType.LINK) || type == (PostType.COMMENT)) {
			karma += 2;
			user.getKarma().upvote(type);
		}else{
			karma +=2;
		}
		// increase the corresbouding karma of the user;
		

		// TODOf
	}

	/**
	 * Decrease the karma of the post if type == LINK or COMMENT
	 */
	public void downvote() {
		// If the type ==LINK or COMMENT, decrease the karma of the post by one
		// ;
		if (type == (PostType.LINK) || type == (PostType.COMMENT)) {
			karma -= 1;
			// decrease the corresbouding karma of the user;
			user.getKarma().downvote(type);
		}else{
			karma -=1;
		}
		
		// TODO
	}

	/**
	 * 
	 * @return the user who posts the post
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * 
	 * @return the subreddit the post belongs to
	 */
	public String getSubreddit() {
		return this.subreddit;
	}

	/**
	 * 
	 * @return the type of the post
	 */
	public PostType getType() {
		return this.type;
	}

	/**
	 * 
	 * @return the content of the post
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 * @return the karma of the post
	 */
	public int getKarma() {
		return this.karma;
	}
}
