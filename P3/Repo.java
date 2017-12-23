import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository which stores and tracks changes to a collection of
 * documents.
 * 
 * @author
 *
 */
public class Repo {

	/* The current version of the repo. */
	private int version;

	/* The name of the repo. It's a unique identifier for a repository. */
	private final String repoName;

	/* The user who is the administrator of the repo. */
	private final User admin;

	/* The collection(list) of documents in the repo. */
	private final List<Document> docs;

	/* The check-ins queued by different users for admin approval. */
	private final QueueADT<ChangeSet> checkIns;

	/* The stack of copies of the repo at points when any check-in was applied. */
	private final StackADT<RepoCopy> versionRecords;

	/**
	 * Constructs a repo object.
	 * 
	 * @param admin
	 *            The administrator for the repo.
	 * @param reponame
	 *            The name of the repo.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public Repo(User admin, String repoName) {
		if (admin == null || repoName == null) {
			throw new IllegalArgumentException();
		}
		this.version = 0;
		this.admin = admin;
		this.repoName = repoName;
		this.docs = new ArrayList<Document>();
		this.checkIns = new SimpleQueue<ChangeSet>();
		this.versionRecords = new SimpleStack<RepoCopy>();
		// contruct a RepoCopy for the first Repo and push it onto the
		// versionRecords;
		versionRecords.push(new RepoCopy(repoName, version, docs));
	}

	/**
	 * Return the name of the repo.
	 * 
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.repoName;
	}

	/**
	 * Returns the user who is administrator for this repository.
	 * 
	 * @return The admin user.
	 */
	public User getAdmin() {
		return this.admin;
	}

	/**
	 * Returns a copy of list of all documents in the repository.
	 * 
	 * @return A list of documents.
	 */
	public List<Document> getDocuments() {
		return new ArrayList<Document>(this.docs);
	}

	/**
	 * Returns a document with a particular name within the repository.
	 * 
	 * @param searchName
	 *            The name of document to be searched.
	 * @return The document if found, null otherwise.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public Document getDocument(String searchName) {
		if (searchName == null) {
			throw new IllegalArgumentException();
		}

		for (Document d : this.docs) {
			if (d.getName().equals(searchName)) {
				return d;
			}
		}

		return null;
	}

	/**
	 * Returns the current version of the repository.
	 * 
	 * @return The version of the repository.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Returns the number of versions (or changes made) for this repository.
	 * 
	 * @return The version count.
	 */
	public int getVersionCount() {
		return versionRecords.size();
	}

	/**
	 * Returns the history of changes made to the repository.
	 * 
	 * @return The string containing the history of changes.
	 */
	public String getVersionHistory() {
		return versionRecords.toString();
	}

	/**
	 * Returns the number of pending check-ins queued for approval.
	 * 
	 * @return The count of changes.
	 */
	public int getCheckInCount() {
		return checkIns.size();
	}

	/**
	 * Queue a new check-in for admin approval.
	 * 
	 * @param checkIn
	 *            The check-in to be queued.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public void queueCheckIn(ChangeSet checkIn) {
		if (checkIn == null) {
			throw new IllegalArgumentException();
		}
		checkIns.enqueue(checkIn);

	}

	/**
	 * Returns and removes the next check-in in the queue if the requesting user
	 * is the administrator.
	 * 
	 * @param requestingUser
	 *            The user requesting for the change set.
	 * @return The checkin if the requestingUser is the admin and a checkin
	 *         exists, null otherwise.
	 * @throws EmptyQueueException
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ChangeSet getNextCheckIn(User requestingUser)
			throws EmptyQueueException {
		if (requestingUser == null) {
			throw new IllegalArgumentException();
		}
		if (requestingUser.equals(admin)) {
			return checkIns.dequeue();
		} else {
			return null;
		}
	}

	/**
	 * Applies the changes contained in a particular checkIn and adds it to the
	 * repository if the requesting user is the administrator. Also saves a copy
	 * of changed repository in the versionRecords.
	 * 
	 * @param requestingUser
	 *            The user requesting the approval.
	 * @param checkIn
	 *            The checkIn to approve.
	 * @return ACCESS_DENIED if requestingUser is not the admin, SUCCESS
	 *         otherwise.
	 * @throws EmptyQueueException
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ErrorType approveCheckIn(User requestingUser, ChangeSet checkIn)
			throws EmptyQueueException {
		if (requestingUser == null || checkIn == null) {
			throw new IllegalArgumentException();
		}
		if (requestingUser.equals(admin)) {

			for (int i = 0; i < checkIn.getChangeCount(); i++) {
				Change curr = checkIn.getNextChange();
				// If the Change Type is ADD, add the doc;
				if (curr.getType() == Change.Type.ADD) {
					docs.add(curr.getDoc());
				}
				// If the Change Type is DEL, remove the doc;
				if (curr.getType() == Change.Type.DEL) {
					for (int j = 0; j < docs.size(); j++) {
						docs.remove(getDocument(curr.getDoc().getName()));
					}
				}
				// If the Change Type is EDIT, set the the existed doc to a new
				// doc;
				if (curr.getType() == Change.Type.EDIT) {
					for (int j = 0; j < docs.size(); j++) {
						if (docs.get(j).getName()
								.equals(curr.getDoc().getName())) {
							docs.set(j, curr.getDoc());
							break;
						}
					}
				}
				i--;
			}
			version++;
			// Create a new RepoCopy and push it onto the versionRecords;
			RepoCopy newRepocopy = new RepoCopy(repoName, version, docs);
			versionRecords.push(newRepocopy);
			return ErrorType.SUCCESS;
		} else {

			return ErrorType.ACCESS_DENIED;
		}

	}

	/**
	 * Reverts the repository to the previous version if present version is not
	 * the oldest version and the requesting user is the administrator.
	 * 
	 * @param requestingUser
	 *            The user requesting the revert.
	 * @return ACCESS_DENIED if requestingUser is not the admin,
	 *         NO_OLDER_VERSION if the present version is the oldest version,
	 *         SUCCESS otherwise.
	 * @throws EmptyStackException
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 */
	public ErrorType revert(User requestingUser) throws EmptyStackException {
		if (requestingUser == null) {
			throw new IllegalArgumentException();
		}
		// Check if the requestingUser is the admin of the repo;
		if (requestingUser.equals(admin)) {
			RepoCopy previousCopy = null;

			if (!(version == 0)) {

				versionRecords.pop();
				// Pop the top repocopy;
				previousCopy = versionRecords.peek();
				// Set tje version to the previous one;
				version = previousCopy.getVersion();
				// Remove all the docs from the previous repo;
				for (int i = docs.size() - 1; i >= 0; i--) {
					docs.remove(i);
				}
				// Add the doc from the previous copy one by one;
				for (int j = 0; j < previousCopy.getDocuments().size(); j++) {
					docs.add(previousCopy.getDocuments().get(j));
				}
			} else {
				return ErrorType.NO_OLDER_VERSION;
			}

			return ErrorType.SUCCESS;
		} else {
			return ErrorType.ACCESS_DENIED;
		}
	}
}
