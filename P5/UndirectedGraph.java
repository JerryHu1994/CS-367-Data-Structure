import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * It is a class implements GraphADT with an adjacency representation. Each
 * vetex represents a person. It implements methods including adVertex,
 * addEdge,removeEdge,getNeighbors and getAllVertices which enables the user to
 * do operations on the graph.
 * 
 * @author Jerry
 *
 * @param <V>
 */
public class UndirectedGraph<V> implements GraphADT<V> {

	// Stores the vertices of this graph, and their adjacency lists.
	// It's protected rather than private so that subclasses can access it.
	protected HashMap<V, ArrayList<V>> hashmap;

	public UndirectedGraph() {
		this.hashmap = new HashMap<V, ArrayList<V>>();
	}

	public UndirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
		if (hashmap == null)
			throw new IllegalArgumentException();
		this.hashmap = hashmap;
	}

	/*
	 * (non-Javadoc) Add the specified vertex into the graph and return true If
	 * the vextex is already contained, just return false
	 * 
	 * @see GraphADT#addVertex(java.lang.Object)
	 */
	@Override
	public boolean addVertex(V vertex) {
		// TODO
		if (vertex == null) {
			throw new IllegalArgumentException();
		}
		if (hashmap.containsKey(vertex)) {
			return false;
		}
		hashmap.put(vertex, new ArrayList<V>());
		return true;
	}

	/*
	 * (non-Javadoc) Add an edge between two specified vertexes and return true
	 * If there is already a edge existed, just return false If the vertices are
	 * not found in the graph, throw IllegalArgumentException
	 * 
	 * @see GraphADT#addEdge(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean addEdge(V v1, V v2) {
		// TODO
		if (v1 == null || v2 == null) {
			throw new IllegalArgumentException();
		}
		if (hashmap.containsKey(v1) && hashmap.containsKey(v2)) {
			if (v1.equals(v2) || hashmap.get(v1).contains(v2)
					|| hashmap.get(v2).contains(v1)) {
				return false;
			} else {
				hashmap.get(v1).add(v2);
				hashmap.get(v2).add(v1);
				return true;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc) Return a set of person who are 1-degree friends of the
	 * specified user If the specified user is not found in the graph, throw
	 * IllegalArguemntException
	 * 
	 * @see GraphADT#getNeighbors(java.lang.Object)
	 */
	@Override
	public Set<V> getNeighbors(V vertex) {
		// TODO
		if (vertex == null) {
			throw new IllegalArgumentException();
		}
		if (!hashmap.containsKey(vertex)) {
			throw new IllegalArgumentException();
		}
		Set<V> neighborset = new HashSet<V>(hashmap.get(vertex));
		return neighborset;
	}

	/*
	 * (non-Javadoc) Remove the edge between specified two vertexes if there is
	 * a edge between them
	 * 
	 * @see GraphADT#removeEdge(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void removeEdge(V v1, V v2) {
		// TODO
		if (v1 == null || v2 == null) {
			throw new IllegalArgumentException();
		}
		if (hashmap.containsKey(v1) && hashmap.containsKey(v2)
				&& hashmap.get(v1).contains(v2) && hashmap.get(v2).contains(v1)) {
			hashmap.get(v1).remove(v2);
			hashmap.get(v2).remove(v1);
		}

	}

	/*
	 * (non-Javadoc) Return a set which contains all vertexes in the graph
	 * 
	 * @see GraphADT#getAllVertices()
	 */
	@Override
	public Set<V> getAllVertices() {
		// TODO
		return hashmap.keySet();
	}

	/*
	 * (non-Javadoc) Returns a print of this graph in adjacency lists form.
	 * 
	 * This method has been written for your convenience (e.g., for debugging).
	 * You are free to modify it or remove the method entirely. THIS METHOD WILL
	 * NOT BE PART OF GRADING.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringWriter writer = new StringWriter();
		for (V vertex : this.hashmap.keySet()) {
			writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
		}
		return writer.toString();
	}

}
