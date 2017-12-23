import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user. Maintains the list of subscribed repositories, working
 * copy of the subscribed repositories and their changelist.
 * 
 * @author
 *
 */
public class User {

	/* The name of the user. It's a unique identifier for a user. */
	private final String userName;

	/* The list of names of the repositories to which the user is subscribed. */
	private final List<String> subRepos;

	/* The list of all pending check-ins not yet made by the user. */
	private final List<ChangeSet> pendingCheckIns;

	/* The list of all local working copies of the user. */
	private final List<RepoCopy> workingCopies;

	/**
	 * Constructor for User.
	 * 
	 * @param username
	 *            The user name.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public User(String userName) {

		if (userName == null) {
			throw new IllegalArgumentException();
		}
		this.userName = userName;
		this.subRepos = new ArrayList<String>();
		this.pendingCheckIns = new ArrayList<ChangeSet>();
		this.workingCopies = new ArrayList<RepoCopy>();
	}

	/**
	 * Returns the name of the user.
	 * 
	 * @return the user name.
	 */
	public String getName() {
		return this.userName;
	}

	/**
	 * Returns a copy of list of subscribed repositories.
	 * 
	 * @return The subscribed repo list.
	 */
	public List<String> getAllSubRepos() {
		return new ArrayList<String>(this.subRepos);
	}

	/**
	 * Returns the working copy for a repository.
	 * 
	 * @param repoName
	 *            The name of the repository.
	 * @return The working copy if exists, null otherwise.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public RepoCopy getWorkingCopy(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < workingCopies.size(); i++) {
			if (workingCopies.get(i).getReponame().equals(repoName)) {
				return workingCopies.get(i);
			}
		}
		return null;
	}

	/**
	 * Subscribes the user to a repository. Adds a new repository to the
	 * subscribed list.
	 * 
	 * @param repoName
	 *            The name of the repository to subscribe.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public void subscribeRepo(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}

		if (!this.subRepos.contains(repoName)) {
			this.subRepos.add(repoName);
		}
	}

	/**
	 * Un-subscribes the user from a repository. Deletes a repository from the
	 * subscribed list.
	 * 
	 * @param repoName
	 *            The name of the repository to unsubscribe.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public void unsubscribeRepo(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < subRepos.size(); i++) {
			if (subRepos.get(i).equals(repoName)) {
				subRepos.remove(i);
				break;
			}
		}
	}

	/**
	 * Checks if the user is subscribed to a particular repository.
	 * 
	 * @param repoName
	 *            The name of the repository to subscribe.
	 * @return True if the repository is subscribed, false otherwise.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public boolean isSubRepo(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		return subRepos.contains(repoName);
	}

	/**
	 * Adds a new change (add, edit or delete) to the pending checkIn for the
	 * repository. If a checkIn does not exits, a new checkIn is created.
	 * 
	 * @param doc
	 *            The document added, deleted or edited.
	 * @param type
	 *            The type of change.
	 * @param repoName
	 *            The name of the repository on which the change is done.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public void addToPendingCheckIn(Document doc, Change.Type type,
			String repoName) {
		if (doc == null || type == null || repoName == null) {
			throw new IllegalArgumentException();
		}
		// Find the correspounding ChangeSet of the repoName;
		ChangeSet settoFind = getPendingCheckIn(repoName);

		if (settoFind == null) {
			// If a ChangeSet is not found, construct a new one and add it to
			// the pendingCheckIns;
			ChangeSet newset = new ChangeSet(repoName);
			pendingCheckIns.add(newset);
			newset.addChange(doc, type);
		} else {
			// Add the change directly to the correspounding ChangeSet;
			settoFind.addChange(doc, type);
		}
	}

	/**
	 * Returns the pending check-in for a repository.
	 * 
	 * @param repoName
	 *            The name of the repository.
	 * @return The pending check-in for the repository if exists, null
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ChangeSet getPendingCheckIn(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < pendingCheckIns.size(); i++) {
			if (pendingCheckIns.get(i).getReponame().equals(repoName)) {
				return pendingCheckIns.get(i);
			}
		}

		return null;
	}

	/**
	 * Checks in or queues a pending checkIn into a repository and removes it
	 * from the local pending CheckIns list.
	 * 
	 * @param repoName
	 *            The name of repository.
	 * @return NO_LOCAL_CHANGES, if there are no pending changes for the
	 *         repository, SUCCESS otherwise.
	 * @throws EmptyQueueException
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ErrorType checkIn(String repoName) throws EmptyQueueException {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		// get the current repoCopy
		RepoCopy currRepocopy = getWorkingCopy(repoName);

		// get the changeset
		ChangeSet currCheckIn = getPendingCheckIn(repoName);

		if (!(currCheckIn == null)) {
			Repo currRepo = VersionControlDb.findRepo(repoName);

			currRepo.queueCheckIn(currCheckIn);

			pendingCheckIns.remove(currCheckIn);

			return ErrorType.SUCCESS;
		} else {
			return ErrorType.NO_LOCAL_CHANGES;
		}
	}

	/**
	 * Gets a latest version of the documents from the repository and puts them
	 * onto a working copy, if the user is currently subscribed to the
	 * repository. When the latest version is checked out, a new working copy is
	 * created and existing one is deleted.
	 * 
	 * @param repoName
	 *            The name of the repository to check out.
	 * @return REPO_NOT_SUBSCRIBED if the repository is not subscribed, SUCCESS
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ErrorType checkOut(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		if (isSubRepo(repoName)) {
			// Find the repo with the repoName;
			Repo curr = VersionControlDb.findRepo(repoName);
			// construct a new RepoCooy
			RepoCopy newRepoCopy = new RepoCopy(repoName, curr.getVersion(),
					curr.getDocuments());
			if (!(getWorkingCopy(repoName) == null)) {
				// If the repocopy is already existed, remove and add the new
				// one;
				workingCopies.remove(getWorkingCopy(repoName));
				workingCopies.add(newRepoCopy);
			} else {
				// If the repocopy is not existed and directly add the new one;
				workingCopies.add(newRepoCopy);
			}
			return ErrorType.SUCCESS;
		} else {
			return ErrorType.REPO_NOT_SUBSCRIBED;
		}
	}

	@Override
	public String toString() {
		String str = "=================================== \n";
		str += "Username: " + this.userName + "\n"
				+ "-----------Repos------------------ \n";
		int count = 0;
		for (String r : this.subRepos) {
			str += ++count + ". " + r + "\n";
		}
		str += this.subRepos.size() + " repos(s) subscribed.\n"
				+ "===================================";
		return str;
	}
}
