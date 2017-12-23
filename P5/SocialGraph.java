import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * The class entends the UndirectedGraph class and represents a social nextwork
 * graph. It has specific methods including friendsOfFriends and pathBetween
 * which is unique to a social network.
 * 
 * @author Jerry
 *
 */
public class SocialGraph extends UndirectedGraph<String> {

	/**
	 * Creates an empty social graph.
	 * 
	 * DO NOT MODIFY THIS CONSTRUCTOR.
	 */
	public SocialGraph() {
		super();
	}

	/**
	 * Creates a graph from a preconstructed hashmap. This method will be used
	 * to test your submissions. You will not find this in a regular ADT.
	 * 
	 * DO NOT MODIFY THIS CONSTRUCTOR. DO NOT CALL THIS CONSTRUCTOR ANYWHERE IN
	 * YOUR SUBMISSION.
	 * 
	 * @param hashmap
	 *            adjacency lists representation of a graph that has no loops
	 *            and is not a multigraph.
	 */
	public SocialGraph(HashMap<String, ArrayList<String>> hashmap) {
		super(hashmap);
	}

	/**
	 * Returns the a set of 2-degree friends of the person If the person is not
	 * in the graph, throw IllegalargumentException
	 * 
	 * @param person
	 *            the person whose friends is going to be found
	 * @return the set of friends
	 */
	public Set<String> friendsOfFriends(String person) {
		// TODO
		if (person == null) {
			throw new IllegalArgumentException();
		}
		if (!hashmap.containsKey(person)) {
			throw new IllegalArgumentException();
		}
		Queue<String> frontier = new LinkedList<String>();
		frontier.add(person);
		// the explored stores vetexes which are already visited
		Set<String> explored = new HashSet<String>();
		// the depths stores person whose has a degree lower or equal to 2
		HashMap<String, Integer> depths = new HashMap<String, Integer>();
		String vertex = null;
		depths.put(person, 0);
		while (!frontier.isEmpty()) {
			vertex = frontier.poll();
			// check if the pointer is at degree more than 2
			if (depths.get(vertex) > 2) {
				break;
			}
			explored.add(vertex);
			Set<String> neighbors = getNeighbors(vertex);
			Iterator<String> itr = neighbors.iterator();
			while (itr.hasNext()) {
				String neighbor = itr.next();
				// if the person is not in the frontier or explored, add it to
				// the depths and frontier
				if (!frontier.contains(neighbor)
						&& !explored.contains(neighbor)) {
					depths.put(neighbor, depths.get(vertex) + 1);
					frontier.add(neighbor);
				}
			}
		}
		// create a new set and extract person whose depths is 2 from the depths
		Set<String> returnset = new HashSet<String>();
		for (String key : depths.keySet()) {
			if (depths.get(key) == 2) {
				returnset.add(key);
			}
		}

		return returnset;
	}

	/**
	 * Returns the shortest path between two person Returns null if there is no
	 * path connecting them If the person is not in the graph, or the two person
	 * is the same person, throw IllegalArgumentException
	 * 
	 * @param pFrom
	 *            the start of the path
	 * @param pTo
	 *            the end of the path
	 * @return list which represents the path
	 */
	public List<String> getPathBetween(String pFrom, String pTo) {
		// TODO
		if (pFrom == null || pTo == null) {
			throw new IllegalArgumentException();
		}
		if (!hashmap.containsKey(pFrom) || !hashmap.containsKey(pTo)
				|| pFrom.equals(pTo)) {
			throw new IllegalArgumentException();
		}
		// do the breadth-first search on the graph starting from the pFrom
		Queue<String> bfs = new LinkedList<String>();
		ArrayList<String> visited = new ArrayList<String>();
		// stores each person and its predecessor
		HashMap<String, String> predpair = new HashMap<String, String>();
		bfs.add(pFrom);
		while (!bfs.isEmpty()) {
			String curr = bfs.poll();
			Set<String> temp = getNeighbors(curr);
			Iterator<String> itr = temp.iterator();
			while (itr.hasNext()) {
				String currnei = itr.next();
				if (!visited.contains(currnei)) {
					visited.add(currnei);
					bfs.add(currnei);
					predpair.put(currnei, curr);
				}
				// when the pTo is found during the breadth-first search, make a
				// new empty list and insert pTo and its predecessor one by one
				// at index 0
				if (currnei.equals(pTo)) {
					List<String> pathlist = new ArrayList<String>();
					pathlist.add(pTo);
					String curr1 = pTo;
					while (predpair.get(curr1) != pFrom) {
						pathlist.add(0, predpair.get(curr1));
						curr1 = predpair.get(curr1);
					}
					pathlist.add(0, pFrom);
					return pathlist;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a pretty-print of this graph in adjacency matrix form. People are
	 * sorted in alphabetical order, "X" denotes friendship.
	 * 
	 * This method has been written for your convenience (e.g., for debugging).
	 * You are free to modify it or remove the method entirely. THIS METHOD WILL
	 * NOT BE PART OF GRADING.
	 *
	 * NOTE: this method assumes that the internal hashmap is valid (e.g., no
	 * loop, graph is not a multigraph). USE IT AT YOUR OWN RISK.
	 *
	 * @return pretty-print of this graph
	 */
	public String pprint() {
		// Get alphabetical list of people, for prettiness
		List<String> people = new ArrayList<String>(this.hashmap.keySet());
		Collections.sort(people);

		// String writer is easier than appending tons of strings
		StringWriter writer = new StringWriter();

		// Print labels for matrix columns
		writer.append("   ");
		for (String person : people)
			writer.append(" " + person);
		writer.append("\n");

		// Print one line of social connections for each person
		for (String source : people) {
			writer.append(source);
			for (String target : people) {
				if (this.getNeighbors(source).contains(target))
					writer.append("  X ");
				else
					writer.append("    ");
			}
			writer.append("\n");

		}

		// Remove last newline so that multiple printlns don't have empty
		// lines in between
		String string = writer.toString();
		return string.substring(0, string.length() - 1);
	}

}
