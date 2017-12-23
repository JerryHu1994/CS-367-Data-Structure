/**
 * Represents the karma which is a variable in the User class;
 * 
 * @author Jerry
 *
 */
public class Karma {
	private int linkKarma;// counts the karma of the LINK type
	private int commentKarma;// counts the karma of the COMMENT type

	/**
	 * The constructor initialize thes linkKarma and commentKarma to be zero.
	 */
	public Karma() {
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	/**
	 * Upvote the karma of the user based on the type
	 * 
	 * @param type
	 *            the type of the post which is defined in the PostType class
	 */
	public void upvote(PostType type) {
		// check if the type of the Karma equals LINK or COMMENT, and increase
		// it by two if needed;
		if (type == (PostType.LINK)) {
			linkKarma += 2;
		}
		if (type == (PostType.COMMENT)) {
			commentKarma += 2;
		}
		// TODO
	}

	/**
	 * Downvote the karma of the user based on the type
	 * 
	 * @param type
	 *            the type of the post which is defined in the PostType class
	 */
	public void downvote(PostType type) {
		// check if the type of the Karma equals LINK or COMMENT, and decrease
		// it by one if needed;
		if (type == (PostType.LINK)) {
			linkKarma -= 1;
		}
		if (type == (PostType.COMMENT)) {
			commentKarma -= 1;
		}
		// TODO
	}

	/**
	 * 
	 * @return the number of the karma in type LINK
	 */
	public int getLinkKarma() {
		return this.linkKarma;
	}

	/**
	 * 
	 * @return the number of the karma in type COMMENT
	 */
	public int getCommentKarma() {
		return this.commentKarma;
	}
}
