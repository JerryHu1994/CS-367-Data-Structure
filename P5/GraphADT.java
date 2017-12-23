import java.util.Set;

/**
 * The interface specifis the implementation of the GraphADT which will be used
 * by the UndirectedGraph class
 * 
 * @author Jerry
 *
 * @param <V>
 */
public interface GraphADT<V> {

	/**
	 * Add a vertex to the graph
	 * 
	 * @param vertex
	 * @return
	 */
	boolean addVertex(V vertex);

	/**
	 * Add an edge between given two vertexes
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	boolean addEdge(V v1, V v2);

	/**
	 * Remove an edge between given two vertexes
	 * 
	 * @param v1
	 * @param v2
	 */
	void removeEdge(V v1, V v2);

	/**
	 * get a set which contains all neighbor vertexes of a given vertex
	 * 
	 * @param vertex
	 * @return
	 */
	Set<V> getNeighbors(V vertex);

	/**
	 * get a set which contains all vextexes in the graph
	 * 
	 * @return
	 */
	Set<V> getAllVertices();

}
